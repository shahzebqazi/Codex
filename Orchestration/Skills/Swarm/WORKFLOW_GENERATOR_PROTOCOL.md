# Workflow generator protocol — How to generate a workflow-based swarm

When the **swarm** skill is invoked with a **workflow** (by keyword, user intent, or explicit reference), the AI agent follows this protocol to produce a workflow swarm plan and config. No implementation work is done; the output is a **plan** and **config** that an orchestrator, subagent runner, or human can execute.

**Prerequisite:** Swarm capability gate must pass (see SYSTEM_PROMPT and SETTINGS.json → swarm.enabled). If the user has not confirmed dispatch, present the confirmation summary and wait for approval before proceeding.

## Step 1: Resolve workflow

- If the user said a **workflow keyword** (e.g. "consolidate", "merge PRDs", "release readiness", "code review pipeline"): match against [Workflows/](Workflows/) registry trigger lists; set **mode** = `workflow`, **workflow_ref** = that workflow’s id or path.
- If the user said **workflow &lt;name&gt;** or **run workflow &lt;id&gt;**: set **workflow_ref** = that name or id; resolve to a file under `Orchestration/Skills/Swarm/Workflows/` (or project-configured workflow path).
- If unclear: list available workflows (from Workflows/ README or by scanning workflow files) and ask which to run.

## Step 2: Load workflow definition

- Read the workflow definition file (markdown with YAML frontmatter per [WORKFLOW_SCHEMA.md](WORKFLOW_SCHEMA.md)).
- Extract: **id**, **name**, **trigger**, **required_capabilities**, **subagent_policy**, **stages** (stage_id, role, task_type, model_key, context_budget, output_budget, turns, depends_on, inputs, outputs, acceptance_criteria, subagent_type).
- Verify **required_capabilities** against SETTINGS.json → swarm.capabilities; if any required capability is missing, reject with a clear message and do not emit a plan.

## Step 3: Resolve inputs

- For each stage, resolve **inputs** to concrete paths or artifacts when they reference project paths (e.g. `Documentation/PRDs/*`), PRD refs, or "output of stage X". List resolved paths or note "in-memory from previous stage" where applicable.
- Ensure any PRDs or files referenced exist; if not, warn in the plan or ask the user.

## Step 4: Map stages to tasks

- Each workflow stage becomes one or more tasks in the emitted config. For most workflows, one stage = one task (same id or stage_id as task id).
- Build a task graph: for each stage, set **depends_on** from the stage’s **depends_on** (reference other stage_ids or task ids).
- Ensure the dependency graph is acyclic. Assign task **id** (e.g. stage_id or prefixed like `WF-1`, `WF-2`).
- Carry over **role**, **model_key**, **acceptance_criteria**, **subagent_type**, **task_type**, and optionally **context_budget**, **output_budget**, **turns**, **inputs**, **outputs** to each task.

## Step 5: Assign models per stage

- Use each stage’s **model_key** from the workflow definition. If a stage omits model_key, derive from **task_type** using [MODELS_AND_EXPERTS.md](MODELS_AND_EXPERTS.md).
- Resolve model_key to project SETTINGS.json or swarm config **models** block. Document in the plan which model/endpoint handles which stage.

## Step 6: Set subagent policy and types

- Set **subagent_policy** from the workflow definition (none | independent_only | per_prd | full).
- For each task, set **subagent_type** from the stage’s subagent_type when present; otherwise leave blank or infer from task_type (e.g. review → code-reviewer).

## Step 7: Emit swarm plan (markdown)

- Fill [WORKFLOW_PLAN_TEMPLATE.md](WORKFLOW_PLAN_TEMPLATE.md) with:
  - Mode = workflow, workflow_ref, stage count, model assignments, subagent policy.
  - Stage graph table and per-stage detail (role, token budgets, turns, inputs/outputs, pipeline ordering).
  - Models used, execution notes, traceability to workflow file.
- Output in chat and/or write to `Documentation/Plans/SWARM_PLAN_<date>_<workflow_id>.md` (or `Orchestration/Harness/Plans/` per project) when persistence is requested.

## Step 8: Emit swarm config (JSON)

- Build a JSON object conforming to [workflow_config.schema.json](workflow_config.schema.json):
  - version, mode = `workflow`, workflow_ref, tasks (with id, stage_id, role, model_key, depends_on, acceptance_criteria, subagent_type, task_type, optional context_budget, output_budget, turns, inputs, outputs), optional stages (if emitting stage-centric config), optional models, subagent_policy.
- Validate against the schema if tooling is available.
- Output in chat and/or write to `Documentation/Plans/SWARM_CONFIG_<date>_<workflow_id>.json` when persistence is requested.

Optional: When the swarm is intended for the project’s orchestrator, produce **project/TASK_GRAPH.md** in the format of [TASK_GRAPH_TEMPLATE.md](TASK_GRAPH_TEMPLATE.md), with task ids and dependencies matching the workflow plan.

## Checklist (agent self-verify)

- [ ] Workflow is resolved: workflow_ref points to a loaded workflow definition.
- [ ] required_capabilities are satisfied by SETTINGS.json → swarm.capabilities.
- [ ] All stages were read; dependency graph is acyclic.
- [ ] Every task has id, role, task_type, and model_key; depends_on references valid task/stage ids.
- [ ] Subagent_policy and per-task subagent_type are set from the workflow.
- [ ] Workflow plan (markdown) is complete and traceable to the workflow file.
- [ ] Swarm config (JSON) validates against workflow_config.schema.json.
