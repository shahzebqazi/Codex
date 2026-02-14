#!/usr/bin/env python3
# AI: Contains subprompts. Scan for task-specific instructions. Preferences: see PREFERENCES block below.
# PREFERENCES (edit for your project):
# (none)
"""
Record agent task-success metrics for benchmarking. Aligns with .ai/memories/MENTAL_MAP.md
(compilation_rate, lint_pass_rate, test_pass_rate, context_efficiency).
Appends one JSON object per line to benchmarks/results/agent_metrics.jsonl.
"""
from __future__ import annotations

import argparse
import json
import os
import sys
from datetime import datetime, timezone

RESULTS_DIR = os.path.join(os.path.dirname(__file__), "..", "results")
METRICS_FILE = os.path.join(RESULTS_DIR, "agent_metrics.jsonl")


def ensure_results_dir() -> None:
    os.makedirs(RESULTS_DIR, exist_ok=True)


def record(
    task_type: str,
    compilation_ok: bool | None = None,
    lint_ok: bool | None = None,
    test_ok: bool | None = None,
    context_efficiency: float | None = None,
    model: str | None = None,
    notes: str | None = None,
) -> None:
    ensure_results_dir()
    entry = {
        "timestamp": datetime.now(timezone.utc).isoformat().replace("+00:00", "Z"),
        "task_type": task_type,
        "compilation_ok": compilation_ok,
        "lint_ok": lint_ok,
        "test_ok": test_ok,
        "context_efficiency": context_efficiency,
        "model": model,
        "notes": notes,
    }
    with open(METRICS_FILE, "a") as f:
        f.write(json.dumps(entry) + "\n")


def main() -> int:
    ap = argparse.ArgumentParser(description="Record one agent task-success metric entry.")
    ap.add_argument("task_type", help="Task type (e.g. simple_edit, fix_bug, add_feature)")
    ap.add_argument("--compilation-ok", action="store_true", help="Edit compiled on first try")
    ap.add_argument("--no-compilation-ok", action="store_true", help="Edit did not compile on first try")
    ap.add_argument("--lint-ok", action="store_true", help="Lint passed")
    ap.add_argument("--no-lint-ok", action="store_true", help="Lint failed")
    ap.add_argument("--test-ok", action="store_true", help="Tests passed")
    ap.add_argument("--no-test-ok", action="store_true", help="Tests failed")
    ap.add_argument("--context-efficiency", type=float, metavar="0.0-1.0", help="Useful tokens / total tokens")
    ap.add_argument("--model", type=str, help="Model identifier (e.g. llama-server default)")
    ap.add_argument("--notes", type=str, help="Optional free-form notes")
    args = ap.parse_args()
    comp = None
    if args.compilation_ok:
        comp = True
    elif args.no_compilation_ok:
        comp = False
    lint = None
    if args.lint_ok:
        lint = True
    elif args.no_lint_ok:
        lint = False
    test = None
    if args.test_ok:
        test = True
    elif args.no_test_ok:
        test = False
    record(
        task_type=args.task_type,
        compilation_ok=comp,
        lint_ok=lint,
        test_ok=test,
        context_efficiency=args.context_efficiency,
        model=args.model,
        notes=args.notes,
    )
    print(f"Recorded to {METRICS_FILE}", file=sys.stderr)
    return 0


if __name__ == "__main__":
    sys.exit(main())
