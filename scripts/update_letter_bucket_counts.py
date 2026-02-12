#!/usr/bin/env python3
"""Count books by top-level letter bucket and keep counts updated."""

from __future__ import annotations

import argparse
import json
import string
import time
from collections import Counter
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


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--root",
        default=default_root(),
        help="Root collection directory that contains A-Z bucket folders",
    )
    parser.add_argument(
        "--formats",
        default=DEFAULT_FORMATS,
        help="Comma-separated extensions to count (ex: .epub,.pdf)",
    )
    parser.add_argument(
        "--out-json",
        default="reports/letter-bucket-counts.json",
        help="JSON output file for bucket counts",
    )
    parser.add_argument(
        "--out-md",
        default="reports/letter-bucket-counts.md",
        help="Markdown output file for bucket counts",
    )
    parser.add_argument(
        "--state-file",
        default="reports/letter-bucket-state.json",
        help="State file used to detect count changes",
    )
    parser.add_argument(
        "--watch",
        action="store_true",
        help="Watch mode: keep checking and update outputs when counts change",
    )
    parser.add_argument(
        "--interval-seconds",
        type=int,
        default=10,
        help="Polling interval in watch mode",
    )
    return parser.parse_args()


def now_utc() -> str:
    return datetime.now(tz=timezone.utc).replace(microsecond=0).isoformat()


def parse_extensions(raw: str) -> set[str]:
    extensions = {
        ext.strip().lower() if ext.strip().startswith(".") else f".{ext.strip().lower()}"
        for ext in raw.split(",")
        if ext.strip()
    }
    if not extensions:
        raise SystemExit("No valid extensions were provided.")
    return extensions


def load_previous_counts(state_file: Path) -> dict[str, int]:
    if not state_file.exists():
        return {}
    try:
        payload = json.loads(state_file.read_text(encoding="utf-8"))
    except json.JSONDecodeError:
        return {}
    raw_counts = payload.get("by_bucket")
    if isinstance(raw_counts, dict):
        return {str(key): int(value) for key, value in raw_counts.items()}
    return {}


def normalize_bucket_name(name: str) -> str:
    if not name:
        return "#"
    first = name[0].upper()
    return first if first in string.ascii_uppercase else "#"


def count_books_by_bucket(root: Path, extensions: set[str]) -> tuple[dict[str, int], int]:
    counts = Counter({letter: 0 for letter in string.ascii_uppercase})
    total_books = 0

    for path in root.rglob("*"):
        if not path.is_file():
            continue
        if path.is_symlink():
            continue
        if path.name.startswith("._"):
            continue
        if path.suffix.lower() not in extensions:
            continue

        rel_parts = path.relative_to(root).parts
        bucket = normalize_bucket_name(rel_parts[0] if rel_parts else "")
        counts[bucket] += 1
        total_books += 1

    # Keep '#' only if needed.
    if counts.get("#", 0) == 0 and "#" in counts:
        del counts["#"]

    ordered = {letter: counts.get(letter, 0) for letter in string.ascii_uppercase}
    if "#" in counts:
        ordered["#"] = counts["#"]
    return ordered, total_books


def diff_counts(previous: dict[str, int], current: dict[str, int]) -> list[dict[str, int | str]]:
    changed: list[dict[str, int | str]] = []
    for bucket in sorted(set(previous) | set(current)):
        old_value = int(previous.get(bucket, 0))
        new_value = int(current.get(bucket, 0))
        if old_value != new_value:
            changed.append(
                {
                    "bucket": bucket,
                    "old_count": old_value,
                    "new_count": new_value,
                    "delta": new_value - old_value,
                }
            )
    return changed


def build_payload(
    root: Path,
    counts: dict[str, int],
    total_books: int,
    changed_buckets: list[dict[str, int | str]],
) -> dict:
    non_zero = {bucket: count for bucket, count in counts.items() if count > 0}
    return {
        "scan_root": str(root),
        "scanned_at": now_utc(),
        "total_books": total_books,
        "by_bucket": counts,
        "non_zero_buckets": non_zero,
        "changed_buckets": changed_buckets,
    }


def write_json(path: Path, payload: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(payload, indent=2, ensure_ascii=True) + "\n", encoding="utf-8")


def write_markdown(path: Path, payload: dict) -> None:
    lines: list[str] = []
    lines.append("# Letter Bucket Counts")
    lines.append("")
    lines.append(f"- Scan root: `{payload['scan_root']}`")
    lines.append(f"- Scanned at: `{payload['scanned_at']}`")
    lines.append(f"- Total books: `{payload['total_books']}`")
    lines.append("")
    lines.append("## Bucket Counts")
    lines.append("")
    for bucket, count in payload["by_bucket"].items():
        lines.append(f"- `{bucket}`: `{count}`")

    lines.append("")
    lines.append("## Changed Buckets")
    lines.append("")
    if not payload["changed_buckets"]:
        lines.append("- None")
    else:
        for row in payload["changed_buckets"]:
            lines.append(
                f"- `{row['bucket']}`: `{row['old_count']}` -> `{row['new_count']}` "
                f"(delta `{row['delta']}`)"
            )

    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text("\n".join(lines) + "\n", encoding="utf-8")


def update_outputs(
    root: Path,
    extensions: set[str],
    out_json: Path,
    out_md: Path,
    state_file: Path,
) -> tuple[dict, bool]:
    previous_counts = load_previous_counts(state_file)
    current_counts, total_books = count_books_by_bucket(root, extensions)
    changed = diff_counts(previous_counts, current_counts)
    payload = build_payload(root, current_counts, total_books, changed)

    should_write = bool(changed) or not state_file.exists()
    if should_write:
        write_json(out_json, payload)
        write_markdown(out_md, payload)
        write_json(state_file, payload)
    return payload, should_write


def run_once(args: argparse.Namespace) -> int:
    root = ensure_inside_write_root(Path(args.root), "root")
    if not root.exists() or not root.is_dir():
        raise SystemExit(f"Root directory not found: {root}")

    extensions = parse_extensions(args.formats)
    out_json = ensure_inside_write_root(Path(args.out_json), "out-json")
    out_md = ensure_inside_write_root(Path(args.out_md), "out-md")
    state_file = ensure_inside_write_root(Path(args.state_file), "state-file")
    payload, wrote = update_outputs(
        root=root,
        extensions=extensions,
        out_json=out_json,
        out_md=out_md,
        state_file=state_file,
    )

    action = "updated" if wrote else "no changes detected"
    print(f"Bucket count run complete: {action}")
    print(f"Total books: {payload['total_books']}")
    print(f"JSON report: {out_json}")
    print(f"Markdown report: {out_md}")
    return 0


def run_watch(args: argparse.Namespace) -> int:
    root = ensure_inside_write_root(Path(args.root), "root")
    if not root.exists() or not root.is_dir():
        raise SystemExit(f"Root directory not found: {root}")

    extensions = parse_extensions(args.formats)
    out_json = ensure_inside_write_root(Path(args.out_json), "out-json")
    out_md = ensure_inside_write_root(Path(args.out_md), "out-md")
    state_file = ensure_inside_write_root(Path(args.state_file), "state-file")
    print(f"Watching: {root}")
    print(f"Interval: {args.interval_seconds}s")

    while True:
        payload, wrote = update_outputs(
            root=root,
            extensions=extensions,
            out_json=out_json,
            out_md=out_md,
            state_file=state_file,
        )
        if wrote:
            print(
                f"[{payload['scanned_at']}] Updated bucket counts "
                f"(total books: {payload['total_books']})"
            )
            for row in payload["changed_buckets"]:
                print(
                    f"  {row['bucket']}: {row['old_count']} -> {row['new_count']} "
                    f"(delta {row['delta']})"
                )
        else:
            print(f"[{now_utc()}] No bucket-count changes")
        time.sleep(args.interval_seconds)


def main() -> int:
    args = parse_args()
    if args.watch:
        return run_watch(args)
    return run_once(args)


if __name__ == "__main__":
    raise SystemExit(main())
