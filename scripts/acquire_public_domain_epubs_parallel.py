#!/usr/bin/env python3
"""
Parallel bulk-acquire public-domain EPUB books from Project Gutenberg (via Gutendex).

This keeps the same safety/validation rules as acquire_public_domain_epubs.py, but
downloads candidates concurrently to reach large targets faster.
"""

from __future__ import annotations

import argparse
import concurrent.futures as cf
import datetime as dt
import json
import os
import shutil
import tempfile
from dataclasses import dataclass
from pathlib import Path
from typing import Optional

from acquire_public_domain_epubs import (
    CATALOG_HOSTS,
    DEFAULT_LIBRARY_ROOT,
    DEFAULT_LIBRARY_TXT,
    DEFAULT_RECOMMENDATIONS_TXT,
    DEFAULT_SOURCES_LOG,
    GUTENBERG_HOSTS,
    GUTENDEX_URL,
    LARGE_TARGET_CONFIRM_TOKEN,
    MAX_SAFE_TARGET,
    AddedBook,
    append_sources_log,
    build_existing_index,
    classify_genre,
    compose_destination,
    contains_prompt_injection_signals,
    download_file,
    extract_epub_metadata,
    fetch_json,
    format_author,
    is_zip_file,
    normalize_key,
    sanitize_untrusted_text,
    sha256_file,
    update_library_txt,
    update_recommendations_txt,
    validate_url,
    write_reports,
    zip_integrity_ok,
)


DEFAULT_REPORTS_DIR = Path("/Volumes/X4-SD/Librarian/reports")
DEFAULT_TEMP_DIR = Path("/Volumes/X4-SD/XTCache/.epub-acquire-parallel-tmp")


@dataclass(frozen=True)
class Candidate:
    gutenberg_id: int
    api_title: str
    api_author: str
    pre_key: str
    epub_url: str
    source_page_url: str
    subjects: list[str]
    bookshelves: list[str]


@dataclass
class WorkerResult:
    status: str
    reason: str
    detail: str
    candidate: Candidate
    temp_path: Optional[Path] = None
    meta_title: str = ""
    meta_author: str = ""
    post_key: str = ""
    sha256: str = ""


def parse_candidate(row: dict) -> tuple[Optional[Candidate], Optional[dict]]:
    book_id = int(row.get("id", 0))
    api_title = sanitize_untrusted_text(row.get("title", ""))
    authors = row.get("authors") or []
    api_author = sanitize_untrusted_text(authors[0].get("name", "")) if authors else ""
    formats = row.get("formats") or {}
    epub_url = formats.get("application/epub+zip")
    source_page_url = f"https://www.gutenberg.org/ebooks/{book_id}" if book_id > 0 else ""

    def skip(reason: str, detail: str = "") -> tuple[None, dict]:
        return None, {
            "gutenberg_id": book_id,
            "title": api_title,
            "author": api_author,
            "source_url": sanitize_untrusted_text(epub_url or ""),
            "reason": reason,
            "detail": sanitize_untrusted_text(detail, max_len=500),
        }

    if not epub_url:
        return skip("missing_epub_url")
    if not api_title or not api_author:
        return skip("missing_catalog_metadata")
    if contains_prompt_injection_signals(api_title) or contains_prompt_injection_signals(api_author):
        return skip("prompt_injection_pattern_in_catalog_metadata")

    try:
        epub_url = validate_url(str(epub_url), GUTENBERG_HOSTS, "EPUB URL")
        source_page_url = validate_url(source_page_url, GUTENBERG_HOSTS, "Source page URL")
    except Exception as exc:  # noqa: BLE001
        return skip("unsafe_source_url", str(exc))

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

    return Candidate(
        gutenberg_id=book_id,
        api_title=api_title,
        api_author=api_author,
        pre_key=normalize_key(api_title, api_author),
        epub_url=epub_url,
        source_page_url=source_page_url,
        subjects=subjects,
        bookshelves=bookshelves,
    ), None


def worker_validate_download(candidate: Candidate, temp_dir: Path) -> WorkerResult:
    tmp_fd, tmp_name = tempfile.mkstemp(
        suffix=f"_{candidate.gutenberg_id}.epub",
        dir=str(temp_dir),
    )
    temp_path = Path(tmp_name)
    try:
        os.close(tmp_fd)
    except Exception:
        pass
    try:
        download_file(candidate.epub_url, temp_path)

        if not is_zip_file(temp_path):
            return WorkerResult(
                status="skip",
                reason="not_zip_epub",
                detail="",
                candidate=candidate,
                temp_path=temp_path,
            )

        zip_ok, zip_reason = zip_integrity_ok(temp_path)
        if not zip_ok:
            return WorkerResult(
                status="skip",
                reason="zip_integrity_failed",
                detail=zip_reason,
                candidate=candidate,
                temp_path=temp_path,
            )

        meta_title, meta_author = extract_epub_metadata(temp_path)
        if not (meta_title and meta_author):
            return WorkerResult(
                status="skip",
                reason="metadata_missing_title_or_author",
                detail="",
                candidate=candidate,
                temp_path=temp_path,
            )
        if contains_prompt_injection_signals(meta_title) or contains_prompt_injection_signals(meta_author):
            return WorkerResult(
                status="skip",
                reason="prompt_injection_pattern_in_epub_metadata",
                detail="",
                candidate=candidate,
                temp_path=temp_path,
            )

        try:
            digest = sha256_file(temp_path)
        except Exception as exc:  # noqa: BLE001
            return WorkerResult(
                status="skip",
                reason="sha256_failed",
                detail=str(exc),
                candidate=candidate,
                temp_path=temp_path,
            )

        return WorkerResult(
            status="ok",
            reason="",
            detail="",
            candidate=candidate,
            temp_path=temp_path,
            meta_title=meta_title,
            meta_author=meta_author,
            post_key=normalize_key(meta_title, meta_author),
            sha256=digest,
        )
    except Exception as exc:  # noqa: BLE001
        return WorkerResult(
            status="skip",
            reason="download_or_validation_failed",
            detail=str(exc),
            candidate=candidate,
            temp_path=temp_path,
        )


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
    temp_dir.mkdir(parents=True, exist_ok=True)

    existing_keys, existing_hashes, before_count = build_existing_index(library_root)
    print(f"[INFO] Existing EPUB count in library: {before_count}")
    print(f"[INFO] Existing dedupe keys: {len(existing_keys)}")
    print(f"[INFO] Existing file hashes: {len(existing_hashes)}")
    print(f"[INFO] Target additions: {target}")
    print(f"[INFO] Workers: {args.workers}")

    added: list[AddedBook] = []
    skipped: list[dict] = []
    reserved_pre_keys: set[str] = set()

    def record_skip(candidate: Candidate, reason: str, detail: str = "") -> None:
        skipped.append(
            {
                "gutenberg_id": candidate.gutenberg_id,
                "title": candidate.api_title,
                "author": candidate.api_author,
                "source_url": candidate.epub_url,
                "reason": reason,
                "detail": sanitize_untrusted_text(detail, max_len=500),
            }
        )

    next_url = validate_url(GUTENDEX_URL, CATALOG_HOSTS, "Initial catalog URL")
    pages_seen = 0

    with cf.ThreadPoolExecutor(max_workers=args.workers) as pool:
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

            futures: dict[cf.Future[WorkerResult], Candidate] = {}
            for row in payload.get("results") or []:
                if len(added) >= target:
                    break
                candidate, parse_skip = parse_candidate(row)
                if parse_skip:
                    skipped.append(parse_skip)
                    continue
                assert candidate is not None

                if candidate.pre_key in existing_keys or candidate.pre_key in reserved_pre_keys:
                    record_skip(candidate, "duplicate_title_author_precheck")
                    continue

                reserved_pre_keys.add(candidate.pre_key)
                futures[pool.submit(worker_validate_download, candidate, temp_dir)] = candidate

            for future in cf.as_completed(futures):
                candidate = futures[future]
                reserved_pre_keys.discard(candidate.pre_key)

                try:
                    result = future.result()
                except Exception as exc:  # noqa: BLE001
                    record_skip(candidate, "worker_failed", str(exc))
                    continue

                if result.status != "ok":
                    if result.temp_path is not None:
                        result.temp_path.unlink(missing_ok=True)
                    record_skip(candidate, result.reason, result.detail)
                    continue

                if len(added) >= target:
                    if result.temp_path is not None:
                        result.temp_path.unlink(missing_ok=True)
                    continue

                if result.post_key in existing_keys:
                    if result.temp_path is not None:
                        result.temp_path.unlink(missing_ok=True)
                    record_skip(candidate, "duplicate_title_author_postcheck")
                    continue
                if result.sha256 in existing_hashes:
                    if result.temp_path is not None:
                        result.temp_path.unlink(missing_ok=True)
                    record_skip(candidate, "duplicate_hash")
                    continue

                genre = classify_genre(candidate.subjects, candidate.bookshelves)
                author_display = format_author(result.meta_author)
                title_display = sanitize_untrusted_text(result.meta_title)
                dest = compose_destination(library_root, title_display, author_display)

                if dest.exists():
                    try:
                        if sha256_file(dest) == result.sha256:
                            if result.temp_path is not None:
                                result.temp_path.unlink(missing_ok=True)
                            record_skip(candidate, "duplicate_hash_destination_exists")
                            continue
                    except Exception:
                        pass

                assert result.temp_path is not None
                shutil.move(str(result.temp_path), str(dest))

                book = AddedBook(
                    gutenberg_id=candidate.gutenberg_id,
                    title=title_display,
                    author=author_display,
                    genre=genre,
                    source_url=candidate.epub_url,
                    source_page_url=candidate.source_page_url,
                    path=dest,
                    sha256=result.sha256,
                    subjects=candidate.subjects,
                    bookshelves=candidate.bookshelves,
                    acquired_on=today,
                )
                added.append(book)
                existing_keys.add(result.post_key)
                existing_hashes.add(result.sha256)
                print(f"[ADD {len(added):04d}/{target}] {book.title} - {book.author}")

                if len(added) >= target:
                    break

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
        "manifest_jsonl": str(manifest_jsonl.resolve()),
        "skipped_jsonl": str(skipped_jsonl.resolve()),
        "report_md": str(report_md.resolve()),
    }
    print("[SUCCESS] Acquisition completed.")
    print(json.dumps(summary, indent=2, ensure_ascii=False))
    return 0


def build_arg_parser() -> argparse.ArgumentParser:
    p = argparse.ArgumentParser(description="Parallel acquire new legal EPUBs into the library.")
    p.add_argument("--target", type=int, default=100, help="Exactly how many new EPUBs to add.")
    p.add_argument("--workers", type=int, default=8, help="Download/validation worker count.")
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
    p.add_argument("--temp-dir", type=Path, default=DEFAULT_TEMP_DIR)
    return p


def main() -> int:
    parser = build_arg_parser()
    args = parser.parse_args()
    return run(args)


if __name__ == "__main__":
    raise SystemExit(main())
