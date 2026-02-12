#!/usr/bin/env python3
"""Acquire legal English EPUBs similar to user tastes from recommendations."""

from __future__ import annotations

import argparse
import datetime as dt
import json
import re
import shutil
import urllib.parse
from dataclasses import dataclass
from pathlib import Path

from acquire_public_domain_epubs import (
    build_existing_index,
    clean_ws,
    compose_destination,
    download_file,
    extract_epub_metadata,
    fetch_json,
    format_author,
    is_zip_file,
    normalize_key,
    sha256_file,
    zip_integrity_ok,
)

DEFAULT_LIBRARY_ROOT = Path("/Volumes/X4-SD/Books")
DEFAULT_RECOMMENDATIONS_TXT = Path("/Volumes/X4-SD/Librarian/Recommendations.txt")
DEFAULT_REPORTS_DIR = Path("/Volumes/X4-SD/Librarian/reports")
DEFAULT_TEMP_DIR = Path("/Volumes/X4-SD/XTCache/.epub-similar-tmp")

SOURCE_NAME = "Project Gutenberg"
SOURCE_HOME = "https://www.gutenberg.org/"
SOURCE_PROOF_URL = "https://www.gutenberg.org/help/copyright.html"
LICENSE_STATUS = "verified"
LICENSE_LABEL = "Public Domain (US)"

SKIP_NOTES = (
    "copyrighted",
    "not on gutenberg",
    "not available",
)

FALLBACK_QUERIES = [
    "Plutarch Parallel Lives",
    "Thomas Reid Essays on the Intellectual Powers of Man",
    "Dio Chrysostom Discourses",
    "Porphyry On Abstinence from Animal Food",
    "Durkheim Suicide A Study in Sociology",
    "Simmel The Philosophy of Money",
    "Rudolf Clausius The Mechanical Theory of Heat",
    "Joseph Fourier The Analytical Theory of Heat",
    "Sadi Carnot Reflections on the Motive Power of Fire",
    "Lord Rayleigh The Theory of Sound",
    "Hendrik Lorentz Theory of Electrons",
    "Max Planck Theory of Heat Radiation",
    "Jean Piaget The Psychology of the Child",
    "Lev Vygotsky Thought and Language",
]


@dataclass
class QuerySeed:
    author: str
    title: str
    query: str
    source: str


@dataclass
class AddedBook:
    gutenberg_id: int
    title: str
    author: str
    path: Path
    source_url: str
    source_page_url: str
    proof_url: str
    source_home: str
    source_name: str
    license_status: str
    license_label: str
    acquired_on: str
    query: str
    query_source: str
    sha256: str


def parse_args() -> argparse.Namespace:
    p = argparse.ArgumentParser(description=__doc__)
    p.add_argument("--target", type=int, default=25, help="Maximum new books to add.")
    p.add_argument("--library-root", type=Path, default=DEFAULT_LIBRARY_ROOT)
    p.add_argument("--recommendations-txt", type=Path, default=DEFAULT_RECOMMENDATIONS_TXT)
    p.add_argument("--reports-dir", type=Path, default=DEFAULT_REPORTS_DIR)
    p.add_argument("--temp-dir", type=Path, default=DEFAULT_TEMP_DIR)
    p.add_argument(
        "--max-results-per-query",
        type=int,
        default=12,
        help="How many Gutendex rows to inspect per query seed.",
    )
    p.add_argument(
        "--max-seeds",
        type=int,
        default=60,
        help="Maximum query seeds to evaluate (0 = no cap).",
    )
    return p.parse_args()


def parse_recommendation_line(line: str) -> tuple[bool, str, str] | None:
    match = re.match(r"^\s*\d+\.\s+(\[ACQUIRED\]\s+)?(.+?)\s+-\s+(.+)$", line)
    if not match:
        return None
    acquired = bool(match.group(1))
    author = clean_ws(match.group(2))
    title = clean_ws(match.group(3))
    return acquired, author, title


def title_without_notes(title: str) -> str:
    stripped = re.sub(r"\([^)]*\)", "", title).strip()
    return clean_ws(stripped)


def is_eligible_recommendation(title: str) -> bool:
    t = title.lower()
    return not any(note in t for note in SKIP_NOTES)


def load_query_seeds(path: Path) -> list[QuerySeed]:
    lines = path.read_text(encoding="utf-8", errors="ignore").splitlines()
    seeds: list[QuerySeed] = []
    for line in lines:
        parsed = parse_recommendation_line(line)
        if not parsed:
            continue
        acquired, author, title = parsed
        if acquired:
            continue
        if not is_eligible_recommendation(title):
            continue
        plain_title = title_without_notes(title)
        if not plain_title:
            continue
        query = clean_ws(f"{author} {plain_title}")
        seeds.append(
            QuerySeed(
                author=author,
                title=plain_title,
                query=query,
                source="recommendations",
            )
        )
    for q in FALLBACK_QUERIES:
        seeds.append(QuerySeed(author="", title=q, query=q, source="fallback"))
    return seeds


def gutendex_search(query: str) -> list[dict]:
    encoded = urllib.parse.quote_plus(query)
    url = (
        "https://gutendex.com/books"
        f"?search={encoded}&mime_type=application%2Fepub%2Bzip&languages=en"
    )
    payload = fetch_json(url, retries=2, pause_seconds=0.5)
    return payload.get("results") or []


def pick_epub_url(formats: dict) -> str:
    candidates = [
        formats.get("application/epub+zip"),
        formats.get("application/epub+zip; charset=utf-8"),
        formats.get("application/octet-stream"),
    ]
    for c in candidates:
        if not c:
            continue
        host = urllib.parse.urlparse(c).netloc.lower()
        if "gutenberg.org" in host:
            return c
    return ""


def write_reports(
    reports_dir: Path,
    run_stamp: str,
    before_count: int,
    after_count: int,
    seeds_total: int,
    added: list[AddedBook],
    skipped: list[dict],
) -> tuple[Path, Path, Path, Path]:
    reports_dir.mkdir(parents=True, exist_ok=True)
    manifest_jsonl = reports_dir / f"similar_acquired_epubs_{run_stamp}.jsonl"
    skipped_jsonl = reports_dir / f"similar_skipped_candidates_{run_stamp}.jsonl"
    report_md = reports_dir / f"similar_acquisition_report_{run_stamp}.md"
    latest_json = reports_dir / "similar_acquisition_latest.json"

    with manifest_jsonl.open("w", encoding="utf-8") as f:
        for b in added:
            f.write(
                json.dumps(
                    {
                        "title": b.title,
                        "author": b.author,
                        "source_url": b.source_url,
                        "source_page_url": b.source_page_url,
                        "proof_url": b.proof_url,
                        "source_home": b.source_home,
                        "source_name": b.source_name,
                        "license_status": b.license_status,
                        "license_label": b.license_label,
                        "path": str(b.path.resolve()),
                        "sha256": b.sha256,
                        "gutenberg_id": b.gutenberg_id,
                        "query": b.query,
                        "query_source": b.query_source,
                        "acquired_on": b.acquired_on,
                    },
                    ensure_ascii=False,
                )
                + "\n"
            )

    with skipped_jsonl.open("w", encoding="utf-8") as f:
        for row in skipped:
            f.write(json.dumps(row, ensure_ascii=False) + "\n")

    with report_md.open("w", encoding="utf-8") as f:
        f.write("# Similar-Taste Legal EPUB Acquisition Report\n\n")
        f.write(f"- Run stamp: `{run_stamp}`\n")
        f.write(f"- Source: `{SOURCE_NAME}` ({SOURCE_HOME})\n")
        f.write(f"- License basis: `{LICENSE_LABEL}`\n")
        f.write(f"- License proof URL: {SOURCE_PROOF_URL}\n")
        f.write(f"- Query seeds inspected: `{seeds_total}`\n")
        f.write(f"- Books before (.epub): `{before_count}`\n")
        f.write(f"- Books after (.epub): `{after_count}`\n")
        f.write(f"- Net added: `{after_count - before_count}`\n")
        f.write(f"- Added books: `{len(added)}`\n")
        f.write(f"- Skipped candidates: `{len(skipped)}`\n\n")

        f.write("## Added Books\n\n")
        if not added:
            f.write("- None\n")
        for b in added:
            f.write(
                f"- `{b.title}` | `{b.author}` | "
                f"`{b.path.resolve()}` | {b.source_url}\n"
            )

        f.write("\n## Skipped Candidates\n\n")
        if not skipped:
            f.write("- None\n")
        for row in skipped:
            f.write(
                "- "
                f"id={row.get('gutenberg_id', 'unknown')} | "
                f"{row.get('title', '')} | {row.get('author', '')} | "
                f"{row.get('reason', 'unknown')} | "
                f"{row.get('detail', '')}\n"
            )

    latest_payload = {
        "run_stamp": run_stamp,
        "source_name": SOURCE_NAME,
        "source_home": SOURCE_HOME,
        "proof_url": SOURCE_PROOF_URL,
        "license_status": LICENSE_STATUS,
        "license_label": LICENSE_LABEL,
        "before_count": before_count,
        "after_count": after_count,
        "net_added": after_count - before_count,
        "query_seeds": seeds_total,
        "added": len(added),
        "skipped_candidates": len(skipped),
        "manifest_jsonl": str(manifest_jsonl.resolve()),
        "skipped_jsonl": str(skipped_jsonl.resolve()),
        "report_md": str(report_md.resolve()),
    }
    latest_json.write_text(json.dumps(latest_payload, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")

    return manifest_jsonl, skipped_jsonl, report_md, latest_json


def run(args: argparse.Namespace) -> int:
    today = dt.date.today().isoformat()
    run_stamp = dt.datetime.now().strftime("%Y%m%d_%H%M%S")

    if not args.recommendations_txt.exists():
        raise SystemExit(f"Recommendations file not found: {args.recommendations_txt}")
    args.temp_dir.mkdir(parents=True, exist_ok=True)

    existing_keys, existing_hashes, before_count = build_existing_index(args.library_root)
    seeds = load_query_seeds(args.recommendations_txt)
    if args.max_seeds > 0:
        seeds = seeds[: args.max_seeds]

    added: list[AddedBook] = []
    skipped: list[dict] = []
    seen_ids: set[int] = set()

    def skip(row: dict, reason: str, detail: str = "", query: str = "", query_source: str = "") -> None:
        skipped.append(
            {
                "gutenberg_id": int(row.get("id", 0)) if row.get("id") else 0,
                "title": clean_ws(str(row.get("title", ""))),
                "author": clean_ws(
                    str(((row.get("authors") or [{}])[0] or {}).get("name", ""))
                ),
                "reason": reason,
                "detail": detail,
                "query": query,
                "query_source": query_source,
            }
        )

    for idx, seed in enumerate(seeds, start=1):
        if len(added) >= args.target:
            break
        print(f"[QUERY {idx}/{len(seeds)}] {seed.query}", flush=True)
        try:
            rows = gutendex_search(seed.query)
        except Exception as exc:  # noqa: BLE001
            skipped.append(
                {
                    "gutenberg_id": 0,
                    "title": seed.title,
                    "author": seed.author,
                    "reason": "search_failed",
                    "detail": str(exc),
                    "query": seed.query,
                    "query_source": seed.source,
                }
            )
            continue

        if not rows:
            continue

        inspected = 0
        for row in rows:
            if len(added) >= args.target:
                break
            if inspected >= args.max_results_per_query:
                break
            inspected += 1

            book_id = int(row.get("id", 0))
            if not book_id or book_id in seen_ids:
                continue
            seen_ids.add(book_id)

            api_title = clean_ws(str(row.get("title", "")))
            authors = row.get("authors") or []
            api_author = clean_ws(str(authors[0].get("name", ""))) if authors else ""
            languages = [str(v).lower() for v in (row.get("languages") or [])]
            formats = row.get("formats") or {}
            epub_url = pick_epub_url(formats)
            source_page_url = f"https://www.gutenberg.org/ebooks/{book_id}"

            if not api_title or not api_author:
                skip(row, "missing_catalog_metadata", query=seed.query, query_source=seed.source)
                continue
            if "en" not in languages:
                skip(row, "non_english", query=seed.query, query_source=seed.source)
                continue
            if not epub_url:
                skip(row, "missing_epub_url", query=seed.query, query_source=seed.source)
                continue

            key_pre = normalize_key(api_title, api_author)
            if key_pre in existing_keys:
                skip(row, "duplicate_title_author_precheck", query=seed.query, query_source=seed.source)
                continue

            temp_path = args.temp_dir / f"similar_{book_id}.epub"
            if temp_path.exists():
                temp_path.unlink()

            try:
                download_file(epub_url, temp_path, retries=2, pause_seconds=0.5)
            except Exception as exc:  # noqa: BLE001
                skip(row, "download_failed", str(exc), query=seed.query, query_source=seed.source)
                continue

            if not is_zip_file(temp_path):
                temp_path.unlink(missing_ok=True)
                skip(row, "not_zip_epub", query=seed.query, query_source=seed.source)
                continue
            if not zip_integrity_ok(temp_path):
                temp_path.unlink(missing_ok=True)
                skip(row, "zip_integrity_failed", query=seed.query, query_source=seed.source)
                continue

            meta_title, meta_author = extract_epub_metadata(temp_path)
            if not meta_title or not meta_author:
                temp_path.unlink(missing_ok=True)
                skip(row, "metadata_missing_title_or_author", query=seed.query, query_source=seed.source)
                continue

            key_post = normalize_key(meta_title, meta_author)
            if key_post in existing_keys:
                temp_path.unlink(missing_ok=True)
                skip(row, "duplicate_title_author_postcheck", query=seed.query, query_source=seed.source)
                continue

            try:
                digest = sha256_file(temp_path)
            except Exception as exc:  # noqa: BLE001
                temp_path.unlink(missing_ok=True)
                skip(row, "sha256_failed", str(exc), query=seed.query, query_source=seed.source)
                continue

            if digest in existing_hashes:
                temp_path.unlink(missing_ok=True)
                skip(row, "duplicate_hash", query=seed.query, query_source=seed.source)
                continue

            author_display = format_author(meta_author)
            title_display = clean_ws(meta_title)
            dest = compose_destination(args.library_root, title_display, author_display)
            if dest.exists():
                try:
                    if sha256_file(dest) == digest:
                        temp_path.unlink(missing_ok=True)
                        skip(
                            row,
                            "duplicate_hash_destination_exists",
                            query=seed.query,
                            query_source=seed.source,
                        )
                        continue
                except Exception:  # noqa: BLE001
                    pass

            shutil.move(str(temp_path), str(dest))

            added_book = AddedBook(
                gutenberg_id=book_id,
                title=title_display,
                author=author_display,
                path=dest,
                source_url=epub_url,
                source_page_url=source_page_url,
                proof_url=SOURCE_PROOF_URL,
                source_home=SOURCE_HOME,
                source_name=SOURCE_NAME,
                license_status=LICENSE_STATUS,
                license_label=LICENSE_LABEL,
                acquired_on=today,
                query=seed.query,
                query_source=seed.source,
                sha256=digest,
            )
            added.append(added_book)
            existing_keys.add(key_post)
            existing_hashes.add(digest)
            print(f"[ADD {len(added):02d}/{args.target}] {title_display} - {author_display}")

    after_count = len(
        [p for p in args.library_root.rglob("*.epub") if p.is_file() and not p.name.startswith("._")]
    )
    manifest_jsonl, skipped_jsonl, report_md, latest_json = write_reports(
        reports_dir=args.reports_dir,
        run_stamp=run_stamp,
        before_count=before_count,
        after_count=after_count,
        seeds_total=len(seeds),
        added=added,
        skipped=skipped,
    )

    summary = {
        "before_count": before_count,
        "after_count": after_count,
        "net_added": after_count - before_count,
        "target": args.target,
        "added": len(added),
        "skipped_candidates": len(skipped),
        "manifest_jsonl": str(manifest_jsonl.resolve()),
        "skipped_jsonl": str(skipped_jsonl.resolve()),
        "report_md": str(report_md.resolve()),
        "latest_json": str(latest_json.resolve()),
    }
    print(json.dumps(summary, indent=2, ensure_ascii=False))
    return 0


def main() -> int:
    args = parse_args()
    return run(args)


if __name__ == "__main__":
    raise SystemExit(main())
