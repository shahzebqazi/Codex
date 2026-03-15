# Workflow swarm plan — [WORKFLOW_NAME]

**Generated:** [ISO date]  
**Mode:** workflow  
**Workflow ref:** [workflow_id or path]

## Summary

- **Stage count:** [N]
- **Model/expert assignments:** [brief list]
- **Subagent policy:** [none | independent_only | per_prd | full]
- **Pipeline ordering:** [e.g. coordinator → (xp, graphics, infra) in parallel → final writer]

## Stage graph

| Stage id | Role | Task type | Model | Context | Output | Depends on | Subagent |
|----------|------|-----------|-------|---------|--------|------------|----------|
| …        | …    | …         | …     | …K      | …K     | …          | …        |

## Stages (detail)

### [STAGE_ID]: [Role name]

- **Role:** [role]
- **Type:** [planning | code | review | refactor | docs | research]
- **Model:** [model_key]
- **Context budget:** [context_budget] tokens
- **Output budget:** [output_budget] tokens
- **Turns:** [turns]
- **Subagent:** [subagent_type or "none"]
- **Depends on:** [list of stage_ids or "none"]
- **Inputs:** [list]
- **Outputs:** [list]
- **Acceptance criteria:** [one or two lines]

(Repeat for each stage.)

## Models used

| model_key | Endpoint / role |
|-----------|------------------|
| …         | …                |

## Execution notes

- [Which stages run in parallel.]
- [Ordering and handoff (e.g. final-writer waits for all reviewers).]
- [Reference to SWARM_CONFIG_*.json if persisted.]

## Traceability

- Source workflow: [path to workflow definition file]
- Task graph output: [e.g. project/TASK_GRAPH.md if emitted]
- Config: [e.g. Documentation/Plans/SWARM_CONFIG_<date>_<workflow_id>.json]
