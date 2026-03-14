# Skill: swarm

**Keywords:** `swarm`, `generate swarm`, `agent swarm`, `swarm for PRD <name>`, `swarm all PRDs`, `run swarm`

**Effect (deterministic):**

1. **Interpret intent** — Determine whether the user wants:
   - **Single PRD:** A swarm focused on one PRD (all its todos and acceptance criteria), or
   - **All PRDs in parallel:** Separate swarms per PRD, to be executed in parallel.
2. **Follow the Generator Protocol** — Execute the steps in [GENERATOR_PROTOCOL.md](GENERATOR_PROTOCOL.md):
   - Read PRDs from `Documents/PRDs/` (or project-configured PRD path).
   - Decompose work into tasks with dependencies and acceptance criteria.
   - Assign models/experts per task using [MODELS_AND_EXPERTS.md](MODELS_AND_EXPERTS.md).
   - Decide sub-agent usage per task using [SUBAGENTS.md](SUBAGENTS.md).
   - Emit a **swarm plan** (markdown) and, when requested or when tooling is used, a **swarm config** (JSON conforming to [swarm_config.schema.json](swarm_config.schema.json)).
3. **Output location:**
   - **In-chat:** Always present a concise summary of the swarm (mode, PRD(s), task count, model assignments). Optionally show full plan in chat.
   - **Files (when user or protocol asks to persist):**
     - Swarm plan: `Documents/Plans/SWARM_PLAN_<date>_<scope>.md` or path given by user.
     - Swarm config: `Documents/Plans/SWARM_CONFIG_<date>_<scope>.json` or path given by user.
     - Task graph: `project/TASK_GRAPH.md` (or path per project convention) when the swarm is intended for orchestrator consumption.

**When to apply:** User or orchestrator message clearly requests generating or running an agent swarm for one PRD, all PRDs in parallel, or "swarm" with scope implied by context.

**Dependencies:** PRDs must exist in the project. If no PRDs exist, the agent should say so and offer to create a minimal PRD or point to where to add one.
