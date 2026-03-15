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

## File and directory verification (avoid structure hallucinations)

To **check if a directory exists** or list its contents, use **tool calls** — do not rely on docs or context alone. In Cursor the agent typically has:

| Goal | Tool | Usage |
|------|------|--------|
| Directory exists? | **Glob** | `target_directory` = path to dir, `glob_pattern` = `*`. Results returned ⇒ dir exists (and you get contents); empty ⇒ missing or empty. |
| Directory exists? | **run_terminal_cmd** | `test -d "<path>"` (Unix) or `if exist "<path>"` (Windows). Exit code 0 ⇒ exists. |
| List directory contents | **Glob** | Same as above; use returned paths as the list. |
| File exists? | **Read** or **Glob** | **Read** the file (success ⇒ exists); or **Glob** with a pattern matching the file. |

Cursor does not expose a separate `list_dir`; **Glob** with `target_directory` + `glob_pattern` is the way to list a directory or confirm it exists. Full constraint: [memories/prompts/constraints/FILE_STRUCTURE_VERIFICATION.md](../memories/prompts/constraints/FILE_STRUCTURE_VERIFICATION.md).

## MCP Integration

- Model Context Protocol servers extend Cursor with tools.
- Configure in Cursor settings. Use for browser, APIs, filesystem.

## Background Agents

- Long-running tasks: tests, builds, watchers.
- Integrate with agenda, journal for status updates.
