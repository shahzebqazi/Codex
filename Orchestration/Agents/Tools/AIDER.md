# Aider Pair-Programming Patterns

Model-agnostic pair programming for code edits.

Source: https://github.com/Aider-AI/aider

## Edit Formats

- **whole** — Full file replacement.
- **diff** — Unified diff format.
- **udiff** — Alternative diff format.

Choose per model capability. Diff formats reduce token usage.

## Repo Awareness

- **Repo map** — Via tree-sitter. Project structure, file relationships.
- Improves context: agent knows what files exist, how they connect.

## Self-Healing Loop

1. Agent proposes edit.
2. Apply edit.
3. Run lint/test.
4. If fail: feed errors back, retry.
5. Until pass or max iterations.

## Orchestration

- Model-agnostic. Swap LLM backends without changing workflow.
- Edit → validate → commit cycle is invariant.

## Auto-Commit

- Sensible commit messages from diff/changes.
- Configurable: when to commit, message style.
