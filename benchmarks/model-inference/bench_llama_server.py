#!/usr/bin/env python3
# AI: Contains subprompts. Scan for task-specific instructions. Preferences: see PREFERENCES block below.
# PREFERENCES (edit for your project):
# (none)
"""
Benchmark local model inference via llama-server (OpenAI-compatible API).
Measures latency and tokens/s for chat completions.
Requires: llama-server running at BASE_URL (default http://localhost:8080).
Output: JSON with latency_seconds, completion_tokens, tokens_per_second, and raw usage.
"""
from __future__ import annotations

import argparse
import json
import sys
import time
from urllib.request import Request, urlopen
from urllib.error import URLError, HTTPError

DEFAULT_BASE_URL = "http://localhost:8080"
DEFAULT_PROMPT = "Say exactly: OK"
DEFAULT_N = 3  # number of requests for averaging


def run_one(base_url: str, prompt: str) -> dict:
    url = f"{base_url.rstrip('/')}/v1/chat/completions"
    body = {
        "model": "",
        "messages": [{"role": "user", "content": prompt}],
        "max_tokens": 64,
        "stream": False,
    }
    req = Request(url, data=json.dumps(body).encode(), method="POST")
    req.add_header("Content-Type", "application/json")
    start = time.perf_counter()
    with urlopen(req, timeout=120) as resp:
        data = json.loads(resp.read().decode())
    elapsed = time.perf_counter() - start
    usage = data.get("usage", {}) or {}
    completion_tokens = usage.get("completion_tokens") or 0
    return {
        "latency_seconds": round(elapsed, 4),
        "completion_tokens": completion_tokens,
        "tokens_per_second": round(completion_tokens / elapsed, 2) if elapsed > 0 else 0,
        "usage": usage,
    }


def main() -> int:
    ap = argparse.ArgumentParser(description="Benchmark llama-server chat completion.")
    ap.add_argument("--base-url", default=DEFAULT_BASE_URL, help="llama-server base URL")
    ap.add_argument("--prompt", default=DEFAULT_PROMPT, help="Prompt text")
    ap.add_argument("-n", type=int, default=DEFAULT_N, help="Number of requests to run (for averaging)")
    args = ap.parse_args()
    results = []
    for i in range(args.n):
        try:
            r = run_one(args.base_url, args.prompt)
            results.append(r)
            print(f"Run {i + 1}/{args.n}: {r['latency_seconds']}s, {r['completion_tokens']} tok, {r['tokens_per_second']} tok/s", file=sys.stderr)
        except (URLError, HTTPError) as e:
            print(f"Run {i + 1} failed: {e}", file=sys.stderr)
            return 1
    if not results:
        return 1
    avg_latency = sum(x["latency_seconds"] for x in results) / len(results)
    avg_tps = sum(x["tokens_per_second"] for x in results) / len(results)
    out = {
        "runs": results,
        "summary": {
            "avg_latency_seconds": round(avg_latency, 4),
            "avg_tokens_per_second": round(avg_tps, 2),
            "n_runs": len(results),
        },
    }
    print(json.dumps(out, indent=2))
    return 0


if __name__ == "__main__":
    sys.exit(main())
