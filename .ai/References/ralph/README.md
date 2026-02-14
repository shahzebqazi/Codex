# Ralph Loop Reference

Source: https://github.com/snarktank/ralph

## Overview

Ralph is an autonomous AI agent loop that runs coding tools (Amp or Claude Code) repeatedly until all PRD items are complete. Each iteration is a fresh instance with clean context.

## Core Pattern

```
for each iteration:
  1. Spawn fresh AI instance (clean context)
  2. Read PRD (prd.json) and progress.txt
  3. Pick highest priority story where passes: false
  4. Implement single story
  5. Run quality checks (typecheck, lint, test)
  6. Commit if checks pass
  7. Update prd.json (set passes: true)
  8. Append learnings to progress.txt
  9. Stop when all stories passes: true
```

## Key Design Decisions

- **Fresh context per iteration** to avoid drift. Memory persists only via git history, progress.txt, and prd.json.
- **One story per iteration** -- must fit in one context window.
- **Mandatory quality checks** before commit.
- **Code comments for future plans** -- not templates or hallucinated code.
- **AGENTS.md files** document discovered patterns (living documentation, not static templates).

## Task Sizing Rules

- Right-sized: "Add a database column and migration", "Add a UI component to an existing page"
- Too big: "Build the entire dashboard", "Add authentication" (must be split)

## PRD Format (prd.json)

```json
{
  "project": "MyApp",
  "branchName": "ralph/task-name",
  "userStories": [
    {
      "id": "US-001",
      "title": "Story title",
      "description": "As a... I want... so that...",
      "acceptanceCriteria": ["Criteria 1", "Typecheck passes"],
      "priority": 1,
      "passes": false,
      "notes": ""
    }
  ]
}
```

## Communication

Agents communicate through:
1. **progress.txt** -- append-only log with learnings per iteration
2. **AGENTS.md** -- directory-specific pattern documentation updated by agents
3. **Git history** -- commits serve as persistent memory between iterations

## Relevance to dotAi

Ralph validates the core dotAi hypothesis: file-based, declarative agent coordination works. dotAi extends this by:
- Replacing progress.txt with jj commit messages (group-chat style)
- Using jj's conflict-as-data for parallel agent merging
- Adding a memory/RL system for long-term learning
- Supporting multiple simultaneous agents (not just sequential loops)
