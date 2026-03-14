# Swarm — Agent Swarm Generation Skill

The **Swarm** skill lets AI agents generate and configure **agent swarms** that execute work against one PRD or all PRDs in parallel, using mixture-of-experts (MoE) / model routing and internal sub-agent capabilities.

## Purpose

- **Trigger:** User or orchestrator requests a swarm (e.g. "swarm", "generate swarm for PRD X", "swarm all PRDs in parallel").
- **Effect:** The agent produces a **swarm plan** and optional machine-readable **swarm config**, including:
  - Task graph (what to do, dependencies, ownership)
  - Model/expert assignment per task (MoE / state-of-the-art routing)
  - Sub-agent policy (when to use internal subagents vs single-context work)
  - Execution mode: **single PRD** (all tasks for one PRD) or **all PRDs in parallel** (one swarm per PRD, tasks run in parallel across PRDs).

## Directory contents

| File | Purpose |
|------|--------|
| [SWARM.md](SWARM.md) | Skill spec: keyword, effect, when to apply, output location |
| [MODELS_AND_EXPERTS.md](MODELS_AND_EXPERTS.md) | Mixture-of-experts and model routing for task types |
| [SUBAGENTS.md](SUBAGENTS.md) | Internal swarm / sub-agent capabilities (e.g. Cursor subagents) |
| [SWARM_CONFIG_SCHEMA.md](SWARM_CONFIG_SCHEMA.md) | Schema for swarm configuration (mode, tasks, models) |
| [swarm_config.schema.json](swarm_config.schema.json) | JSON Schema for tooling and validation |
| [SWARM_PLAN_TEMPLATE.md](SWARM_PLAN_TEMPLATE.md) | Template for AI-generated swarm plan |
| [TASK_GRAPH_TEMPLATE.md](TASK_GRAPH_TEMPLATE.md) | Task graph format aligned with LEAD_ARCHITECT |
| [GENERATOR_PROTOCOL.md](GENERATOR_PROTOCOL.md) | Step-by-step protocol for agents to generate a swarm |

## Relation to project

- **PRDs:** `Documents/PRDs/` or `project/PRDs/` (see START_HERE and CONTRIBUTING). Swarm targets PRD todos and acceptance criteria.
- **Task graph:** Produced swarm plan can emit `project/TASK_GRAPH.md` (or equivalent) for orchestrator or subagent execution.
- **Orchestration:** See [Orchestration/Tasks/SWE/ORCHESTRATOR.md](../../Tasks/SWE/ORCHESTRATOR.md) and [LEAD_ARCHITECT.md](../../Tasks/SWE/LEAD_ARCHITECT.md) for how the swarm plan is consumed.

## Execution modes

1. **Single PRD** — One PRD; tasks decomposed and assigned to agents/subagents; may use multiple models and subagents for parallel tasks.
2. **All PRDs in parallel** — Each PRD gets its own task graph and agent set; PRDs are executed in parallel (e.g. one subagent or worker per PRD).

Agents use [GENERATOR_PROTOCOL.md](GENERATOR_PROTOCOL.md) to produce the plan and config.
