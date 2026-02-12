#!/usr/bin/env python3
"""Scan a local ebook folder and produce summary + token-card outputs."""

from __future__ import annotations

import argparse
import hashlib
import json
import re
from collections import Counter, defaultdict
from dataclasses import dataclass
from datetime import datetime, timezone
from pathlib import Path

DEFAULT_FORMATS = ".epub,.pdf,.mobi,.azw,.azw3,.txt"
WRITE_ROOT = Path("/Volumes/X4-SD").resolve()
CANDIDATE_ROOTS = [
    "/Volumes/X4-SD/Books",
    "/Volumes/X4-SD/Author Letter Bucket Collection",
]


def default_root() -> str:
    for candidate in CANDIDATE_ROOTS:
        if Path(candidate).exists():
            return candidate
    return CANDIDATE_ROOTS[0]


def ensure_inside_write_root(path: Path, label: str) -> Path:
    resolved = path.expanduser().resolve()
    try:
        resolved.relative_to(WRITE_ROOT)
    except ValueError as exc:
        raise SystemExit(f"{label} must be inside {WRITE_ROOT}: {resolved}") from exc
    return resolved


@dataclass(frozen=True)
class FileRecord:
    file_id: str
    rel_path: str
    title: str
    author: str
    fmt: str
    size_bytes: int
    modified_utc: str
    work_key: str


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--root",
        default=default_root(),
        help="Root folder to scan",
    )
    parser.add_argument(
        "--formats",
        default=DEFAULT_FORMATS,
        help="Comma-separated extensions to include (ex: .epub,.pdf)",
    )
    parser.add_argument(
        "--out-json",
        default="reports/collection-summary.json",
        help="Summary JSON output path",
    )
    parser.add_argument(
        "--out-md",
        default="reports/collection-summary.md",
        help="Summary Markdown output path",
    )
    parser.add_argument(
        "--out-cards",
        default="reports/token-cards.jsonl",
        help="Token-card JSONL output path",
    )
    parser.add_argument(
        "--max-duplicate-rows",
        type=int,
        default=50,
        help="Maximum duplicate groups to report",
    )
    return parser.parse_args()


def normalize_text(value: str) -> str:
    return re.sub(r"[^a-z0-9]+", " ", value.lower()).strip()


def parse_title_author(stem: str) -> tuple[str, str]:
    if " - " in stem:
        title, author = stem.rsplit(" - ", 1)
        title = title.strip()
        author = author.strip()
        if title and author:
            return title, author
    return stem.strip(), ""


def work_key_for(title: str, author: str) -> str:
    return f"{normalize_text(title)}|{normalize_text(author)}".strip("|")


def iso_utc(ts: float) -> str:
    return datetime.fromtimestamp(ts, tz=timezone.utc).replace(microsecond=0).isoformat()


def token_card(record: FileRecord) -> dict[str, str]:
    return {
        "id": record.file_id,
        "t": record.title,
        "a": record.author,
        "fmt": record.fmt,
        "lic": "unknown",
        "src": "local",
        "p": record.rel_path,
        "k": record.work_key,
    }


def scan_collection(root: Path, extensions: set[str]) -> list[FileRecord]:
    records: list[FileRecord] = []
    for path in root.rglob("*"):
        if not path.is_file():
            continue
        if path.is_symlink():
            continue
        if path.name.startswith("._"):
            continue
        ext = path.suffix.lower()
        if ext not in extensions:
            continue

        title, author = parse_title_author(path.stem)
        rel_path = str(path.relative_to(root))
        digest = hashlib.sha1(rel_path.encode("utf-8")).hexdigest()[:12]
        stat = path.stat()
        record = FileRecord(
            file_id=f"loc:{digest}",
            rel_path=rel_path,
            title=title,
            author=author,
            fmt=ext,
            size_bytes=stat.st_size,
            modified_utc=iso_utc(stat.st_mtime),
            work_key=work_key_for(title, author),
        )
        records.append(record)
    return records


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2, ensure_ascii=True) + "\n", encoding="utf-8")


def write_cards(path: Path, cards: list[dict[str, str]]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", encoding="utf-8") as handle:
        for card in cards:
            handle.write(json.dumps(card, ensure_ascii=True) + "\n")


def write_markdown(path: Path, summary: dict) -> None:
    lines: list[str] = []
    lines.append("# Collection Review")
    lines.append("")
    lines.append(f"- Scan root: `{summary['scan_root']}`")
    lines.append(f"- Scanned at: `{summary['scanned_at']}`")
    lines.append(f"- Total files: `{summary['total_files']}`")
    lines.append("")
    lines.append("## Format Counts")
    lines.append("")
    for fmt, count in sorted(summary["by_format"].items(), key=lambda item: item[0]):
        lines.append(f"- `{fmt}`: `{count}`")

    lines.append("")
    lines.append("## Top Duplicate Work Keys")
    lines.append("")
    dupes = summary["duplicate_work_keys"]
    if not dupes:
        lines.append("- None")
    else:
        for row in dupes:
            lines.append(f"- `{row['work_key']}`: `{row['count']}`")

    lines.append("")
    lines.append("## Sample Files")
    lines.append("")
    for row in summary["sample_files"]:
        lines.append(f"- `{row['rel_path']}`")

    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text("\n".join(lines) + "\n", encoding="utf-8")


def build_summary(root: Path, records: list[FileRecord], max_duplicate_rows: int) -> dict:
    by_format = Counter(record.fmt for record in records)
    by_work_key = defaultdict(int)
    for record in records:
        if record.work_key:
            by_work_key[record.work_key] += 1

    duplicate_rows = [
        {"work_key": key, "count": count}
        for key, count in by_work_key.items()
        if count > 1
    ]
    duplicate_rows.sort(key=lambda row: (-row["count"], row["work_key"]))

    sample_files = [
        {
            "rel_path": record.rel_path,
            "title": record.title,
            "author": record.author,
            "fmt": record.fmt,
        }
        for record in sorted(records, key=lambda row: row.rel_path)[:50]
    ]

    return {
        "scan_root": str(root),
        "scanned_at": datetime.now(tz=timezone.utc).replace(microsecond=0).isoformat(),
        "total_files": len(records),
        "by_format": dict(by_format),
        "duplicate_work_keys": duplicate_rows[:max_duplicate_rows],
        "sample_files": sample_files,
    }


def main() -> int:
    args = parse_args()

    root = ensure_inside_write_root(Path(args.root), "root")
    if not root.exists() or not root.is_dir():
        raise SystemExit(f"Root directory not found: {root}")

    extensions = {
        ext.strip().lower() if ext.strip().startswith(".") else f".{ext.strip().lower()}"
        for ext in args.formats.split(",")
        if ext.strip()
    }
    if not extensions:
        raise SystemExit("No valid extensions provided.")

    records = scan_collection(root, extensions)
    cards = [token_card(record) for record in records]
    summary = build_summary(root, records, args.max_duplicate_rows)

    out_json = ensure_inside_write_root(Path(args.out_json), "out-json")
    out_md = ensure_inside_write_root(Path(args.out_md), "out-md")
    out_cards = ensure_inside_write_root(Path(args.out_cards), "out-cards")

    write_json(out_json, summary)
    write_cards(out_cards, cards)
    write_markdown(out_md, summary)

    print(f"Scanned {len(records)} files under {root}")
    print(f"Wrote summary JSON: {out_json}")
    print(f"Wrote summary Markdown: {out_md}")
    print(f"Wrote token cards: {out_cards}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
