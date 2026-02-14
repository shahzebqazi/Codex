# Cursor IDE Skill

Integration patterns for Cursor IDE in dotAi workflows.

## Cursor Rules

- Location: `.cursor/rules/`
- One rule per file. Loaded automatically.
- Use for project-specific conventions, style, constraints.

## Cursor Plans

- File: `plan.md`
- YAML frontmatter for metadata.
- Todos in same format as TODO.md for interoperability.

## Modes

- **Agent** — Full tool access, edits, multi-step execution.
- **Ask** — Read-only, Q&A.
- **Plan** — Planning without execution. Output plan.md.

## Profiles

| Profile | Use case |
|---------|----------|
| Default | General editing, balanced |
| Expert | Minimal guidance, fast iteration |
| Mentor | Detailed explanations, teaching |
| Rapid | Quick fixes, minimal context |

Auto-switch based on context: new project → Mentor; familiar codebase → Expert; small fix → Rapid.

## MCP Integration

- Model Context Protocol servers extend Cursor with tools.
- Configure in Cursor settings. Use for browser, APIs, filesystem.

## Background Agents

- Long-running tasks: tests, builds, watchers.
- Integrate with agenda, journal for status updates.
