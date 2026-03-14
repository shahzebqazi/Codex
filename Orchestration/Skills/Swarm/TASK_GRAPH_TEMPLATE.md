# Task graph (Swarm / LEAD_ARCHITECT format)

Use this format when emitting `project/TASK_GRAPH.md` from the swarm generator. Aligns with [LEAD_ARCHITECT.md](../../Tasks/SWE/LEAD_ARCHITECT.md) task graph.

```markdown
# Task graph — [PRD name or "Multi-PRD swarm"]

## Tasks

- **T1:** [Task name] (owner: [agent-name])
  - Depends on: none | T0, T2
  - PRD items: [task ids from PRD frontmatter]
  - Status: pending
  - Estimated size: 1 context window
  - Model: [model_key]
  - Subagent: [subagent_type or —]

- **T2:** ...
```

## Fields

| Field | Meaning |
|-------|--------|
| **Tid** | Unique task id (T1, T2, or PRD-prefixed e.g. MVP-1). |
| **Task name** | Short description. |
| **owner** | Agent or worker name for orchestration. |
| **Depends on** | Task ids that must complete first. |
| **PRD items** | Todo ids from the PRD YAML frontmatter (e.g. task-1, task-2). |
| **Status** | pending \| in_progress \| completed. |
| **Estimated size** | e.g. 1 context window (per LEAD_ARCHITECT task sizing). |
| **Model** | model_key from MODELS_AND_EXPERTS. |
| **Subagent** | subagent_type from SUBAGENTS or "—" if none. |

The swarm generator fills this template and can write it to `project/TASK_GRAPH.md` when the plan is intended for the orchestrator or for handoff.
