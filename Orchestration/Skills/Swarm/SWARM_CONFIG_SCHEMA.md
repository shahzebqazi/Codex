# Swarm configuration schema

Swarm config is a JSON object that describes **mode**, **tasks** (or **stages** for workflow mode), **models**, and **subagent policy**. It is validated against [swarm_config.schema.json](swarm_config.schema.json). For workflow-only config with a stages array, see [workflow_config.schema.json](workflow_config.schema.json).

## Top-level fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| **version** | string | yes | Schema version, e.g. `"0.1.0"`. |
| **mode** | string | yes | `"single_prd"`, `"all_prds_parallel"`, or `"workflow"`. |
| **prd_ref** | string | conditional | For `single_prd`, the PRD id or path (e.g. `MVP_PRD`, `Documentation/PRDs/MVP_PRD.md`). |
| **prd_list** | string[] | conditional | For `all_prds_parallel`, list of PRD ids or paths. |
| **workflow_ref** | string | conditional | For `workflow`, the workflow id or path (e.g. `consolidate-prds`, `Workflows/CONSOLIDATE_PRDS.md`). |
| **tasks** | task[] | yes* | List of tasks. *Omit when using workflow config with **stages** only (workflow_config.schema.json). |
| **models** | object | no | Map of model_key → { endpoint?, provider?, role? }. Omit to use project default. |
| **subagent_policy** | string | no | `"none"` \| `"independent_only"` \| `"per_prd"` \| `"full"`. Default `"independent_only"`. |

## Task object

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| **id** | string | yes | Unique task id (e.g. `task-1`, `MVP-3`, or stage_id for workflow). |
| **prd_ref** | string | conditional | PRD this task belongs to (required for single_prd / all_prds_parallel). |
| **stage_id** | string | no | For workflow mode: stage id from workflow definition. |
| **owner** | string | no | Agent or worker name (e.g. `agent-1`, `subagent-explore`). |
| **role** | string | no | Agent role name (e.g. Coordinator, XP Engineer Reviewer); used in workflow mode. |
| **model_key** | string | no | Key from `models` or default. |
| **depends_on** | string[] | no | Task ids that must complete before this one. |
| **acceptance_criteria** | string | no | One-line or short criteria. |
| **subagent_type** | string | no | For Cursor/MCP: `generalPurpose`, `explore`, `shell`, `code-reviewer`, etc. |
| **task_type** | string | no | `planning`, `code`, `review`, `refactor`, `docs`, `research`. |
| **context_budget** | integer | no | Input token budget for this task/stage. |
| **output_budget** | integer | no | Output token budget. |
| **turns** | string \| integer | no | Estimated conversation turns. |
| **inputs** | string[] | no | What this stage reads (workflow mode). |
| **outputs** | string[] | no | What this stage produces (workflow mode). |

## Example (single PRD)

```json
{
  "version": "0.1.0",
  "mode": "single_prd",
  "prd_ref": "MVP_PRD",
  "subagent_policy": "independent_only",
  "tasks": [
    { "id": "T1", "prd_ref": "MVP_PRD", "task_type": "planning", "model_key": "reasoning", "depends_on": [] },
    { "id": "T2", "prd_ref": "MVP_PRD", "task_type": "code", "model_key": "code", "depends_on": ["T1"], "subagent_type": "generalPurpose" }
  ],
  "models": {
    "reasoning": { "role": "planning" },
    "code": { "role": "code" }
  }
}
```

## Example (all PRDs in parallel)

```json
{
  "version": "0.1.0",
  "mode": "all_prds_parallel",
  "prd_list": ["MVP_PRD", "FEATURES_PRD"],
  "subagent_policy": "per_prd",
  "tasks": [
    { "id": "MVP-1", "prd_ref": "MVP_PRD", "owner": "swarm-mvp", "depends_on": [] },
    { "id": "FEAT-1", "prd_ref": "FEATURES_PRD", "owner": "swarm-feat", "depends_on": [] }
  ]
}
```

## Example (workflow mode)

```json
{
  "version": "0.1.0",
  "mode": "workflow",
  "workflow_ref": "consolidate-prds",
  "subagent_policy": "full",
  "tasks": [
    { "id": "coordinator", "stage_id": "coordinator", "role": "Coordinator", "task_type": "planning", "model_key": "reasoning", "depends_on": [], "context_budget": 60000, "output_budget": 6000 },
    { "id": "xp-review", "stage_id": "xp-review", "role": "XP Engineer Reviewer", "task_type": "review", "model_key": "code", "depends_on": ["coordinator"], "context_budget": 50000, "subagent_type": "generalPurpose" }
  ],
  "models": {
    "reasoning": { "role": "planning" },
    "code": { "role": "code" }
  }
}
```

Validation: use `swarm_config.schema.json` (or `workflow_config.schema.json` for workflow-only config with optional **stages** array) with a JSON Schema validator when persisting or passing config to tooling.
