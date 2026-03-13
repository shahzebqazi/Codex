# JSON Conventions

Machine-readable config and data format for dotAi system.

## Use Cases

- `SETTINGS.json` — Global and project settings.
- Memories — Stored in JSON for programmatic update.
- Agent config — Schemas, tool definitions.

## Validation

- Use JSON Schema for validation where available.
- Valid JSON enables reliable agent updates.

## JSON vs YAML

- **Prefer JSON** for settings that agents programmatically update.
- **YAML** only for tool-specific files (e.g., `orchestrator-compose.yml`, CI configs).
- JSON: no ambiguity, easier to patch, widely supported.

## Structure

- Keep flat where possible. Nested objects only when logical.
- Use meaningful keys. Avoid abbreviations unless canonical.
