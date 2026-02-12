#!/usr/bin/env python3
"""
Bulk-acquire public-domain EPUB books from Project Gutenberg (via Gutendex).

This script enforces:
- exactly N valid new EPUB additions
- title+author dedupe with normalization
- file hash dedupe
- EPUB archive integrity + readable metadata checks
- canonical folder/file naming in the target library

It also updates:
- Library.txt
- Recommendations.txt
- Book_Sources_Reference.txt
- run reports/manifests under /Volumes/X4-SD/Librarian/reports
"""

from __future__ import annotations

import argparse
import datetime as dt
import hashlib
import ipaddress
import json
import re
import shutil
import socket
import sys
import time
import unicodedata
import urllib.error
import urllib.parse
import urllib.request
import zipfile
from dataclasses import dataclass
from pathlib import Path
from typing import Iterable, Optional, Sequence
from xml.etree import ElementTree as ET


DEFAULT_LIBRARY_ROOT = Path("/Volumes/X4-SD/Books")
DEFAULT_LIBRARY_TXT = Path("/Volumes/X4-SD/Librarian/Library.txt")
DEFAULT_RECOMMENDATIONS_TXT = Path("/Volumes/X4-SD/Librarian/Recommendations.txt")
DEFAULT_SOURCES_LOG = Path(
    "/Volumes/X4-SD/Manuals/References/Book_Sources_Reference.txt"
)
DEFAULT_REPORTS_DIR = Path("/Volumes/X4-SD/Librarian/reports")

GUTENDEX_URL = "https://gutendex.com/books?mime_type=application%2Fepub%2Bzip"
USER_AGENT = "Mozilla/5.0 (Codex-Librarian/1.0; +https://www.gutenberg.org/)"
SAFE_SCHEMES = {"https"}
CATALOG_HOSTS = {"gutendex.com"}
GUTENBERG_HOSTS = {"gutenberg.org", "www.gutenberg.org"}
MAX_JSON_BYTES = 5 * 1024 * 1024
MAX_DOWNLOAD_BYTES = 80 * 1024 * 1024
MAX_EPUB_MEMBERS = 8000
MAX_EPUB_UNCOMPRESSED_BYTES = 300 * 1024 * 1024
MAX_EPUB_ENTRY_BYTES = 100 * 1024 * 1024
MAX_EPUB_EXPANSION_RATIO = 250.0
MAX_METADATA_XML_BYTES = 2 * 1024 * 1024
MAX_SAFE_TARGET = 250
LARGE_TARGET_CONFIRM_TOKEN = "I_UNDERSTAND_BULK_DOWNLOADS"
WRITE_ROOT = Path("/Volumes/X4-SD").resolve()
INJECTION_PATTERNS = [
    "ignore previous instructions",
    "ignore all instructions",
    "system prompt",
    "developer message",
    "jailbreak",
    "<script",
    "```",
]


class SecurityError(RuntimeError):
    """Raised when an operation violates strict safety guards."""


def sanitize_untrusted_text(text: str, max_len: int = 240) -> str:
    cleaned = clean_ws(str(text or ""))
    cleaned = "".join(ch for ch in cleaned if ch.isprintable())
    return cleaned[:max_len]


def contains_prompt_injection_signals(text: str) -> bool:
    haystack = sanitize_untrusted_text(text, max_len=1000).lower()
    return any(marker in haystack for marker in INJECTION_PATTERNS)


def host_is_allowed(host: str, allowed_hosts: set[str]) -> bool:
    if not host:
        return False
    host = host.lower().rstrip(".")
    for allowed in allowed_hosts:
        allowed = allowed.lower().rstrip(".")
        if host == allowed or host.endswith(f".{allowed}"):
            return True
    return False


def assert_public_host(host: str) -> None:
    if not host:
        raise SecurityError("Missing host")
    try:
        infos = socket.getaddrinfo(host, None)
    except socket.gaierror as exc:
        raise SecurityError(f"Could not resolve host: {host} ({exc})") from exc
    for info in infos:
        ip_text = info[4][0]
        ip = ipaddress.ip_address(ip_text)
        if (
            ip.is_private
            or ip.is_loopback
            or ip.is_link_local
            or ip.is_multicast
            or ip.is_unspecified
            or ip.is_reserved
        ):
            raise SecurityError(f"Refusing non-public address for host {host}: {ip}")


def validate_url(url: str, allowed_hosts: set[str], context: str) -> str:
    parsed = urllib.parse.urlparse(clean_ws(url))
    if parsed.scheme.lower() not in SAFE_SCHEMES:
        raise SecurityError(f"{context}: URL scheme must be https: {url}")
    host = (parsed.hostname or "").lower()
    if not host_is_allowed(host, allowed_hosts):
        raise SecurityError(f"{context}: URL host not allowed: {host}")
    assert_public_host(host)
    return parsed.geturl()


def ensure_path_inside(path: Path, root: Path, label: str) -> Path:
    root_resolved = root.resolve()
    path_resolved = path.resolve()
    try:
        path_resolved.relative_to(root_resolved)
    except ValueError as exc:
        raise SecurityError(f"{label} escapes write root: {path_resolved}") from exc
    return path_resolved


def validate_write_paths(paths: Sequence[Path]) -> None:
    for path in paths:
        ensure_path_inside(path, WRITE_ROOT, "Configured path")


class SafeRedirectHandler(urllib.request.HTTPRedirectHandler):
    def __init__(self, allowed_hosts: set[str], context: str) -> None:
        self.allowed_hosts = allowed_hosts
        self.context = context
        super().__init__()

    def redirect_request(self, req, fp, code, msg, headers, newurl):  # type: ignore[override]
        validate_url(newurl, self.allowed_hosts, f"{self.context} redirect")
        return super().redirect_request(req, fp, code, msg, headers, newurl)


def build_secure_opener(allowed_hosts: set[str], context: str) -> urllib.request.OpenerDirector:
    # Disable proxy env vars to reduce SSRF surface and lock redirects to allowlist.
    return urllib.request.build_opener(
        urllib.request.ProxyHandler({}),
        SafeRedirectHandler(allowed_hosts, context),
    )


def stream_limited(resp, dst: Path, max_bytes: int) -> int:
    written = 0
    with dst.open("wb") as out:
        while True:
            chunk = resp.read(1024 * 128)
            if not chunk:
                break
            written += len(chunk)
            if written > max_bytes:
                raise SecurityError(f"Download exceeded size limit ({max_bytes} bytes)")
            out.write(chunk)
    return written


def read_limited(resp, max_bytes: int) -> bytes:
    data = resp.read(max_bytes + 1)
    if len(data) > max_bytes:
        raise SecurityError(f"Response exceeded size limit ({max_bytes} bytes)")
    return data


@dataclass
class AddedBook:
    gutenberg_id: int
    title: str
    author: str
    genre: str
    source_url: str
    source_page_url: str
    path: Path
    sha256: str
    subjects: list[str]
    bookshelves: list[str]
    acquired_on: str

    @property
    def key(self) -> str:
        return normalize_key(self.title, self.author)


def clean_ws(text: str) -> str:
    return re.sub(r"\s+", " ", (text or "")).strip()


def strip_diacritics(text: str) -> str:
    normalized = unicodedata.normalize("NFKD", text)
    return "".join(ch for ch in normalized if not unicodedata.combining(ch))


def normalize_for_match(text: str) -> str:
    text = strip_diacritics(clean_ws(text).lower())
    return "".join(ch for ch in text if ch.isalnum())


def normalize_key(title: str, author: str) -> str:
    return f"{normalize_for_match(title)}|{normalize_for_match(author)}"


def safe_component(text: str, max_len: int = 160) -> str:
    text = clean_ws(text)
    text = (
        text.replace("/", " ")
        .replace("\\", " ")
        .replace(":", " ")
        .replace("*", " ")
        .replace("?", "")
        .replace('"', "")
        .replace("<", "")
        .replace(">", "")
        .replace("|", " ")
    )
    text = re.sub(r"\s+", " ", text).strip(" .")
    text = text or "Unknown"
    return text[:max_len].rstrip(" .")


def format_author(name: str) -> str:
    name = clean_ws(name)
    if not name:
        return "Unknown"
    if "," in name:
        parts = [clean_ws(p) for p in name.split(",") if clean_ws(p)]
        if len(parts) >= 2:
            return safe_component(f"{parts[0]}, {' '.join(parts[1:])}", 120)
        return safe_component(parts[0], 120)

    tokens = [t for t in name.split(" ") if t]
    if len(tokens) == 1:
        return safe_component(tokens[0], 120)
    last = tokens[-1]
    first = " ".join(tokens[:-1])
    return safe_component(f"{last}, {first}", 120)


def author_bucket(author_display: str) -> str:
    last = clean_ws(author_display.split(",", 1)[0] if "," in author_display else author_display)
    for ch in last:
        if ch.isalpha():
            return ch.upper()
    return "#"


def sha256_file(path: Path) -> str:
    h = hashlib.sha256()
    with path.open("rb") as f:
        for chunk in iter(lambda: f.read(1024 * 1024), b""):
            h.update(chunk)
    return h.hexdigest()


def is_zip_file(path: Path) -> bool:
    return zipfile.is_zipfile(path)


def zip_integrity_ok(path: Path) -> tuple[bool, str]:
    try:
        with zipfile.ZipFile(path) as zf:
            infos = zf.infolist()
            if not infos:
                return False, "zip archive has no entries"
            if len(infos) > MAX_EPUB_MEMBERS:
                return False, f"zip has too many entries ({len(infos)})"

            total_comp = 0
            total_uncomp = 0
            for info in infos:
                total_comp += max(info.compress_size, 0)
                total_uncomp += max(info.file_size, 0)
                if info.file_size > MAX_EPUB_ENTRY_BYTES:
                    return False, f"zip entry too large: {info.filename}"

            if total_uncomp > MAX_EPUB_UNCOMPRESSED_BYTES:
                return False, "zip uncompressed size too large"
            ratio = total_uncomp / max(total_comp, 1)
            if ratio > MAX_EPUB_EXPANSION_RATIO:
                return False, "zip compression ratio indicates zip bomb risk"

            bad = zf.testzip()
            if bad is not None:
                return False, f"zip failed integrity check at entry: {bad}"
            return True, ""
    except Exception as exc:  # noqa: BLE001
        return False, str(exc)


def read_first_text(elements: Iterable[ET.Element]) -> str:
    for el in elements:
        txt = clean_ws(el.text or "")
        if txt:
            return txt
    return ""


def read_zip_member_limited(zf: zipfile.ZipFile, member: str, max_bytes: int) -> bytes:
    info = zf.getinfo(member)
    if info.file_size > max_bytes:
        raise SecurityError(f"Metadata entry too large: {member}")
    data = zf.read(member)
    if len(data) > max_bytes:
        raise SecurityError(f"Metadata bytes too large: {member}")
    return data


def extract_epub_metadata(path: Path) -> tuple[str, str]:
    """
    Return (title, author). Empty strings if unavailable.
    """
    try:
        with zipfile.ZipFile(path) as zf:
            if "META-INF/container.xml" not in zf.namelist():
                return "", ""
            container_xml = read_zip_member_limited(zf, "META-INF/container.xml", MAX_METADATA_XML_BYTES)
            container = ET.fromstring(container_xml)
            rootfile = container.find(".//{*}rootfile")
            if rootfile is None:
                return "", ""
            opf_path = rootfile.attrib.get("full-path", "")
            if not opf_path:
                return "", ""
            if opf_path.startswith("/") or ".." in Path(opf_path).parts:
                return "", ""

            opf_xml = read_zip_member_limited(zf, opf_path, MAX_METADATA_XML_BYTES)
            opf = ET.fromstring(opf_xml)

            title = read_first_text(opf.findall(".//{http://purl.org/dc/elements/1.1/}title"))
            author = read_first_text(opf.findall(".//{http://purl.org/dc/elements/1.1/}creator"))

            if not author:
                author = read_first_text(opf.findall(".//{*}creator"))
            if not title:
                title = read_first_text(opf.findall(".//{*}title"))

            return sanitize_untrusted_text(title), sanitize_untrusted_text(author)
    except Exception:
        return "", ""


def fallback_name_from_filename(path: Path) -> tuple[str, str]:
    stem = path.stem
    if " - " in stem:
        title, author = stem.rsplit(" - ", 1)
        return sanitize_untrusted_text(title), sanitize_untrusted_text(author)
    return sanitize_untrusted_text(stem), sanitize_untrusted_text(path.parent.name)


def fetch_json(url: str, retries: int = 4, pause_seconds: float = 1.5) -> dict:
    safe_url = validate_url(url, CATALOG_HOSTS, "Catalog fetch")
    opener = build_secure_opener(CATALOG_HOSTS, "Catalog fetch")
    last_error: Optional[Exception] = None
    for attempt in range(1, retries + 1):
        req = urllib.request.Request(
            safe_url,
            headers={"User-Agent": USER_AGENT, "Accept": "application/json"},
        )
        try:
            with opener.open(req, timeout=60) as resp:
                validate_url(resp.geturl(), CATALOG_HOSTS, "Catalog response")
                data = read_limited(resp, MAX_JSON_BYTES)
                return json.loads(data.decode("utf-8"))
        except Exception as exc:  # noqa: BLE001
            last_error = exc
            time.sleep(pause_seconds * attempt)
    raise RuntimeError(f"Failed to fetch JSON after {retries} attempts: {safe_url} ({last_error})")


def download_file(url: str, dst: Path, retries: int = 4, pause_seconds: float = 1.5) -> None:
    safe_url = validate_url(url, GUTENBERG_HOSTS, "EPUB download")
    opener = build_secure_opener(GUTENBERG_HOSTS, "EPUB download")
    last_error: Optional[Exception] = None
    for attempt in range(1, retries + 1):
        req = urllib.request.Request(safe_url, headers={"User-Agent": USER_AGENT})
        try:
            with opener.open(req, timeout=90) as resp:
                validate_url(resp.geturl(), GUTENBERG_HOSTS, "EPUB response")
                content_type = (resp.headers.get("Content-Type") or "").split(";", 1)[0].strip().lower()
                if content_type and content_type not in {
                    "application/epub+zip",
                    "application/zip",
                    "application/octet-stream",
                }:
                    raise SecurityError(f"Unexpected content type: {content_type}")
                stream_limited(resp, dst, MAX_DOWNLOAD_BYTES)
            return
        except Exception as exc:  # noqa: BLE001
            last_error = exc
            time.sleep(pause_seconds * attempt)
    raise RuntimeError(f"Failed to download after {retries} attempts: {safe_url} ({last_error})")


def classify_genre(subjects: list[str], bookshelves: list[str]) -> str:
    haystack = " ".join(subjects + bookshelves).lower()

    rules = [
        ("Science Fiction", ["science fiction"]),
        ("Gothic Horror", ["horror", "ghost stories", "gothic"]),
        ("Mystery & Detective Fiction", ["detective", "mystery"]),
        ("Fantasy & Children's Literature", ["children", "juvenile", "fairy", "fantasy"]),
        ("Poetry", ["poetry", "poems"]),
        ("Drama", ["drama", "plays", "tragedies", "comedies"]),
        ("History", ["history", "historical"]),
        ("Ancient Philosophy", ["stoics", "plato", "aristotle", "epictetus", "seneca"]),
        ("Modern Philosophy", ["philosophy", "ethics", "metaphysics", "logic"]),
        ("Eastern Philosophy & Religion", ["hinduism", "buddhism", "taoism", "confucian", "religion"]),
        ("Economics", ["economics", "political economy"]),
        ("Logic & Mathematics", ["mathematics", "logic", "geometry", "algebra"]),
        ("Physics", ["physics", "astronomy", "electricity", "thermodynamics"]),
        ("Biology & Natural History", ["biology", "zoology", "botany", "natural history", "evolution"]),
        ("Classic Fiction", ["fiction", "novel", "short stories", "literature"]),
    ]
    for genre, terms in rules:
        if any(term in haystack for term in terms):
            return genre
    return "General Literature"


def parse_recommendation_line(line: str) -> Optional[tuple[str, str, str]]:
    """
    Returns (prefix_with_number, author, title_plus_notes)
    """
    match = re.match(r"^(\s*\d+\.\s+)(?:\[ACQUIRED\]\s+)?(.+)$", line)
    if not match:
        return None
    prefix = match.group(1)
    content = match.group(2).strip()
    if " - " not in content:
        return None
    author, title = content.split(" - ", 1)
    return prefix, clean_ws(author), clean_ws(title)


def title_variants(title: str) -> set[str]:
    variants = {normalize_for_match(title)}
    stripped_paren = re.sub(r"\([^)]*\)", "", title).strip()
    if stripped_paren:
        variants.add(normalize_for_match(stripped_paren))
    before_colon = title.split(":", 1)[0].strip()
    if before_colon:
        variants.add(normalize_for_match(before_colon))
    before_dash = title.split(" - ", 1)[0].strip()
    if before_dash:
        variants.add(normalize_for_match(before_dash))
    return {v for v in variants if v}


def build_existing_index(library_root: Path) -> tuple[set[str], set[str], int]:
    existing_keys: set[str] = set()
    existing_hashes: set[str] = set()
    epub_paths = sorted(
        p
        for p in library_root.rglob("*.epub")
        if p.is_file() and not p.name.startswith("._")
    )

    for path in epub_paths:
        try:
            existing_hashes.add(sha256_file(path))
        except Exception:  # noqa: BLE001
            pass

        title, author = extract_epub_metadata(path)
        if not (title and author):
            title, author = fallback_name_from_filename(path)
        if title and author:
            existing_keys.add(normalize_key(title, author))

    return existing_keys, existing_hashes, len(epub_paths)


def compose_destination(library_root: Path, title: str, author: str) -> Path:
    author_display = format_author(author)
    letter = author_bucket(author_display)
    author_dir = safe_component(author_display, 120)
    title_display = safe_component(title, 170)
    file_stem = safe_component(f"{title_display} - {author_display}", 220)
    dest_dir = ensure_path_inside(library_root / letter / author_dir, library_root, "Destination directory")
    dest_dir.mkdir(parents=True, exist_ok=True)
    candidate = ensure_path_inside(dest_dir / f"{file_stem}.epub", library_root, "Destination file")
    if not candidate.exists():
        return candidate

    # If file name collision occurs, suffix numeric index.
    n = 2
    while True:
        alt = ensure_path_inside(dest_dir / f"{file_stem} ({n}).epub", library_root, "Destination file")
        if not alt.exists():
            return alt
        n += 1


def update_library_txt(library_txt: Path, added: list[AddedBook], run_label: str) -> None:
    grouped: dict[str, list[AddedBook]] = {}
    for book in added:
        grouped.setdefault(book.genre, []).append(book)

    lines: list[str] = []
    lines.append("")
    lines.append("---")
    lines.append("")
    lines.append(f"## Public Domain Acquisitions ({run_label})")
    lines.append("")
    for genre in sorted(grouped):
        lines.append(f"### {sanitize_untrusted_text(genre)}")
        books = sorted(grouped[genre], key=lambda b: (normalize_for_match(b.author), normalize_for_match(b.title)))
        for b in books:
            lines.append(f"- {sanitize_untrusted_text(b.author)} - {sanitize_untrusted_text(b.title)}")
        lines.append("")

    library_txt.parent.mkdir(parents=True, exist_ok=True)
    with library_txt.open("a", encoding="utf-8") as f:
        f.write("\n".join(lines))


def update_recommendations_txt(recommendations_txt: Path, added: list[AddedBook]) -> int:
    added_author_to_title_variants: dict[str, set[str]] = {}
    for b in added:
        author_key = normalize_for_match(b.author)
        variants = title_variants(b.title)
        added_author_to_title_variants.setdefault(author_key, set()).update(variants)

    if not recommendations_txt.exists():
        recommendations_txt.parent.mkdir(parents=True, exist_ok=True)
        recommendations_txt.write_text("", encoding="utf-8")

    lines = recommendations_txt.read_text(encoding="utf-8").splitlines()
    updated: list[str] = []
    marked = 0

    for line in lines:
        parsed = parse_recommendation_line(line)
        if not parsed:
            updated.append(line)
            continue

        prefix, author, title = parsed
        if "[ACQUIRED]" in line:
            updated.append(line)
            continue

        author_key = normalize_for_match(author)
        rec_title_variants = title_variants(title)
        matches = bool(
            author_key in added_author_to_title_variants
            and rec_title_variants.intersection(added_author_to_title_variants[author_key])
        )

        if matches:
            updated.append(f"{prefix}[ACQUIRED] {author} - {title}")
            marked += 1
        else:
            updated.append(line)

    recommendations_txt.write_text("\n".join(updated) + "\n", encoding="utf-8")
    return marked


def append_sources_log(sources_log: Path, added: list[AddedBook], run_label: str) -> None:
    lines: list[str] = []
    lines.append("")
    lines.append("================================================================================")
    lines.append(f"BULK ACQUISITION RUN: {run_label} (Project Gutenberg EPUB)")
    lines.append("================================================================================")
    for idx, b in enumerate(added, start=1):
        lines.append(
            f"{idx}. {sanitize_untrusted_text(b.title)} - {sanitize_untrusted_text(b.author)} | "
            f"{b.source_url} | {b.acquired_on}"
        )

    sources_log.parent.mkdir(parents=True, exist_ok=True)
    with sources_log.open("a", encoding="utf-8") as f:
        f.write("\n".join(lines) + "\n")


def write_reports(
    reports_dir: Path,
    run_stamp: str,
    before_count: int,
    after_count: int,
    added: list[AddedBook],
    skipped: list[dict],
) -> tuple[Path, Path, Path]:
    reports_dir.mkdir(parents=True, exist_ok=True)
    manifest_jsonl = reports_dir / f"acquired_epubs_{run_stamp}.jsonl"
    skipped_jsonl = reports_dir / f"skipped_candidates_{run_stamp}.jsonl"
    report_md = reports_dir / f"acquisition_report_{run_stamp}.md"

    with manifest_jsonl.open("w", encoding="utf-8") as f:
        for b in added:
            f.write(
                json.dumps(
                    {
                        "gutenberg_id": b.gutenberg_id,
                        "title": sanitize_untrusted_text(b.title),
                        "author": sanitize_untrusted_text(b.author),
                        "genre": sanitize_untrusted_text(b.genre),
                        "path": str(b.path.resolve()),
                        "source_url": b.source_url,
                        "source_page_url": b.source_page_url,
                        "sha256": b.sha256,
                        "acquired_on": b.acquired_on,
                    },
                    ensure_ascii=False,
                )
                + "\n"
            )

    with skipped_jsonl.open("w", encoding="utf-8") as f:
        for row in skipped:
            scrubbed = {
                "gutenberg_id": row.get("gutenberg_id"),
                "title": sanitize_untrusted_text(str(row.get("title", ""))),
                "author": sanitize_untrusted_text(str(row.get("author", ""))),
                "source_url": sanitize_untrusted_text(str(row.get("source_url", ""))),
                "reason": sanitize_untrusted_text(str(row.get("reason", ""))),
                "detail": sanitize_untrusted_text(str(row.get("detail", "")), max_len=500),
            }
            f.write(json.dumps(scrubbed, ensure_ascii=False) + "\n")

    with report_md.open("w", encoding="utf-8") as f:
        f.write("# Bulk Acquisition Report\n\n")
        f.write(f"- Run stamp: `{run_stamp}`\n")
        f.write(f"- Books before (.epub in main library): `{before_count}`\n")
        f.write(f"- Books after (.epub in main library): `{after_count}`\n")
        f.write(f"- Net added: `{after_count - before_count}`\n")
        f.write(f"- Valid new EPUBs added: `{len(added)}`\n")
        f.write(f"- Skipped candidates: `{len(skipped)}`\n\n")
        f.write("## Added Books (Absolute Path + Source URL)\n\n")
        for b in added:
            f.write(
                f"- `{b.path.resolve()}` | {b.source_url} | "
                f"{sanitize_untrusted_text(b.author)} - {sanitize_untrusted_text(b.title)}\n"
            )
        f.write("\n## Skipped Candidates\n\n")
        for row in skipped:
            bid = row.get("gutenberg_id", "unknown")
            reason = sanitize_untrusted_text(str(row.get("reason", "unknown")))
            title = sanitize_untrusted_text(str(row.get("title", "")))
            author = sanitize_untrusted_text(str(row.get("author", "")))
            detail = sanitize_untrusted_text(str(row.get("detail", "")), max_len=500)
            f.write(f"- id={bid} | {title} | {author} | {reason} | {detail}\n")

    return manifest_jsonl, skipped_jsonl, report_md


def run(args: argparse.Namespace) -> int:
    today = dt.date.today().isoformat()
    run_stamp = dt.datetime.now().strftime("%Y%m%d_%H%M%S")
    run_label = f"{today} {run_stamp}"

    target = args.target
    if target <= 0:
        raise SystemExit("target must be > 0")
    if target > MAX_SAFE_TARGET and args.confirm_large_target != LARGE_TARGET_CONFIRM_TOKEN:
        raise SystemExit(
            "target exceeds safe threshold. Pass "
            f"--confirm-large-target {LARGE_TARGET_CONFIRM_TOKEN} to continue."
        )

    library_root = args.library_root.expanduser().resolve()
    library_txt = args.library_txt.expanduser().resolve()
    recommendations_txt = args.recommendations_txt.expanduser().resolve()
    sources_log = args.sources_log.expanduser().resolve()
    reports_dir = args.reports_dir.expanduser().resolve()
    temp_dir = args.temp_dir.expanduser().resolve()

    validate_write_paths(
        [
            library_root,
            library_txt,
            recommendations_txt,
            sources_log,
            reports_dir,
            temp_dir,
        ]
    )

    if not library_root.exists() or not library_root.is_dir():
        raise SystemExit(f"library root not found or not a directory: {library_root}")

    temp_dir.mkdir(parents=True, exist_ok=True)

    existing_keys, existing_hashes, before_count = build_existing_index(library_root)
    print(f"[INFO] Existing EPUB count in library: {before_count}")
    print(f"[INFO] Existing dedupe keys: {len(existing_keys)}")
    print(f"[INFO] Existing file hashes: {len(existing_hashes)}")

    added: list[AddedBook] = []
    skipped: list[dict] = []

    next_url = validate_url(GUTENDEX_URL, CATALOG_HOSTS, "Initial catalog URL")
    pages_seen = 0

    while len(added) < target and next_url:
        if args.max_pages and pages_seen >= args.max_pages:
            break
        pages_seen += 1
        print(f"[INFO] Fetching catalog page {pages_seen}: {next_url}")
        payload = fetch_json(next_url)
        raw_next = payload.get("next")
        if raw_next:
            try:
                next_url = validate_url(str(raw_next), CATALOG_HOSTS, "Catalog next page URL")
            except Exception as exc:  # noqa: BLE001
                print(f"[WARN] Stopping pagination due to unsafe next URL: {exc}")
                next_url = ""
        else:
            next_url = ""
        results = payload.get("results") or []

        for row in results:
            if len(added) >= target:
                break

            book_id = int(row.get("id", 0))
            api_title = sanitize_untrusted_text(row.get("title", ""))
            authors = row.get("authors") or []
            api_author = sanitize_untrusted_text(authors[0].get("name", "")) if authors else ""
            formats = row.get("formats") or {}
            epub_url = formats.get("application/epub+zip")
            source_page_url = f"https://www.gutenberg.org/ebooks/{book_id}" if book_id > 0 else ""

            def skip(reason: str, detail: str = "") -> None:
                skipped.append(
                    {
                        "gutenberg_id": book_id,
                        "title": sanitize_untrusted_text(api_title),
                        "author": sanitize_untrusted_text(api_author),
                        "source_url": sanitize_untrusted_text(epub_url or ""),
                        "reason": sanitize_untrusted_text(reason),
                        "detail": sanitize_untrusted_text(detail, max_len=500),
                    }
                )

            if not epub_url:
                skip("missing_epub_url")
                continue
            if not api_title or not api_author:
                skip("missing_catalog_metadata")
                continue
            if contains_prompt_injection_signals(api_title) or contains_prompt_injection_signals(api_author):
                skip("prompt_injection_pattern_in_catalog_metadata")
                continue
            try:
                epub_url = validate_url(str(epub_url), GUTENBERG_HOSTS, "EPUB URL")
                source_page_url = validate_url(source_page_url, GUTENBERG_HOSTS, "Source page URL")
            except Exception as exc:  # noqa: BLE001
                skip("unsafe_source_url", str(exc))
                continue

            pre_key = normalize_key(api_title, api_author)
            if pre_key in existing_keys:
                skip("duplicate_title_author_precheck")
                continue

            temp_path = ensure_path_inside(temp_dir / f"gutenberg_{book_id}.epub", temp_dir, "Temp file")
            if temp_path.exists():
                temp_path.unlink()

            try:
                download_file(epub_url, temp_path)
            except Exception as exc:  # noqa: BLE001
                skip("download_failed", str(exc))
                continue

            if not is_zip_file(temp_path):
                temp_path.unlink(missing_ok=True)
                skip("not_zip_epub")
                continue
            zip_ok, zip_reason = zip_integrity_ok(temp_path)
            if not zip_ok:
                temp_path.unlink(missing_ok=True)
                skip("zip_integrity_failed", zip_reason)
                continue

            meta_title, meta_author = extract_epub_metadata(temp_path)
            if not (meta_title and meta_author):
                temp_path.unlink(missing_ok=True)
                skip("metadata_missing_title_or_author")
                continue
            if contains_prompt_injection_signals(meta_title) or contains_prompt_injection_signals(meta_author):
                temp_path.unlink(missing_ok=True)
                skip("prompt_injection_pattern_in_epub_metadata")
                continue

            post_key = normalize_key(meta_title, meta_author)
            if post_key in existing_keys:
                temp_path.unlink(missing_ok=True)
                skip("duplicate_title_author_postcheck")
                continue

            try:
                digest = sha256_file(temp_path)
            except Exception as exc:  # noqa: BLE001
                temp_path.unlink(missing_ok=True)
                skip("sha256_failed", str(exc))
                continue

            if digest in existing_hashes:
                temp_path.unlink(missing_ok=True)
                skip("duplicate_hash")
                continue

            subjects = [
                sanitize_untrusted_text(s, max_len=180)
                for s in (row.get("subjects") or [])
                if sanitize_untrusted_text(s, max_len=180)
                and not contains_prompt_injection_signals(str(s))
            ]
            bookshelves = [
                sanitize_untrusted_text(s, max_len=180)
                for s in (row.get("bookshelves") or [])
                if sanitize_untrusted_text(s, max_len=180)
                and not contains_prompt_injection_signals(str(s))
            ]
            genre = classify_genre(subjects, bookshelves)

            author_display = format_author(meta_author)
            title_display = sanitize_untrusted_text(meta_title)
            dest = compose_destination(library_root, title_display, author_display)

            if dest.exists():
                try:
                    if sha256_file(dest) == digest:
                        temp_path.unlink(missing_ok=True)
                        skip("duplicate_hash_destination_exists")
                        continue
                except Exception:  # noqa: BLE001
                    pass

            shutil.move(str(temp_path), str(dest))

            book = AddedBook(
                gutenberg_id=book_id,
                title=title_display,
                author=author_display,
                genre=genre,
                source_url=epub_url,
                source_page_url=source_page_url,
                path=dest,
                sha256=digest,
                subjects=subjects,
                bookshelves=bookshelves,
                acquired_on=today,
            )
            added.append(book)
            existing_keys.add(post_key)
            existing_hashes.add(digest)
            print(f"[ADD {len(added):04d}/{target}] {book.title} - {book.author}")

    if len(added) != target:
        print(f"[ERROR] Could not reach target. Added={len(added)} target={target}")
        return 2

    after_count = len(
        [p for p in library_root.rglob("*.epub") if p.is_file() and not p.name.startswith("._")]
    )
    if after_count - before_count != target:
        print(
            "[ERROR] Post-run count mismatch: "
            f"before={before_count} after={after_count} expected_delta={target}"
        )
        return 3

    update_library_txt(library_txt, added, run_label)
    marked = update_recommendations_txt(recommendations_txt, added)
    append_sources_log(sources_log, added, run_label)
    manifest_jsonl, skipped_jsonl, report_md = write_reports(
        reports_dir=reports_dir,
        run_stamp=run_stamp,
        before_count=before_count,
        after_count=after_count,
        added=added,
        skipped=skipped,
    )

    summary = {
        "before_count": before_count,
        "after_count": after_count,
        "net_added": after_count - before_count,
        "target": target,
        "added": len(added),
        "skipped_candidates": len(skipped),
        "recommendations_marked_acquired": marked,
        "manifest_jsonl": str(manifest_jsonl),
        "skipped_jsonl": str(skipped_jsonl),
        "report_md": str(report_md),
    }
    print("[SUCCESS] Acquisition completed.")
    print(json.dumps(summary, indent=2, ensure_ascii=False))
    return 0


def build_arg_parser() -> argparse.ArgumentParser:
    p = argparse.ArgumentParser(description="Acquire new legal EPUBs into the library.")
    p.add_argument("--target", type=int, default=25, help="Exactly how many new EPUBs to add.")
    p.add_argument("--max-pages", type=int, default=0, help="Optional safety cap for catalog pages.")
    p.add_argument(
        "--confirm-large-target",
        default="",
        help=f"Required token when target > {MAX_SAFE_TARGET}.",
    )
    p.add_argument("--library-root", type=Path, default=DEFAULT_LIBRARY_ROOT)
    p.add_argument("--library-txt", type=Path, default=DEFAULT_LIBRARY_TXT)
    p.add_argument("--recommendations-txt", type=Path, default=DEFAULT_RECOMMENDATIONS_TXT)
    p.add_argument("--sources-log", type=Path, default=DEFAULT_SOURCES_LOG)
    p.add_argument("--reports-dir", type=Path, default=DEFAULT_REPORTS_DIR)
    p.add_argument("--temp-dir", type=Path, default=Path("/Volumes/X4-SD/XTCache/.epub-acquire-tmp"))
    return p


def main() -> int:
    parser = build_arg_parser()
    args = parser.parse_args()
    return run(args)


if __name__ == "__main__":
    sys.exit(main())
