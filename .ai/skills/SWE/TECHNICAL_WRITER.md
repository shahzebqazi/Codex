# TECHNICAL_WRITER -- Agentic Logging and Documentation

## Overview

Agents write structured, append-only logs for work tracking, build history, and errors. Use this skill when recording progress, commits, or incidents. Integrates with jj for automated buildlog entries.

## Log Types

| Log | Purpose | Priority levels | Path |
|-----|---------|-----------------|------|
| Worklog | Daily work, decisions, blockers | P1/P2/P3 | `worklog/` |
| Buildlog | Build/release history, conventional commits | — | `buildlog/` |
| Changelog | User-facing changes by version | — | `CHANGELOG.md` |
| Error log | Failures, exceptions, diagnostics | — | `logs/errors/` |

## Entry Formats

### Worklog (prioritized)

```
YYYY-MM-DD HH:mm [P1|P2|P3] <summary>
<optional body>
---
```

- P1 = critical, must address today
- P2 = important, this week
- P3 = nice-to-have

### Buildlog (conventional commits)

```
YYYY-MM-DD HH:mm <type>(<scope>): <description>
  rev: <jj revision or git hash>
  refs: #issue if any
---
```

Types: `feat`, `fix`, `docs`, `refactor`, `test`, `chore`, etc.

### Changelog

```
## [version] - YYYY-MM-DD
### Added / Changed / Fixed / Removed
- Entry
```

### Error log

```
YYYY-MM-DD HH:mm [ERROR|WARN] <component>: <message>
<stack or context>
---
```

## XDG-Compliant Paths

Use `$XDG_DATA_HOME` when available, fallback to `.ai/`:

- `$XDG_DATA_HOME/dotai/worklog/` or `.ai/logs/worklog/`
- `$XDG_DATA_HOME/dotai/buildlog/` or `.ai/logs/buildlog/`
- `$XDG_DATA_HOME/dotai/logs/errors/` or `.ai/logs/errors/`

Ensure parent dirs exist before writing.

## Append-Only

- Never overwrite existing log entries.
- Append new entries at end of file or to new dated file.
- Use `---` as entry separator for parsing.

## Log Rotation

- Worklog: one file per day, e.g. `worklog/2025-02-14.md`.
- Buildlog: one file per release or month, e.g. `buildlog/2025-02.md`.
- Error log: rotate by size (e.g. 1MB) or age (e.g. 7 days); archive to `*.archived.log`.

## Quick Access Commands

Agents can invoke:

- `tail -n 50 .ai/logs/worklog/$(date +%Y-%m-%d).md` — recent work
- `tail -n 20 .ai/logs/buildlog/current.md` — recent builds
- `grep -r "ERROR" .ai/logs/errors/` — recent errors

## jj Integration

On each meaningful jj commit:

1. Parse commit message for conventional format.
2. Append to buildlog with timestamp and revision.
3. Use `jj log -r @- -n 1 -T 'commit_id'` to get rev.

Example automation: after `jj describe -m "feat(auth): add OAuth"`, agent appends:

```
2025-02-14 14:32 feat(auth): add OAuth
  rev: abc123
---
```

Manual buildlog entries are fine; automated sync from jj is preferred when available.
