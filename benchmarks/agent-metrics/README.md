# Agent task-success metrics

<!-- AI: Contains subprompts. Scan for task-specific instructions. Preferences: see PREFERENCES block if present. -->
<!-- PREFERENCES (edit for your project): -->
<!-- (none) -->

Methods to collect **agent outcome metrics** aligned with [.ai/memories/MENTAL_MAP.md](../../.ai/memories/MENTAL_MAP.md): compilation rate, lint pass rate, test pass rate, context efficiency. Results are stored under `benchmarks/results/` and can be used to populate MENTAL_MAP or downstream dashboards.

## Prerequisites

- Python 3.6+

## Record one run

From repo root:

```bash
# Minimal: task type only (for counting runs)
python3 benchmarks/agent-metrics/record_agent_metrics.py fix_bug

# With outcome flags
python3 benchmarks/agent-metrics/record_agent_metrics.py fix_bug --compilation-ok --lint-ok --test-ok --model "llama-server default"

# With context efficiency (0.0–1.0)
python3 benchmarks/agent-metrics/record_agent_metrics.py add_feature --test-ok --context-efficiency 0.72 --notes "first attempt"
```

Each call appends one JSON line to `benchmarks/results/agent_metrics.jsonl`. Fields: `timestamp`, `task_type`, `compilation_ok`, `lint_ok`, `test_ok`, `context_efficiency`, `model`, `notes`.

## Where results are stored

- **File:** `benchmarks/results/agent_metrics.jsonl` (JSONL: one JSON object per line).
- Aggregate rates (e.g. compilation_rate, lint_pass_rate) can be computed from this file and written into MENTAL_MAP or a separate summary; see main benchmarks README for methodology.

## Integration with MENTAL_MAP

MENTAL_MAP tracks compilation_rate, lint_pass_rate, test_pass_rate, context_efficiency and a "Model Performance by Task Type" table. Use this recorder from agent workflows (e.g. after each edit or at end of session) so that aggregated results can later be used to update MENTAL_MAP or SYSTEM model recommendations.
