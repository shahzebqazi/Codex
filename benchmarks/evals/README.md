# Coding-task evals (future work)

<!-- AI: Contains subprompts. Scan for task-specific instructions. Preferences: see PREFERENCES block if present. -->
<!-- PREFERENCES (edit for your project): -->
<!-- (none) -->

**Status:** Stub / out of scope for the first iteration of the benchmarks branch.

## SWE-bench

[SWE-bench](https://github.com/swe-bench/SWE-bench) is an evaluation framework for autonomous coding agents: it provides real GitHub issues and checks whether an agent can produce a patch that passes the project’s tests. Codex and GUIDE.md reference it as a future plan (“Formal SWE-bench evaluation”).

## Possible integration (later)

- Add a script or workflow that runs a subset of SWE-bench tasks against a local agent (e.g. dotAi chatbot or Cursor agent).
- Store results under `benchmarks/results/evals/` (e.g. per-task pass/fail, patch quality).
- Feed aggregate pass rates into MENTAL_MAP or a dedicated evals report.

No implementation exists on this branch yet; this file is a scope placeholder for future work.
