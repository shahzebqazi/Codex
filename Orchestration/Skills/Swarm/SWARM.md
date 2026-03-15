# Skill: swarm

**Keywords:** `swarm`, `generate swarm`, `agent swarm`, `swarm for PRD <name>`, `swarm all PRDs`, `run swarm`, `workflow <id>`, `consolidate`, `merge PRDs`, `code review pipeline`, `release readiness`, and any [Workflows/](Workflows/) trigger keywords

**Effect (deterministic):**

1. **Swarm guard rail** — Before any swarm or workflow execution, check `SETTINGS.json → swarm.enabled`. If `false` or absent, do NOT produce swarm plans, do NOT role-play multiple agents, do NOT generate delegation language. Inform the user that swarm mode is disabled and how to enable it. If `confirm_before_dispatch` is true, present the confirmation summary and wait for user approval before proceeding.
2. **Route by intent** — Determine which mode applies:
   - **Workflow:** User message matches a [Workflows/](Workflows/) trigger keyword (e.g. "consolidate", "merge PRDs", "code review pipeline", "release readiness") or says "workflow &lt;id&gt;" or "run workflow &lt;id&gt;". Use [WORKFLOW_GENERATOR_PROTOCOL.md](WORKFLOW_GENERATOR_PROTOCOL.md) and emit a workflow swarm plan and config per [workflow_config.schema.json](workflow_config.schema.json).
   - **Single PRD:** User names one PRD (by name or path). Use [GENERATOR_PROTOCOL.md](GENERATOR_PROTOCOL.md) with mode = `single_prd`, prd_ref = that PRD.
   - **All PRDs in parallel:** User says "all PRDs", "parallel PRDs", or equivalent. Use GENERATOR_PROTOCOL with mode = `all_prds_parallel`, prd_list = PRDs from `Documentation/PRDs/`.
   - **Ambiguous:** List available workflows (from [Workflows/README.md](Workflows/README.md)) and available PRDs; ask which to run.
3. **Execute the chosen protocol** — For workflow mode: follow [WORKFLOW_GENERATOR_PROTOCOL.md](WORKFLOW_GENERATOR_PROTOCOL.md). For PRD modes: follow [GENERATOR_PROTOCOL.md](GENERATOR_PROTOCOL.md) (read PRDs, decompose tasks, assign models/subagents, emit plan and config).
4. **Output location:**
   - **In-chat:** Always present a concise summary (mode, scope, stage/task count, model assignments). Optionally show full plan in chat.
   - **Files (when user or protocol asks to persist):**
     - Swarm plan: `Documentation/Plans/SWARM_PLAN_<date>_<scope>.md` or path given by user.
     - Swarm config: `Documentation/Plans/SWARM_CONFIG_<date>_<scope>.json` or path given by user.
     - Task graph: `project/TASK_GRAPH.md` (or path per project convention) when intended for orchestrator consumption.

**When to apply:** User or orchestrator message clearly requests generating or running an agent swarm (workflow, one PRD, or all PRDs in parallel), or uses a workflow trigger keyword.

**Dependencies:** For PRD modes, PRDs must exist in the project. For workflow mode, workflow definitions must exist under `Orchestration/Skills/Swarm/Workflows/`. If the chosen scope is missing, say so and offer to create or point to where to add it.
