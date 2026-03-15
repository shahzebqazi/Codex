# OpenClaw Gateway Patterns

Agent coordination via single control plane.

Source: https://github.com/openclaw/openclaw

## Architecture

- **Single control plane** — Session routing, coordination.
- **Multi-channel** — Different input channels (chat, API, CLI) route to agents.
- **Docker sandbox per session** — Isolated execution per user/session.

## Agent Communication

- `sessions_send` — Send message to another session.
- `sessions_spawn` — Spawn new agent session.
- Enables agent-to-agent handoff and delegation.

## Markdown-First Prompts

| File | Purpose |
|------|---------|
| AGENTS.md | Agent behavior, coordination rules |
| SOUL.md | Core identity, values |
| TOOLS.md | Available tools, usage |
| SKILL.md | Reusable capabilities |

## Integration

- Align dotAi AGENTS.md, SKILL.md with OpenClaw prompt architecture.
- Sessions map to dotAi project contexts where applicable.
