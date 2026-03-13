# Headless runner (1, 3, 5, or 10 minutes)

Used on the **Experimental AI Coding Branch** to run a small model with `START_HERE.md` and a PRD as context.

## Acceptance test first

Before any headless run, an **acceptance test** runs (unless `--skip-acceptance` is used). It ensures:

1. **Local model is installed** — API is reachable and lists at least one model.
2. **Model is running** — Service responds.
3. **Model is working (not bugged)** — A minimal completion succeeds.

If the acceptance test fails, the headless run is not started and the script exits with a non-zero code. Run the test alone:

```bash
cd Orchestration/Harness
python acceptance_test_local_model.py [--base-url http://localhost:8080] [--model default]
```

## Runners by duration

| Script | Duration |
|--------|----------|
| `run_headless_1min.py` | 1 minute |
| `run_headless_3min.py` | 3 minutes |
| `run_headless_5min.py` | 5 minutes |
| `headless_run_10min.py` | 10 minutes (default) or `--minutes N` |

All runners run the acceptance test first, then the headless loop, then ask the agent to **summarize their work** at the end.

## Prerequisites

- OpenAI-compatible API (e.g. llama-server at `http://localhost:8080`).
- `pip install requests`.

## Quick start

```bash
# Ensure a model is served (e.g. docker compose -f Orchestration/orchestrator-compose.yml up -d llama-server).
cd Orchestration/Harness

# 1, 3, 5, or 10 minutes
python run_headless_1min.py
python run_headless_3min.py
python run_headless_5min.py
python headless_run_10min.py --minutes 10
```

## Environment

| Variable | Default | Description |
|----------|---------|-------------|
| `HEADLESS_BASE_URL` | `http://localhost:8080` | Base URL for OpenAI-compatible API |
| `HEADLESS_MODEL` | `default` | Model name (e.g. the GGUF name for llama-server) |

## Options (main script)

- `--minutes N` — Run for N minutes (e.g. 1, 3, 5, 10).
- `--prd PATH` — Path to PRD markdown (default: Code Review Harness PRD).
- `--base-url URL` — Override API base URL.
- `--model NAME` — Override model name.
- `--skip-acceptance` — Skip acceptance test (use only if already verified).

## Output

- **Session log:** `headless_sessions/headless_session_YYYYMMDD-HHMMSS.jsonl` — each line is a JSON object with `turn`, `role`, `content`, `ts`; the last turn has `"summary": true` with the agent’s work summary.
- **Summary file:** `headless_sessions/headless_session_YYYYMMDD-HHMMSS_summary.md` — standalone markdown with the agent’s end-of-session summary for easy review.

## Review after a run

See [EXPERIMENTAL_BRANCH_REVIEW.md](EXPERIMENTAL_BRANCH_REVIEW.md) for how to review the work done on the Experimental AI Coding Branch.
