# Benchmarks — Methods to benchmark AI

<!-- AI: Contains subprompts. Scan for task-specific instructions. Preferences: see PREFERENCES block if present. -->
<!-- PREFERENCES (edit for your project): -->
<!-- (none) -->

This directory lives on the **benchmarks** branch. It provides reproducible methods to benchmark (1) local model inference and (2) agent task-success metrics. Results complement [.ai/memories/MENTAL_MAP.md](.ai/memories/MENTAL_MAP.md) and [.ai/project/SYSTEM.md](.ai/project/SYSTEM.md) without replacing them.

## Methodology

### Model inference

- **What we measure:** Latency (wall-clock time per completion), completion tokens, and tokens/s for chat completions against an OpenAI-compatible endpoint (e.g. llama-server).
- **Why:** Informs model and quantization choice (see [.ai/skills/INFRA/LLAMA_CPP.md](.ai/skills/INFRA/LLAMA_CPP.md) for size/quality/latency tradeoffs) and SYSTEM model routing (e.g. “simple edits → smallest model”).
- **Tool:** [model-inference/bench_llama_server.py](model-inference/bench_llama_server.py). Output: JSON with latency and tokens/s (per run and average).

### Agent task-success metrics

- **What we measure:** Per-task outcomes: compilation_ok, lint_ok, test_ok, and optional context_efficiency (useful tokens / total tokens). Aligns with MENTAL_MAP’s “Dense Reward History” and “Model Performance by Task Type”.
- **Why:** Enables compilation_rate, lint_pass_rate, test_pass_rate and model-by-task-type tables; supports adaptive compute allocation and pivot thresholds (see MENTAL_MAP).
- **Tool:** [agent-metrics/record_agent_metrics.py](agent-metrics/record_agent_metrics.py). Output: JSONL append to `benchmarks/results/agent_metrics.jsonl`.

### How results relate to MENTAL_MAP and SYSTEM

- **MENTAL_MAP:** Use aggregated agent_metrics (e.g. rates by task_type and model) to update “Model Performance by Task Type” and dense reward history. Optionally run a small script to compute rates from `agent_metrics.jsonl` and suggest MENTAL_MAP edits.
- **SYSTEM:** Use model-inference benchmarks to compare quantization and models; update “Model Routing” and “Default Model” based on latency and quality needs.

## Runbooks

### Run model-inference benchmark

1. Start llama-server (e.g. `docker-compose up llama-server` from repo root or run llama-server manually on port 8080).
2. From repo root:  
   `python3 benchmarks/model-inference/bench_llama_server.py`
3. Optional: save output to `benchmarks/results/model-inference-<timestamp>.json`.

### Record agent metrics (single run)

1. From repo root:  
   `python3 benchmarks/agent-metrics/record_agent_metrics.py <task_type> [--compilation-ok] [--lint-ok] [--test-ok] [--context-efficiency 0.0-1.0] [--model "name"]`
2. Entries append to `benchmarks/results/agent_metrics.jsonl`.

### Aggregate agent metrics for MENTAL_MAP

1. Read `benchmarks/results/agent_metrics.jsonl` (one JSON object per line).
2. Compute per-task_type and per-model: counts and rates for compilation_ok, lint_ok, test_ok; average context_efficiency.
3. Update MENTAL_MAP “Model Performance by Task Type” and “Dense Reward History” with these aggregates (manually or via a small script).

## Results location

| Artifact | Path |
|----------|------|
| Model-inference JSON | `benchmarks/results/model-inference-*.json` (optional; create when saving runs) |
| Agent metrics log | `benchmarks/results/agent_metrics.jsonl` |

The `benchmarks/results/` directory is the single place for benchmark outputs on this branch.

## Coding-task evals (future)

Coding-task evals (e.g. [SWE-bench](https://github.com/swe-bench/SWE-bench)) are out of scope for the first iteration. Scope and stub for future work: [evals/README.md](evals/README.md).

## Index of tools

| Tool | Purpose |
|------|---------|
| [model-inference/bench_llama_server.py](model-inference/bench_llama_server.py) | Measure latency and tokens/s for llama-server chat completion |
| [model-inference/README.md](model-inference/README.md) | How to run model-inference benchmark |
| [agent-metrics/record_agent_metrics.py](agent-metrics/record_agent_metrics.py) | Append one agent task-outcome record (JSONL) |
| [agent-metrics/README.md](agent-metrics/README.md) | How to record and where results are stored |
