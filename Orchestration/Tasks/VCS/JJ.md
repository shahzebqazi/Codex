# JJ -- Jujutsu Version Control for AI Agents

## Overview

Jujutsu (jj) is the communication protocol of the dotAi system. All AI agents MUST use jj for version control. Humans use git. jj operates on top of git repositories -- every jj repo is a valid git repo.

Source: https://github.com/martinvonz/jj

## Why jj for Agents

1. **Working-copy-as-commit** -- every file write is automatically a commit. No "forgot to save" problem.
2. **Conflicts as data** -- parallel agent branches always merge. Conflicts are stored in commits, not blocking errors.
3. **Automatic rebase** -- when shared code changes, all agent branches auto-rebase.
4. **Operation log** -- every action recorded and undoable. Full audit trail.
5. **Concurrent safety** -- multiple containers can safely operate on the same repo.
6. **Revsets** -- query the commit graph programmatically for orchestration.

## Commit Messages as Group Chat

jj commit messages are the primary communication channel between agents. They should be:
- **Brief** -- minimize tokens for reading efficiency
- **Addressed** -- mention relevant agents or humans when needed
- **Actionable** -- convey status, findings, or requests

### Format

```
[agent-name] action summary @mention-if-relevant
```

### Examples

```
[builder-1] auth module tests passing @qa check integration
[qa] found race condition in session handler, blocking T-42
[architect] approved REST->gRPC migration, updating contracts
[resolver] fixed merge conflict in auth.rs, tests green
[orchestrator] all T-40 tasks complete, merging to main
```

### Special Keywords in Messages

- **SUCCESS** -- task complete, acceptance criteria met
- **PIVOT** -- changing approach, explain why
- **BLOCKED** -- waiting on human input or dependency
- **CONTINUE** -- making progress, status update

## Agent Identity

All agents commit under a dedicated AI email:
```
ai@dotai.dev
```

Configurable in `config/SETTINGS.json` under `agents.email`.

## Feature branch per task

**AI agents MUST create a feature branch for each task they work on.** Before starting work on a task:

1. Create a new branch for that task (e.g. `jj branch set agent-name/task-id` or `jj new -m "WIP: task-id"` and then create a bookmark).
2. Do all work for that task on that branch. Do not commit task work to `main` or to another task’s branch.
3. When the task is complete, mark the branch (e.g. with a commit message containing SUCCESS) so it can be merged or reviewed.

This applies whether the agent is spawned by an orchestrator or working alone (e.g. chatbot, Cursor, Codex). If no task id is provided, derive a short branch name from the task (e.g. `feature/add-settings-41` or `agent/current-date-brief-description`).

## Branch Naming

```
agent-name/task-id
```

Examples: `builder-1/T-42`, `qa/T-43-review`, `resolver/merge-auth`, `feature/settings-persistence`

## Key Commands for Agents

```bash
# Status
jj status                    # working copy changes
jj log -r "all()"           # full commit graph
jj log -r "mine()"          # my commits only

# Commit (automatic -- jj amends working copy commit)
jj describe -m "[agent] message"   # set commit message
jj new                             # start new change on top

# Branch operations
jj bookmark create agent-1/T-42   # create named branch
jj bookmark set agent-1/T-42      # update branch pointer

# Orchestrator queries (revsets)
jj log -r "author(ai@dotai.dev) & ~ancestors(main)"  # all unmerged agent work
jj log -r "description(contains:'SUCCESS')"           # completed tasks
jj log -r "description(contains:'BLOCKED')"           # blocked agents
jj log -r "conflicts()"                               # commits with conflicts

# Merge (orchestrator)
jj new agent-1/T-42 agent-2/T-43   # merge two agent branches
jj resolve                          # resolve conflicts if any

# Sync with git
jj git push                         # push bookmarks to git remote
jj git fetch                        # pull from git remote
```

## Context Refresh

When an agent's context window fills up:
1. Commit current work with a descriptive message
2. Start a new jj change (`jj new`)
3. Re-read only: current task PRD, MENTAL_MAP.md, and recent jj log
4. Continue from where the previous commit left off
5. The jj commit history serves as the agent's external memory
