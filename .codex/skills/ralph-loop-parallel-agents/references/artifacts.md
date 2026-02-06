# Core Artifacts

Use these templates as a starting point. Keep artifacts small, append-only where possible, and update them on every iteration.

## prd.json

```json
{
  "source": "PRD.md",
  "items": [
    {
      "id": "FR1",
      "title": "Requirements extraction",
      "acceptance": [
        "Extract epics, user stories, constraints, and acceptance criteria"
      ],
      "owner": "planner",
      "passes": false
    }
  ],
  "open_questions": [
    "What is the canonical PRD schema?"
  ]
}
```

## task_graph.yaml

```yaml
- id: T1
  title: Extract requirement pool
  owner: planner
  depends_on: []
  prd_items: [FR1]
  status: todo
- id: T2
  title: Define interface contracts
  owner: architect
  depends_on: [T1]
  prd_items: [FR3]
  status: todo
```

## interface_contracts.md

```markdown
# Interface Contracts

## Contract: Requirement Pool Schema

- File: prd.json
- Fields: id, title, acceptance[], owner, passes
```

## progress.txt

```text
[2026-02-06] [planner] [T1] started
[2026-02-06] [planner] [T1] blocked: PRD schema missing
```

## decision_log.md

```markdown
# Decision Log

- 2026-02-06: Use a single PRD source of truth and per-agent branches.
```
