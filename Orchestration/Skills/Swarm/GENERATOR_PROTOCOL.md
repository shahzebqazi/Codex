# Generator protocol — How to generate an agent swarm

When the **swarm** skill is invoked, the AI agent follows this protocol to produce a swarm plan and optional config. No implementation work is done; the output is a **plan** and **config** that an orchestrator, subagent runner, or human can execute.

## Step 1: Resolve scope

- If the user said **one PRD** (by name or path): set **mode** = `single_prd`, **prd_ref** = that PRD id or path.
- If the user said **all PRDs** or **parallel PRDs**: set **mode** = `all_prds_parallel`, **prd_list** = list of PRD ids or paths under `Documents/PRDs/` (or project-configured PRD path).
- If unclear: list available PRDs and ask which to include, or default to all.

## Step 2: Load PRDs

- Read each target PRD file (markdown with YAML frontmatter).
- Extract: **name**, **overview**, **todos** (id, content, status).
- Note acceptance criteria in todo content or in PRD body.

## Step 3: Decompose into tasks

- For **single_prd:** Build one task graph from that PRD’s todos; add dependencies (e.g. planning → code → review) and split any todo that is too large into multiple tasks (one context window per task, per LEAD_ARCHITECT).
- For **all_prds_parallel:** For each PRD, build a task graph; optionally add a “coordinator” or “first task” per PRD. Task ids should be unique (e.g. prefix with PRD: `MVP-1`, `FEAT-2`).

## Step 4: Assign models/experts

- For each task, set **task_type** (planning, code, review, refactor, docs, research) from [MODELS_AND_EXPERTS.md](MODELS_AND_EXPERTS.md).
- Map **task_type** to **model_key** (default, local, reasoning, code, review, or project-defined). Prefer project SETTINGS.json and local-first; document any state-of-the-art choice in the plan.

## Step 5: Set subagent policy and types

- Choose **subagent_policy:** none | independent_only | per_prd | full (see [SUBAGENTS.md](SUBAGENTS.md)).
- For each task that will use a subagent, set **subagent_type** (e.g. generalPurpose, explore, shell, code-reviewer). Leave blank or omit when no subagent.

## Step 6: Emit swarm plan (markdown)

- Fill [SWARM_PLAN_TEMPLATE.md](SWARM_PLAN_TEMPLATE.md) with:
  - Mode, PRD(s), task count, model assignments, subagent policy.
  - Task graph table and per-task detail.
  - Models used, execution notes, traceability.
- Output in chat and/or write to `Documents/Plans/SWARM_PLAN_<date>_<scope>.md` if the user or project expects a persisted file.

## Step 7: Emit swarm config (JSON)

- Build a JSON object conforming to [swarm_config.schema.json](swarm_config.schema.json):
  - version, mode, prd_ref or prd_list, tasks (with id, prd_ref, model_key, depends_on, acceptance_criteria, subagent_type, task_type), optional models, subagent_policy.
- Validate against the schema if tooling is available.
- Output in chat and/or write to `Documents/Plans/SWARM_CONFIG_<date>_<scope>.json` when persistence is requested.

## Step 8: Optional — Emit task graph for orchestrator

- When the swarm is intended for the project’s orchestrator (or LEAD_ARCHITECT consumer), produce **project/TASK_GRAPH.md** in the format of [TASK_GRAPH_TEMPLATE.md](TASK_GRAPH_TEMPLATE.md).
- Ensure task ids, dependencies, and PRD items match the swarm plan and config.

## Checklist (agent self-verify)

- [ ] Scope is clear: single PRD or all PRDs in parallel.
- [ ] All target PRDs were read and todos extracted.
- [ ] Every task has id, prd_ref, and at least task_type → model_key.
- [ ] Dependencies are acyclic and stated.
- [ ] Subagent_policy and per-task subagent_type are set where applicable.
- [ ] Swarm plan (markdown) is complete and traceable.
- [ ] Swarm config (JSON) validates against swarm_config.schema.json.
- [ ] If required, project/TASK_GRAPH.md is written and consistent with the plan.
