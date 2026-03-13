# Task Management Format

Structured task format for dotAi system. Compatible with plan.md for Cursor integration.

## Format

```
[Task] #Priority #Status [Deadline]
  - Step 1
  - Step 2
```

## Priorities

- `#payment` — Revenue/critical path
- `#urgent` — Time-sensitive
- `#overdue` — Past deadline

## Statuses

- `#not-started`
- `#in-progress`
- `#completed`

## Completed Tasks

Prefix task line with `x`:

```
x [Implement auth] #payment #completed [2025-02-10]
  - Step 1
  - Step 2
```

## Example

```
[Add payment webhook] #payment #in-progress [2025-02-20]
  - Define endpoint schema
  - Implement signature verification
  - Add retry logic

[Update docs] #not-started
  - README
  - API reference
```

## Integration

- Use same format in `plan.md` YAML frontmatter todos.
- Agents: parse, update status, sync with TASK_GRAPH.md.
