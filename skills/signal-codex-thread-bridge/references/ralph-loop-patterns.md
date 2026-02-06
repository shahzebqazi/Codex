# Ralph Loop Patterns

Use these patterns to keep perpetual coding agents aligned with a PRD and to avoid context drift.

## Core Loop

- Start each iteration with a small, well-scoped task.
- Load only the PRD, current task, and the last progress summary.
- End each iteration by updating progress artifacts.

## Recommended Artifacts

- `prd.json`: structured requirements and acceptance criteria.
- `progress.txt`: rolling summary of what is done and what is next.
- `AGENTS.md`: any policy or coordination updates for agent behavior.

## Practical Guidance

- Prefer small tasks over large epics.
- If an iteration becomes ambiguous, write a concrete clarification question first.
- Keep a crisp trace from work output back to PRD items.
