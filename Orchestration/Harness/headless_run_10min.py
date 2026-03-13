#!/usr/bin/env python3
"""
Headless runner: run a small model for N minutes with START_HERE.md + PRD as context.
Runs acceptance test first (local model installed, running, working). At end, asks the
agent to summarize their work and appends the summary to the session log.

Usage:
  python headless_run_10min.py [--minutes 1|3|5|10] [--prd path/to/PRD.md] [--skip-acceptance]
  HEADLESS_BASE_URL=http://localhost:8080 HEADLESS_MODEL=default python headless_run_10min.py

Requires: requests (pip install requests)
"""

from __future__ import annotations

import argparse
import json
import os
import subprocess
import sys
import time
from pathlib import Path

try:
    import requests
except ImportError:
    print("pip install requests", file=sys.stderr)
    sys.exit(1)

# Paths: script lives at Orchestration/Harness/headless_run_10min.py
HARNESS_DIR = Path(__file__).resolve().parent
REPO_ROOT = HARNESS_DIR.parent.parent.parent  # Harness -> Orchestration -> Project -> repo root
START_HERE_PATH = REPO_ROOT / "START_HERE.md"
DEFAULT_PRD_PATH = REPO_ROOT / "Project" / "Product Documents" / "PRDs" / "CODE_REVIEW_HARNESS_AND_API_PRD.md"
SESSION_LOG_DIR = HARNESS_DIR / "headless_sessions"


def load_text(path: Path, default: str = "") -> str:
    if not path.is_file():
        return default
    return path.read_text(encoding="utf-8", errors="replace").strip()


def build_system_prompt(start_here: str, prd_content: str) -> str:
    return (
        "You are an AI agent in the dotAi system running headlessly. Follow the instructions in START_HERE and the PRD.\n\n"
        "--- START_HERE.md ---\n"
        f"{start_here[:24_000]}\n\n"
        "--- PRD (excerpt) ---\n"
        f"{prd_content[:16_000]}\n\n"
        "Work on the PRD tasks. For each response: state which task you are addressing, then give a concrete next step (code snippet, file path, or clear action). Keep responses focused and under 1500 words when possible."
    ).strip()


def chat_openai_compatible(
    base_url: str,
    model: str,
    messages: list[dict],
    timeout: int = 120,
) -> str:
    url = f"{base_url.rstrip('/')}/v1/chat/completions"
    payload = {
        "model": model,
        "messages": messages,
        "max_tokens": 2048,
        "temperature": 0.3,
    }
    r = requests.post(url, json=payload, timeout=timeout)
    r.raise_for_status()
    data = r.json()
    choice = data.get("choices", [{}])[0]
    return (choice.get("message") or {}).get("content", "").strip()


def run_acceptance_test(base_url: str, model: str, skip: bool) -> None:
    """Run acceptance_test_local_model.py; exit if not skip and test fails."""
    if skip:
        return
    env = os.environ.copy()
    env["HEADLESS_BASE_URL"] = base_url
    env["HEADLESS_MODEL"] = model
    script = HARNESS_DIR / "acceptance_test_local_model.py"
    r = subprocess.run(
        [sys.executable, str(script), "--base-url", base_url, "--model", model],
        env=env,
        capture_output=True,
        text=True,
        cwd=str(HARNESS_DIR),
    )
    if r.returncode != 0:
        print(r.stderr or "Acceptance test failed.", file=sys.stderr)
        sys.exit(r.returncode)


def run_headless(
    minutes: float = 10.0,
    prd_path: Path | None = None,
    base_url: str | None = None,
    model: str | None = None,
    skip_acceptance: bool = False,
) -> Path:
    base_url = base_url or os.environ.get("HEADLESS_BASE_URL", "http://localhost:8080")
    model = model or os.environ.get("HEADLESS_MODEL", "default")
    prd_path = prd_path or DEFAULT_PRD_PATH

    run_acceptance_test(base_url, model, skip_acceptance)

    start_here = load_text(START_HERE_PATH, "(START_HERE.md not found)")
    prd_content = load_text(Path(prd_path), "(PRD not found)")

    system_prompt = build_system_prompt(start_here, prd_content)
    messages: list[dict] = [
        {"role": "system", "content": system_prompt},
        {"role": "user", "content": "Start working through the PRD tasks. Pick the first pending task and describe exactly what you would do (file, change, or step). Be concrete."},
    ]

    SESSION_LOG_DIR.mkdir(parents=True, exist_ok=True)
    timestamp = time.strftime("%Y%m%d-%H%M%S")
    log_path = SESSION_LOG_DIR / f"headless_session_{timestamp}.jsonl"
    summary_path = SESSION_LOG_DIR / f"headless_session_{timestamp}_summary.md"

    end_time = time.monotonic() + minutes * 60
    turn = 0

    with open(log_path, "w", encoding="utf-8") as f:
        while time.monotonic() < end_time:
            turn += 1
            try:
                content = chat_openai_compatible(base_url, model, messages)
            except Exception as e:
                rec = {"turn": turn, "error": str(e), "ts": time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime())}
                f.write(json.dumps(rec) + "\n")
                f.flush()
                time.sleep(5)
                continue

            messages.append({"role": "assistant", "content": content})
            rec = {"turn": turn, "role": "assistant", "content": content[:8000], "ts": time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime())}
            f.write(json.dumps(rec, ensure_ascii=False) + "\n")
            f.flush()

            remaining = end_time - time.monotonic()
            if remaining < 60:
                break

            messages.append({
                "role": "user",
                "content": "Continue with the next PRD task or the next step for the current task. Be concrete (file path, code, or action).",
            })
            time.sleep(0.5)

        # Final step: ask the agent to summarize their work
        summary_prompt = (
            "Session is ending. In a short, structured summary (bullets or numbered list), "
            "summarize the work you did this session: which PRD tasks you addressed, what you proposed or did, "
            "and what remains to be done. Keep it under 500 words."
        )
        messages.append({"role": "user", "content": summary_prompt})
        try:
            summary_content = chat_openai_compatible(base_url, model, messages)
        except Exception as e:
            summary_content = f"(Summary failed: {e})"
        rec = {"turn": turn + 1, "role": "assistant", "summary": True, "content": summary_content, "ts": time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime())}
        f.write(json.dumps(rec, ensure_ascii=False) + "\n")
        f.flush()

    # Write standalone summary file for easy review
    with open(summary_path, "w", encoding="utf-8") as out:
        out.write(f"# Headless session summary — {timestamp}\n\n")
        out.write(f"Duration: {minutes} min. Log: `{log_path.name}`\n\n")
        out.write("## Agent summary\n\n")
        out.write(summary_content)
        out.write("\n")
    print(f"Session log: {log_path}")
    print(f"Summary: {summary_path}")
    return log_path


def main() -> None:
    p = argparse.ArgumentParser(description="Headless run: START_HERE + PRD → model (1, 3, 5, or 10 min). Acceptance test runs first unless --skip-acceptance.")
    p.add_argument("--minutes", type=float, default=10, help="Run for this many minutes (e.g. 1, 3, 5, 10)")
    p.add_argument("--prd", type=Path, default=None, help="Path to PRD markdown")
    p.add_argument("--base-url", default=None, help="OpenAI-compatible API base URL")
    p.add_argument("--model", default=None, help="Model name")
    p.add_argument("--skip-acceptance", action="store_true", help="Skip acceptance test (use only if already verified)")
    args = p.parse_args()

    run_headless(
        minutes=args.minutes,
        prd_path=args.prd,
        base_url=args.base_url,
        model=args.model,
        skip_acceptance=args.skip_acceptance,
    )


if __name__ == "__main__":
    main()
