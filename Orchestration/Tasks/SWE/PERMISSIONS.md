# PERMISSIONS -- Access Control and Execution Safety

## Overview

Agents apply permission levels and execution modes to gate file access and operations. Use this skill when deciding whether to read, write, or execute. Reference `project/RULES.md` for project defaults.

## Permission Levels

| Level | File read | File write | Execute | Scope |
|-------|-----------|------------|---------|-------|
| Restricted | .ai/ only | None | None | Sandboxed exploration |
| Standard | Project + .ai/ | Project + .ai/ (excl. config) | Non-destructive commands | Normal development |
| Permissive | All | All | All | System-level work |
| Custom | Per project rules | Per project rules | Per project rules | See project/RULES.md |

Project rules in `project/RULES.md` override defaults. User rules in `.ai/config/local/RULES.md` (gitignored) override project rules.

## File Read/Write Rules

- **Always allowed**: `.ai/memories/`, `.ai/references/`, `.ai/project/`, task-specific files.
- **Gated**: `.ai/config/`, `.env`, system configs — require explicit approval in semi-auto/manual.
- **Dangerous**: Lock files (`package-lock.json`, `Cargo.lock`), `.git/` — confirm before modifying.
- **Never touch**: User's home dir, system dirs (unless permissive + explicit task).

## Safe vs Dangerous Operations

| Safe | Dangerous |
|------|-----------|
| Read any project file | Delete files, directories |
| Create new files | Overwrite without backup |
| Edit with undo path | Force push, reset |
| Run tests, linters | Install deps, modify lock files |
| Commit (jj) | Merge, rewrite history |
| Start containers | Stop/kill containers |
| Create branches | Delete branches |

For dangerous ops: gate by execution level; in semi-auto or manual, always ask first.

## Execution Levels

Configure per-agent in `config/SETTINGS.json` (see BEHAVIOR_CONFIG.md):

| Level | Behavior |
|-------|----------|
| Manual | Propose every action; human approves each. |
| Semi-auto | Auto-execute safe ops; ask before destructive ops (delete, force push, system changes). |
| Full-auto | Execute all ops without asking. |

Default in `project/RULES.md` is typically **full-auto**. Override per project or user.

## Safety Gates for Destructive Operations

Before any destructive operation (delete, force push, rewrite, system config change):

1. **Identify** — Classify as destructive.
2. **Check level** — If semi-auto or manual, stop and propose.
3. **Confirm** — Present action, impact, and ask for approval.
4. **Execute** — Only after approval or if full-auto.

## Auto-Execution Control

Legacy "auto-execution mod" is absorbed here. Control behavior via:

- `config/SETTINGS.json` → `execution.level`: `manual` | `semi-auto` | `full-auto`
- `config/SETTINGS.json` → `execution.destructive_gate`: require confirmation for destructive ops (default: true in semi-auto)

Agents read these settings before executing; no separate auto-execution mod.
