# Ralph-Loop Pattern

Perpetual agent pattern. Fresh context per iteration.

Source: https://github.com/snarktank/ralph

## Core Principles

- **Fresh context** — Each iteration starts with minimal, focused context. No conversation bloat.
- **One story per iteration** — Work must fit one context window. Split larger goals.
- **Quality before commit** — Mandatory quality checks before any commit.

## Artifacts

### prd.json
- Product requirements. Acceptance criteria with `passes` status (true/false).
- Drives iteration scope.

### progress.txt
- Append-only log. What was done, what's next.
- Never overwrite. Append each iteration.

### AGENTS.md
- Pattern documentation. How this agent works, conventions.
- Reference for other agents and humans.

## Workflow

1. Read prd.json, progress.txt, AGENTS.md.
2. Pick next story that fits context window.
3. Execute. Run quality checks.
4. If passes: commit, append to progress.txt.
5. Repeat.
