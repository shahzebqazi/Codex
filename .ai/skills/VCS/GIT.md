# GIT -- Git Conventions for Humans

## Overview

In the dotAi system, git is the human-facing VCS layer. Humans commit with git; AI agents use jj (which operates on the same git repo). Both coexist safely.

## Human Commit Conventions

- Use conventional commits: `feat:`, `fix:`, `docs:`, `refactor:`, `test:`, `chore:`
- Commit messages convey human intent and decisions
- Humans commit on `main` or feature branches
- AI agent work lives on `agent-name/task-id` branches until merged

## Branch Strategy

```
main                        -- stable, human-approved code
  feature/name              -- human feature branches
  agent-name/task-id        -- AI agent working branches (managed by jj)
```

## Interaction with jj

- jj reads and writes the same .git directory
- `jj git push` syncs jj bookmarks to git remote
- Humans can `git log` to see all work (human + agent)
- Agent commits are identifiable by `ai@dotai.dev` email

## GitHub Integration

Agents can (if enabled in config/SETTINGS.json):
- Create GitHub issues for bugs or suggestions
- Maintain their own branches on the remote
- Create pull requests for completed work
- Comment on existing issues with findings

## Protected Operations

These git operations require human approval regardless of RULES.md settings:
- Force push to main
- Deleting remote branches created by humans
- Modifying .github/ workflows
- Changing repository settings
