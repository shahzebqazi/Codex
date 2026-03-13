# Swarm configuration schema

Swarm config is a JSON object that describes **mode**, **tasks**, **models**, and **subagent policy**. It is validated against [swarm_config.schema.json](swarm_config.schema.json).

## Top-level fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| **version** | string | yes | Schema version, e.g. `"0.1.0"`. |
| **mode** | string | yes | `"single_prd"` or `"all_prds_parallel"`. |
| **prd_ref** | string | conditional | For `single_prd`, the PRD id or path (e.g. `MVP_PRD`, `Documents/PRDs/MVP_PRD.md`). |
| **prd_list** | string[] | conditional | For `all_prds_parallel`, list of PRD ids or paths. |
| **tasks** | task[] | yes | List of tasks with id, prd_ref, owner, model_key, depends_on, acceptance_criteria, subagent_type. |
| **models** | object | no | Map of model_key → { endpoint?, provider?, role? }. Omit to use project default. |
| **subagent_policy** | string | no | `"none"` \| `"independent_only"` \| `"per_prd"` \| `"full"`. Default `"independent_only"`. |

## Task object

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| **id** | string | yes | Unique task id (e.g. `task-1`, `MVP-3`). |
| **prd_ref** | string | yes | PRD this task belongs to. |
| **owner** | string | no | Agent or worker name (e.g. `agent-1`, `subagent-explore`). |
| **model_key** | string | no | Key from `models` or default. |
| **depends_on** | string[] | no | Task ids that must complete before this one. |
| **acceptance_criteria** | string | no | One-line or short criteria. |
| **subagent_type** | string | no | For Cursor/MCP: `generalPurpose`, `explore`, `shell`, `code-reviewer`, etc. |
| **task_type** | string | no | `planning`, `code`, `review`, `refactor`, `docs`, `research`. |

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

Validation: use `swarm_config.schema.json` with a JSON Schema validator when persisting or passing config to tooling.
