# Harness — AI Scaffold & Desktop App

This directory holds specs and the **harness implementation**: **Python backend** (Ollama client, guard rails, API) + **Lua GUI** (terminal or IUP). Ollama is the LLM backend.

---

## Stack: Python + Lua GUI + Ollama

| Layer | Role | Location |
|-------|------|----------|
| **Ollama** | Local LLM server | `http://localhost:11434` (run `ollama serve`, `ollama pull lfm2`) |
| **Python harness** | API server, conversation state, guard rails, blocklist | `Harness/python/` |
| **Lua GUI** | Chat UI (terminal or IUP window) | `Harness/lua_gui/` |

---

## Quick start

1. **Ollama:** Install [Ollama](https://ollama.com), run `ollama serve`, then `ollama pull lfm2` (or another model).

2. **Python harness (API server):**
   ```bash
   cd Orchestration/Harness/python
   pip install -r requirements.txt
   python -m harness --port 15555
   ```
   Server listens at `http://127.0.0.1:15555`.

3. **Lua client:**
   - Terminal: `cd Orchestration/Harness/lua_gui && lua client_terminal.lua` (requires `luarocks install luasocket`)
   - IUP GUI: `lua client_gui_iup.lua` (requires `luarocks install iup`)

---

## What the Python harness does

- **Ollama client** — `GET /api/tags` (list models), `POST /v1/chat/completions` (chat, streaming or not).
- **Conversation** — In-memory history; last N messages sent to Ollama (cap 20); in-memory cap 50.
- **Guard rails** — Input length cap (32k chars), response truncation (16k chars), optional blocklist.
- **System prompt** — Loads `Orchestration/Harness/SYSTEM_PROMPT.md` by default (dotAi convention / system prompt); overridable via API.
- **Blocklist** — `Orchestration/Memories/blocklist.txt` (one pattern per line); messages matching are rejected.

**API (for Lua or any client):**

- `GET /api/models` — list models
- `POST /api/chat` — `{"content":"...", "model":"lfm2", "stream": true|false}` → SSE stream or JSON `{ "content": "..." }`
- `POST /api/new_chat` — clear conversation
- `GET /api/system_prompt` / `POST /api/system_prompt` — get/set system prompt
- `GET /api/history` — current messages

---

## Scaffold layer (what agents read)

The **scaffold** is the markdown protocol agents use; it lives alongside the harness:

- Entry: `Orchestration/Harness/SYSTEM_PROMPT.md`
- Rules: `Orchestration/Constraints/RULES.md`
- System / memory: `Orchestration/Memories/system/` (runtime, model_serving), `Memories/prompts/CONTEXT_REFRESH.md`, `MENTAL_MAP.md`
- Tasks: `Orchestration/Tasks/`, `Orchestration/Agents/AGENT_PROMPT.md`
- PRDs: `Project/Product/PRDs/`

See project README and MVP_PRD for scaffold gaps (case sensitivity, task validation, etc.).

---

## Files in this directory

| Path | Purpose |
|------|--------|
| `README.md` | This file |
| `HARNESS_SPEC.md` | Checklist (was Electron-focused; now also Python+Lua) |
| `python/` | Python harness package and HTTP server |
| `lua_gui/` | Lua API client, terminal client, IUP GUI |
| `../Memories/blocklist.txt` | Blocklist patterns (harness loads from here) |

---

## Optional: Electron / other frontends

The original spec described an Electron desktop app. This repo implements **Python harness + Lua GUI** instead. Any frontend that can call the harness HTTP API (e.g. Electron, a web UI, or another Lua toolkit) can reuse the same server.

See also: `Orchestration/Agents/AGENT_PROMPT.md`, `Project/Product/PRDs/FEATURES_PRD.md`, `Project/Product/PRDs/MVP_PRD.md`.
