---
name: codex-notes-system
description: Organize a personal Codex notes system by splitting a monolithic note into focused files, adding explicit JSON schemas, and resolving conflicting mods into a single effective configuration. Use when migrating prompts/notes, setting up memory and style governance, or standardizing cross-project AI workflows.
---

# Codex Notes System

## Overview

Turn a single, sprawling notes file into a small, structured folder of focused files with explicit schemas and conflict-free configuration.

## Preferred Folder Layout

Create a dedicated folder in the repo, for example `.ai/codex-notes/`, and place these files there:

- `mindmap.md`
- `memories.json`
- `mods.json`
- `templates/`
- `changelog.md`

Use templates in `assets/templates/` as starting points.

## Workflow

1. Extract the source note
- Identify the parts that are core governance (mind map, memory, mods).
- Identify reusable templates (TODO, research protocol, studio log, agenda prompt).

2. Split into focused files
- Move mind map content into `mindmap.md`.
- Convert memory sections into `memories.json` using the schema.
- Resolve mods into a single `mods.json` file.
- Put reusable prompts in `templates/`.

3. Apply schemas
- Validate `memories.json` and `mods.json` against the schemas in `references/schemas/`.
- Keep placeholders in templates, not in the JSON.

4. Resolve mods conflicts
- Use precedence: hard rules > project rules > user rules > defaults.
- Store all layers in `mods.json` and compute `effective` values.
- Never leave conflicting directives in the same layer.

5. Maintain governance
- Update `changelog.md` for any changes that affect workflow or style.
- Keep `memories.json` minimal and factual.

## Conflict Resolution Rules

- Hard rules always win.
- Project rules override user rules.
- User rules override defaults.
- If a value is not defined at a higher precedence, fall back to the next.

## Resources

- `references/notes-structure.md` - File layout and responsibilities.
- `references/mods-policy.md` - Conflict resolution policy.
- `references/schemas/` - JSON schemas for memories and mods.
- `assets/templates/` - Copyable templates to bootstrap the folder.
