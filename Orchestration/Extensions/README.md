# Extensions — Technology Compatibility

Extensions document integration points between the dotAi system and external tools, APIs, and platforms. Each subfolder holds patterns, config notes, and compatibility status for one technology or family.

## Compatibility Index

| Technology | Extension | Status | Notes |
|------------|-----------|--------|--------|
| **Anthropic API** | [Anthropic/ANTHROPIC.md](Anthropic/ANTHROPIC.md) | Stub | Claude API; model routing, keys, usage. |
| **Anysphere / Graphite** | [Anysphere_Graphite/ANYSPHERE_GRAPHITE.md](Anysphere_Graphite/ANYSPHERE_GRAPHITE.md) | Stub | Cursor (Anysphere), Graphite stacked PRs. |
| **Cursor** | — | In-tree | See [Orchestration/Tasks/TOOLS/CURSOR.md](../Orchestration/Tasks/TOOLS/CURSOR.md). |
| **Hugging Face** | [HuggingFace/HUGGINGFACE.md](HuggingFace/HUGGINGFACE.md) | Stub | LLMs, APIs, benchmarks, skills, docs. |
| **Ollama** | [Ollama/OLLAMA.md](Ollama/OLLAMA.md) | Stub | Local LLM runtime; alternative to llama-server. |
| **OpenAI API** | [OpenAI/OPENAI.md](OpenAI/OPENAI.md) | Stub | Chat, embeddings, usage. |
| **OpenClaw** | [Openclaw/OPENCLAW.md](Openclaw/OPENCLAW.md) | Documented | Gateway, sessions, agent coordination. |
| **OpenCode** | [Opencode/OPENCODE.md](Opencode/OPENCODE.md) | Placeholder | Future open-source coding agent interoperability. |

## Conventions

- One folder per technology or logical family; one primary `UPPERCASE.md` per folder.
- Document: purpose, config/env, model routing (if applicable), and alignment with dotAi (SETTINGS.json, model_serving.md, RULES).
- Stub = integration points described, to be filled as used; Documented = enough to integrate today.

## Cross-references

- **Model routing**: [Orchestration/Memories/system/model_serving.md](../Orchestration/Memories/system/model_serving.md)
- **Runtime / deps**: [Orchestration/Memories/system/runtime.md](../Orchestration/Memories/system/runtime.md)
- **Config**: [Orchestration/Memories/SETTINGS.json](../Orchestration/Memories/SETTINGS.json)
