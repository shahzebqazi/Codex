# Swarm plan — [SCOPE: single PRD | all PRDs parallel]

**Generated:** [ISO date]  
**Mode:** [single_prd | all_prds_parallel]  
**PRD(s):** [prd_ref or prd_list]

## Summary

- **Task count:** [N]
- **Model/expert assignments:** [brief list]
- **Subagent policy:** [none | independent_only | per_prd | full]

## Task graph

| Id | PRD | Task type | Model | Depends on | Subagent |
|----|-----|-----------|-------|------------|----------|
| …  | …   | …         | …     | …          | …        |

## Tasks (detail)

### [TASK_ID]: [Short title]

- **PRD:** [prd_ref]
- **Type:** [planning | code | review | refactor | docs | research]
- **Model:** [model_key]
- **Subagent:** [subagent_type or "none"]
- **Depends on:** [list or "none"]
- **Acceptance criteria:** [one or two lines]

(Repeat for each task.)

## Models used

| model_key | Endpoint / role |
|-----------|------------------|
| …         | …                |

## Execution notes

- [Any ordering, concurrency, or environment notes.]
- [Reference to SWARM_CONFIG_*.json if persisted.]

## Traceability

- Source PRD(s): [paths]
- Task graph output: [e.g. project/TASK_GRAPH.md]
- Config: [e.g. Documents/Plans/SWARM_CONFIG_*.json]
