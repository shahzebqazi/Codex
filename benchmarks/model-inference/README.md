# Model inference benchmark

<!-- AI: Contains subprompts. Scan for task-specific instructions. Preferences: see PREFERENCES block if present. -->
<!-- PREFERENCES (edit for your project): -->
<!-- (none) -->

Measures **latency** and **tokens/s** for chat completions against a local llama-server (OpenAI-compatible API). Aligns with `.ai/skills/INFRA/LLAMA_CPP.md` and `.ai/project/SYSTEM.md` model recommendations.

## Prerequisites

- llama-server running (e.g. `http://localhost:8080`). See Codex root `docker-compose.yml` or LLAMA_CPP.md for how to start it.
- Python 3.6+ (stdlib only; no extra deps).

## Run

```bash
# From repo root
python3 benchmarks/model-inference/bench_llama_server.py

# Custom base URL and number of runs
python3 benchmarks/model-inference/bench_llama_server.py --base-url http://localhost:8080 -n 5
```

Output: JSON to stdout (summary + per-run latency, completion_tokens, tokens_per_second). Progress to stderr.

## Results location

Pipe to a file or append to a results log, e.g.:

```bash
python3 benchmarks/model-inference/bench_llama_server.py > benchmarks/results/model-inference-$(date +%Y%m%d-%H%M%S).json 2>&1
```

Keep results under `benchmarks/results/` (see main benchmarks README).
