# BEHAVIOR_CONFIG -- AI Agent Behavior Configuration

## Overview

Agents read behavior config from `config/SETTINGS.json`. Config controls response style, code conventions, execution level, and testing. Absorbs legacy "mods" and "workflow-preferences" concepts.

## Precedence

Higher wins over lower:

1. **Hard rules** — System invariants, safety (never overridden)
2. **Project rules** — `project/RULES.md`, project-specific
3. **User rules** — `.ai/config/local/RULES.md` (gitignored)
4. **Defaults** — Built-in agent defaults

Project and user rules override `config/SETTINGS.json` when conflicting.

## Config Location

Primary: `config/SETTINGS.json`  
Per-project override: `config/<project>/SETTINGS.json` (if present)  
User override: `.ai/config/local/SETTINGS.json` (gitignored)

## Configurable Traits

| Trait | Key | Values | Default |
|-------|-----|--------|---------|
| Response style | `response.style` | `concise`, `verbose`, `balanced` | `concise` |
| Example usage | `response.examples` | `always`, `on_request`, `never` | `on_request` |
| Comment style | `code.comments` | `minimal`, `docblocks`, `inline` | `minimal` |
| Spacing | `code.spacing` | `compact`, `standard`, `loose` | `standard` |
| Paradigm | `code.paradigm` | `functional`, `oop`, `procedural`, `mixed` | project-defined |
| Testing | `testing.required` | `strict`, `moderate`, `optional` | `moderate` |
| VC strictness | `vcs.strictness` | `strict`, `relaxed` | `strict` |
| Execution level | `execution.level` | `manual`, `semi-auto`, `full-auto` | see PERMISSIONS |

## Execution and Permissions

- `execution.level` — See PERMISSIONS.md for manual/semi-auto/full-auto.
- `execution.destructive_gate` — Require confirm for destructive ops (default: true when semi-auto).

## How Agents Use Config

1. On init or task start, read `config/SETTINGS.json`.
2. Merge project/user overrides if present.
3. Apply traits to:
   - Response length and structure
   - Code formatting (comments, spacing)
   - When to add examples
   - Test expectations
   - Whether to ask before destructive ops

## Example `config/SETTINGS.json`

```json
{
  "response": {
    "style": "concise",
    "examples": "on_request"
  },
  "code": {
    "comments": "minimal",
    "spacing": "standard",
    "paradigm": "mixed"
  },
  "testing": {
    "required": "moderate"
  },
  "vcs": {
    "strictness": "strict"
  },
  "execution": {
    "level": "full-auto",
    "destructive_gate": true
  }
}
```
