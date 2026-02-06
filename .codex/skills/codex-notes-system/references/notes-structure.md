# Notes Structure

Use this structure to keep the system small and searchable.

## Core Files

- `mindmap.md`
  - Single source of truth for style, symbols, and formatting rules.

- `memories.json`
  - Current preferences and workflow habits.
  - Machine-updatable; validated by schema.

- `mods.json`
  - Resolved configuration from multiple layers (hard, project, user, defaults).
  - Machine-updatable; validated by schema.

- `changelog.md`
  - Short, human-readable log of major changes to the system.

## Templates

- `templates/` holds reusable prompts and logs (TODO, research protocol, studio log, etc.).

## Updates

- Update `mindmap.md` only when you change the rules.
- Update `memories.json` frequently, but keep it factual.
- Update `mods.json` only when workflow directives change.
