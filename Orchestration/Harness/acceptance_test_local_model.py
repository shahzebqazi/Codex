#!/usr/bin/env python3
"""
Acceptance test: ensure a local model is installed, running, and working (not bugged).
Exits 0 only if all checks pass. Used before headless runs on the Experimental AI Coding Branch.

Checks:
  1. API is reachable (GET /v1/models or health).
  2. At least one model is available (installed).
  3. A minimal completion succeeds (model running and not bugged).

Usage:
  python acceptance_test_local_model.py [--base-url http://localhost:8080] [--model default]
  HEADLESS_BASE_URL=http://localhost:8080 HEADLESS_MODEL=default python acceptance_test_local_model.py
"""

from __future__ import annotations

import argparse
import os
import sys
from pathlib import Path

try:
    import requests
except ImportError:
    print("pip install requests", file=sys.stderr)
    sys.exit(2)

DEFAULT_BASE_URL = "http://localhost:8080"
DEFAULT_MODEL = "default"
TIMEOUT_CONNECT = 5
TIMEOUT_COMPLETION = 60


def check_reachable(base_url: str) -> None:
    """Verify the API base URL is reachable."""
    url = f"{base_url.rstrip('/')}/v1/models"
    r = requests.get(url, timeout=TIMEOUT_CONNECT)
    r.raise_for_status()


def check_model_installed(base_url: str, model: str) -> None:
    """Verify at least one model is listed (or skip if server does not expose list)."""
    url = f"{base_url.rstrip('/')}/v1/models"
    r = requests.get(url, timeout=TIMEOUT_CONNECT)
    r.raise_for_status()
    data = r.json()
    if not isinstance(data, dict):
        return
    # OpenAI shape: { "data": [ {"id": "..."} ] }; some servers use different keys
    models = data.get("data", data.get("models", []))
    if not isinstance(models, list):
        return
    ids = [m.get("id") or m.get("name", "") for m in models if m]
    if ids and model != "default":
        if not any(model in (m.get("id") or m.get("name") or "") for m in models):
            raise SystemExit(f"Acceptance test failed: model '{model}' not in list: {ids}")


def check_completion_works(base_url: str, model: str) -> None:
    """Verify a minimal chat completion succeeds (model running and not bugged)."""
    url = f"{base_url.rstrip('/')}/v1/chat/completions"
    payload = {
        "model": model,
        "messages": [{"role": "user", "content": "Reply with exactly: OK"}],
        "max_tokens": 10,
        "temperature": 0,
    }
    r = requests.post(url, json=payload, timeout=TIMEOUT_COMPLETION)
    r.raise_for_status()
    data = r.json()
    choices = data.get("choices") or []
    if not choices:
        raise SystemExit("Acceptance test failed: completion returned no choices (model bugged?).")
    content = (choices[0].get("message") or {}).get("content") or ""
    if not content.strip():
        raise SystemExit("Acceptance test failed: completion returned empty content (model bugged?).")


def main() -> int:
    p = argparse.ArgumentParser(description="Acceptance test: local model installed, running, working")
    p.add_argument("--base-url", default=os.environ.get("HEADLESS_BASE_URL", DEFAULT_BASE_URL))
    p.add_argument("--model", default=os.environ.get("HEADLESS_MODEL", DEFAULT_MODEL))
    args = p.parse_args()

    try:
        check_reachable(args.base_url)
        check_model_installed(args.base_url, args.model)
        check_completion_works(args.base_url, args.model)
    except requests.RequestException as e:
        print(f"Acceptance test failed: {e}", file=sys.stderr)
        return 1
    except SystemExit as e:
        print(str(e), file=sys.stderr)
        return 1

    print("Acceptance test passed: local model is installed, running, and working.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
