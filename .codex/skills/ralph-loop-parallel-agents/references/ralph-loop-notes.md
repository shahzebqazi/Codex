# Ralph Loop Notes

This file summarizes useful patterns from the Ralph repo and adapts them for Codex workflows.

## Key Mechanics

- Each iteration runs with a fresh agent context to avoid drift.
- Long-term memory lives in files (not in the agent): `progress.txt`, `prd.json`, and git history.
- The loop focuses on the highest-priority PRD item that is not yet marked `passes: true`.
- Every iteration ends with: code changes, quality checks, and artifact updates.

## Suggested Loop Outline

1. Read the PRD and current artifacts.
2. Select the next unmet PRD item.
3. Implement the smallest possible change to satisfy it.
4. Run tests/typechecks.
5. Commit with the PRD item id.
6. Update `prd.json` (set `passes: true`) and append to `progress.txt`.

## Task Sizing Guidance

- If a task cannot be completed in a single iteration, split it.
- Prefer one PRD item per iteration.
- Record clarifying questions immediately and mark the task blocked.

## Collaboration Tips

- Use separate branches per agent to reduce conflicts.
- Merge only after the shared interface contracts are stable.
- Keep `AGENTS.md` up to date with lessons learned.
