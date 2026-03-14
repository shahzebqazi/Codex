# Subagents — Internal Swarm and Sub-Agent Capabilities

Swarm execution can use **internal sub-agents** (e.g. Cursor `mcp_task` subagents, or harness-specific workers) to run tasks in parallel without full Docker/container per agent. This document defines when and how to use subagents in the swarm plan.

## When to use subagents

- **Parallel independent tasks** — Tasks with no dependency on each other can be dispatched to subagents in parallel (e.g. one subagent per task or per PRD).
- **Specialized work** — Assign by **subagent_type** when the harness supports it (e.g. `explore` for codebase search, `shell` for scripts, `generalPurpose` for mixed work).
- **Single PRD, many tasks** — One coordinator agent produces the plan; subagents execute independent task slices.
- **All PRDs in parallel** — One subagent per PRD (or one per PRD’s first wave of independent tasks).

Avoid subagents when:

- The work is a single, small task (overhead not justified).
- Tasks have tight coupling or shared mutable state that is hard to partition (prefer sequential or explicit handoff).
- The environment does not support subagents; then emit a plan for human or orchestrator-driven execution (e.g. Docker workers per task).

## Subagent types (Cursor / MCP)

When the harness is Cursor or an MCP-enabled client, swarm config can reference these **subagent_type** values for dispatch:

| subagent_type | Use for |
|---------------|--------|
| **generalPurpose** | Multi-step research, code search, mixed tasks. |
| **explore** | Fast codebase exploration, file/search patterns. |
| **shell** | Git, jj, build, test, and other CLI operations. |
| **code-reviewer** | Review after implementation; run after code tasks. |
| **other** | Custom or future types; document in swarm plan. |

The swarm generator should set **subagent_type** per task (or per worker) when producing the config so that the executor can invoke the right agent type.

## Contract for subagent tasks

Each task dispatched to a subagent should have:

- **Input:** Task id, PRD ref (and optional section), acceptance criteria, relevant file paths or revsets, model_key.
- **Output:** Result summary, status (success / failure / pivot), and any artifacts (commits, file paths, logs). Status signals (e.g. SUCCESS, PIVOT) align with [Orchestration/Tasks/SWE/ORCHESTRATOR.md](../../Tasks/SWE/ORCHESTRATOR.md) and jj commit conventions.

The coordinator (or orchestrator) aggregates results, handles merge/conflict resolution per ORCHESTRATOR.md, and updates the task graph.

## Policy in swarm config

In [swarm_config.schema.json](swarm_config.schema.json), **subagent_policy** can be:

- **none** — No subagents; single agent or external orchestrator only.
- **independent_only** — Use subagents only for tasks with no dependencies.
- **per_prd** — One subagent per PRD (for “all PRDs in parallel” mode).
- **full** — Use subagents for every task that has an assigned subagent_type (executor may still serialize if only one worker is available).

The generator protocol sets **subagent_policy** and, per task, **subagent_type** (and optionally **model_key**) so the executor has a clear recipe.
