# dependencies/

External tools, APIs, and runtimes — one `.md` file per dependency. Flat structure (max 2 dirs deep). Install/integration notes and compatibility for system-level or API extensions (Ollama, OpenClaw, ChatGPT, Cursor, etc.).

| File | Purpose |
|------|---------|
| [anthropic.md](anthropic.md) | Anthropic API (Claude) |
| [anysphere.md](anysphere.md) | Anysphere / Graphite (Cursor, stacked PRs) |
| [cursor.md](cursor.md) | Cursor IDE integration |
| [huggingface.md](huggingface.md) | Hugging Face (LLMs, APIs, benchmarks) |
| [ollama.md](ollama.md) | Ollama local LLM runtime |
| [openai.md](openai.md) | OpenAI API |
| [openclaw.md](openclaw.md) | OpenClaw gateway |
| [opencode.md](opencode.md) | OpenCode (future) |

Config for external dependencies lives in `memories/SETTINGS.json` and `memories/system/`.
