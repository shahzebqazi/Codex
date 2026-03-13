# SYMBOLIC_LANGUAGE -- Universal Shorthand for .ai/ Documents

## Overview

Use these symbols in any `.ai/` markdown file for compact status tracking. Parse on read; write when updating status. Valid in headings, tables, lists, and inline.

## Status Indicators

| Symbol | Meaning |
|--------|---------|
| `!` | Active |
| `@` | Blocked |
| `#` | Pending |
| `$` | Completed |
| `%` | Paused |
| `&` | Cancelled |

Examples: `!Auth module`, `@Blocked on API`, `$Done`, `%Paused`

## Priority

| Symbol | Meaning |
|--------|---------|
| `!` | Critical |
| `@` | Medium |
| `#` | Low |

Use with status for clarity, e.g. `!!` = active + critical, `#$` = low + completed.

## Roles

| Symbol | Role |
|--------|------|
| `!` | Developer |
| `@@` | Architect |
| `*` | QA |
| `**` | DevOps |

Examples: `! @john`, `@@ approved`, `* needs review`

## Progress

Express as `0%`–`100%`. Use in headings or inline: `[45%]`, `Progress: 70%`.

## Usage in .ai/ Files

- **Headings**: `## ! Auth refactor [60%]`
- **Tables**: Status column with `!`, `@`, `#`, `$`, `%`, `&`
- **Lists**: `- $ Implemented`, `- @ Blocked on DB`
- **Inline**: `Task is $ complete` or `! critical`

Agents parse these when reading; apply them when updating `.ai/` documents for consistency.
