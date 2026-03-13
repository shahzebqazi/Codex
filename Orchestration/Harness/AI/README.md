# AI (designated folder in Product)

This directory is the **designated AI folder** under Product. It holds (or points to) documentation for the **AI harness** and related agent-facing product content.

## AI harness documentation

The AI harness is the Python backend + Lua GUI that provides conversation state, guard rails, blocklist, and an HTTP API for chat and model listing. Its specification and implementation live under Orchestration:

| Resource | Location | Description |
|----------|----------|-------------|
| **Harness spec** | [Orchestration/Harness/HARNESS_SPEC.md](../../../Orchestration/Harness/HARNESS_SPEC.md) | Checklist and file layout (Python harness, Lua GUI, Ollama). |
| **Harness README** | [Orchestration/Harness/README.md](../../../Orchestration/Harness/README.md) | Quick start, API, scaffold layer, blocklist. |
| **Lua GUI** | [Orchestration/Harness/lua_gui/README.md](../../../Orchestration/Harness/lua_gui/README.md) | Terminal and IUP clients. |
| **Python package** | `Orchestration/Harness/python/harness/` | Implementation (server, conversation, guard rails, blocklist). |

Use the paths above when referencing harness behavior, API, or run instructions. Project and user documents (PRDs, requirements, prompts) live under **Project/Product/**; Documents/ contains only a README (see [Documents/README.md](../../../Documents/README.md)).
