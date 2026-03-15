# Swarm — Agent Swarm Generation Skill

The **Swarm** skill lets AI agents generate and configure **agent swarms** that execute work against one PRD or all PRDs in parallel, using mixture-of-experts (MoE) / model routing and internal sub-agent capabilities.

## Purpose

- **Trigger:** User or orchestrator requests a swarm (e.g. "swarm", "generate swarm for PRD X", "swarm all PRDs in parallel", or a workflow keyword such as "consolidate", "code review pipeline", "release readiness").
- **Effect:** The agent produces a **swarm plan** and optional machine-readable **swarm config**, including:
  - Task graph (or stage graph for workflow mode): what to do, dependencies, ownership
  - Model/expert assignment per task/stage (MoE / state-of-the-art routing)
  - Sub-agent policy (when to use internal subagents vs single-context work)
  - Execution mode: **single PRD**, **all PRDs in parallel**, or **workflow** (pipeline defined in [Workflows/](Workflows/)).

## Directory contents

| File | Purpose |
|------|--------|
| [SWARM.md](SWARM.md) | Skill spec: keyword, effect, routing, when to apply, output location |
| [MODELS_AND_EXPERTS.md](MODELS_AND_EXPERTS.md) | Mixture-of-experts and model routing for task types |
| [SUBAGENTS.md](SUBAGENTS.md) | Internal swarm / sub-agent capabilities (e.g. Cursor subagents) |
| [SWARM_CONFIG_SCHEMA.md](SWARM_CONFIG_SCHEMA.md) | Schema for swarm configuration (mode, tasks, models) |
| [swarm_config.schema.json](swarm_config.schema.json) | JSON Schema for tooling and validation |
| [workflow_config.schema.json](workflow_config.schema.json) | JSON Schema for workflow mode (stages, role, budgets) |
| [WORKFLOW_SCHEMA.md](WORKFLOW_SCHEMA.md) | YAML+MD schema for workflow definition files |
| [WORKFLOW_GENERATOR_PROTOCOL.md](WORKFLOW_GENERATOR_PROTOCOL.md) | Protocol for generating workflow-based swarms |
| [WORKFLOW_PLAN_TEMPLATE.md](WORKFLOW_PLAN_TEMPLATE.md) | Template for workflow swarm plans (stage-oriented) |
| [SWARM_PLAN_TEMPLATE.md](SWARM_PLAN_TEMPLATE.md) | Template for AI-generated PRD swarm plan |
| [TASK_GRAPH_TEMPLATE.md](TASK_GRAPH_TEMPLATE.md) | Task graph format aligned with LEAD_ARCHITECT |
| [GENERATOR_PROTOCOL.md](GENERATOR_PROTOCOL.md) | Step-by-step protocol for PRD-based swarm generation |
| [Workflows/](Workflows/) | Registry of workflow definitions (CONSOLIDATE_PRDS, CODE_REVIEW_PIPELINE, RELEASE_READINESS) |

## Relation to project

- **PRDs:** `Documentation/PRDs/` (see START_HERE and CONTRIBUTING). Swarm targets PRD todos and acceptance criteria.
- **Task graph:** Produced swarm plan can emit `project/TASK_GRAPH.md` (or equivalent) for orchestrator or subagent execution.
- **Orchestration:** See [Orchestration/Tasks/SWE/ORCHESTRATOR.md](../../Tasks/SWE/ORCHESTRATOR.md) and [LEAD_ARCHITECT.md](../../Tasks/SWE/LEAD_ARCHITECT.md) for how the swarm plan is consumed.

## Execution modes

1. **Single PRD** — One PRD; tasks decomposed and assigned to agents/subagents; may use multiple models and subagents for parallel tasks. Use [GENERATOR_PROTOCOL.md](GENERATOR_PROTOCOL.md).
2. **All PRDs in parallel** — Each PRD gets its own task graph and agent set; PRDs are executed in parallel (e.g. one subagent or worker per PRD). Use GENERATOR_PROTOCOL.
3. **Workflow** — Pipeline defined in [Workflows/](Workflows/) (e.g. consolidate PRDs, code review pipeline, release readiness). Stages have roles, token budgets, and dependencies. Use [WORKFLOW_GENERATOR_PROTOCOL.md](WORKFLOW_GENERATOR_PROTOCOL.md) and [WORKFLOW_PLAN_TEMPLATE.md](WORKFLOW_PLAN_TEMPLATE.md).
