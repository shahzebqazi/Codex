---
name: "Benchmarks branch — Methods to benchmark AI"
overview: "This branch adds reproducible methods to benchmark local AI models (inference) and agent behavior (task success). Tooling and docs live on this branch; results complement MENTAL_MAP and SYSTEM without replacing them."
todos:
  - id: branch-create
    content: "Create git branch benchmarks from main (or from the appropriate base)"
    status: pending
  - id: benchmark-model-inference
    content: "Define and implement methods to benchmark local model inference (e.g. latency, tokens/s, throughput) for llama-server/GGUF (and document how to run them)"
    status: pending
  - id: benchmark-agent-metrics
    content: "Define and implement methods to collect agent task-success metrics (e.g. compile/lint/test pass rates, context efficiency) aligned with MENTAL_MAP; document how to run and where results are stored"
    status: pending
  - id: benchmark-docs
    content: "Document benchmark methodology, runbooks, and how results relate to MENTAL_MAP and SYSTEM (model choice)"
    status: pending
  - id: benchmark-evals-optional
    content: "Optional: add scope or stubs for coding-task evals (e.g. SWE-bench) in Scope / future work"
    status: pending
isProject: false
---

# Benchmarks branch — Methods to benchmark AI

## Context

The benchmarks branch exists so dotAi and downstream repos can measure model and agent performance in a reproducible way. It complements [MENTAL_MAP.md](../../memories/MENTAL_MAP.md) (agent performance tracking, model-by-task-type) and [SYSTEM.md](../SYSTEM.md) (model recommendations) without replacing them. Benchmark results can inform model choice and adaptive compute allocation.

## Scope

### In scope

- Local (llama-server) inference benchmarks: latency, throughput, tokens/s.
- Agent outcome metrics: compilation rate, lint pass rate, test pass rate, context efficiency (aligned with MENTAL_MAP).
- Scripts/recipes to run benchmarks and a clear location for results (e.g. `benchmarks/` on this branch).
- Documentation: methodology, runbooks, and how results relate to MENTAL_MAP and SYSTEM.

### Out of scope (first iteration)

- Formal SWE-bench runs.
- Benchmarking of Cursor/IDE internals.
- Cloud-only APIs (can be added later).

## Done when

- Branch `benchmarks` exists.
- At least one runnable method for (a) model inference and (b) agent metrics.
- Methodology and runbooks documented in the repo.
