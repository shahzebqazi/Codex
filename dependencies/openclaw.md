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

## Plugin compatibility (this repo)

**This repo is not an OpenClaw plugin** in the formal sense. It is **content-compatible**: you can use dotAi markdown (AGENTS.md, SKILL.md, rules, prompts) with OpenClaw by placing or referencing them in your workspace; OpenClaw does not require a plugin for that.

OpenClaw’s plugin system expects ([docs](https://docs.openclaw.ai/plugin)):

1. **Manifest** — `openclaw.plugin.json` in the plugin root with `id`, `configSchema`; optional `skills` (array of skill dirs).
2. **Runtime module** — A TypeScript/JavaScript entry (e.g. `index.ts`) that exports `default function(api)` and registers tools, skills, or other capabilities via `api.registerTool()`, etc. Plugins run in-process with the Gateway.
3. **Discovery** — Plugin is under `plugins.load.paths`, `~/.openclaw/extensions/`, or `/.openclaw/extensions/` (workspace), or installed via `openclaw plugins install <package>`.

This repo (cursor-killer) is markdown-first plus a Python harness; it has no `openclaw.plugin.json` and no plugin entry module. The **openclaw-zergrush** repo is prompts-only (`.md` + `.json` schemas); it too has no manifest or entry. So neither is loadable as an OpenClaw plugin today.

To make a repo **plugin-compatible** you would: add `openclaw.plugin.json`, add a minimal JS/TS entry that exports `register(api)` (and optionally list `skills` in the manifest pointing at your prompt dirs), and distribute as a package or place the plugin root in one of the discovery paths above.
