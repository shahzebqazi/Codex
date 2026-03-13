# Agenda Generation

Intelligent agenda generation from project metadata. Lightweight and fast.

## Types

| Type | Source | Output |
|------|--------|--------|
| Todo list | TODO.md, TASK_GRAPH.md | Prioritized task list |
| Calendar | jj log, git log | Timeline, milestones |
| Changelog | Commits, jj log | Release notes |
| Journal | Dates, events | JOURNAL.md entry |
| Resource tracking | Project files | Links, deps, refs |

## Inputs

- **jj log** — Version control history, commit messages.
- **TASK_GRAPH.md** — Task dependencies and status.
- **Project files** — Package manifests, configs, docs.

## Behavior

- Read metadata; avoid full codebase scans.
- Output actionable, scannable formats.
- Cache when inputs unchanged.
- Fail gracefully: partial output if some sources missing.
