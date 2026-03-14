# Harness spec — Python + Lua GUI + Ollama

Use with `Orchestration/Agents/AGENT_PROMPT.md` and `Orchestration/Harness/PRDs/FEATURES_PRD.md`. This stack: **Python harness** (Ollama client, guard rails, skill dispatch, HTTP API) + **Lua GUI** (terminal or IUP).

## Core (MVP)

- [x] **Ollama** — Base URL `http://localhost:11434`; list models (`GET /api/tags`), chat (`POST /v1/chat/completions`, stream or not).
- [x] **Python harness** — Conversation state, context cap (20 msgs), memory cap (50), response truncation (16k chars), input cap (32k).
- [x] **HTTP API** — `/api/models`, `/api/chat`, `/api/new_chat`, `/api/system_prompt`, `/api/history`.
- [x] **Lua client** — Terminal client and IUP GUI calling the API.
- [x] **System prompt** — Load `Orchestration/Harness/SYSTEM_PROMPT.md` (convention) by default; overridable via API.
- [x] **Blocklist** — `Orchestration/Memories/blocklist.txt`; harness rejects messages matching patterns.
- [ ] **Docs** — README in `Harness/` and `Harness/lua_gui/` (done); add run instructions to project README if desired.
- [ ] **Health endpoint** — `GET /health` returning 200 + JSON with Ollama reachability.
- [ ] **Skill dispatcher** — Keyword→directory lookup before LLM call; intercept and enforce skill behavior deterministically.
- [ ] **Harness mode** — `mode` field in SETTINGS.json (chat | agent | swarm); guard rails enforce per-mode constraints.
- [ ] **Session-keyed conversations** — Per-client state isolation via session ID; replaces global singleton.

## Architecture

```
User Input
    │
    ├─ Blocklist check (reject if matched)
    │
    ├─ Skill Dispatcher
    │   ├─ First token matches Skills/{keyword}/ dir?
    │   │   ├─ Callable exists → invoke directly (deterministic)
    │   │   └─ SKILL.md only → inject into system prompt
    │   └─ No match → normal LLM chat
    │
    ├─ Mode Guard Rails
    │   ├─ chat → text-only output, file writes blocked
    │   ├─ agent → artifacts allowed, gated by VCS_AND_FILE_GATE
    │   └─ swarm → fan-out to sub-agents
    │
    ├─ Session-Keyed Conversation (isolated per session_id)
    │
    └─ Ollama / LLM Backend
        └─ Response → truncation → stream to client
```

## File layout

```
Harness/
  README.md
  HARNESS_SPEC.md
  PRDs/
    MVP_PRD.md
    FEATURES_PRD.md
    CODE_REVIEW_HARNESS_AND_API_PRD.md
    AI_SYSTEM_COMPATIBILITY_PRD.md
    HOTKEYS_PRD.md
    README_REWRITE_PRD.md
    REPO_CONSISTENCY_PRD.md
    SESSION_PRD_PROMPT.md
  Documents/
    README.md
  python/
    requirements.txt
    harness/
      __init__.py
      config.py          # Ollama URL, model, guard rails, mode, paths
      ollama_client.py
      guard_rails.py     # Length + mode + skill + budget enforcement
      blocklist.py
      conversation.py
      server.py          # Flask API + skill dispatcher + session mgmt
      __main__.py
  lua_gui/
    README.md
    api.lua
    client_terminal.lua
    client_gui_iup.lua
Orchestration/Memories/
  blocklist.txt
  SETTINGS.json          # Includes mode field (chat | agent | swarm)
Orchestration/Skills/
  manifest.json          # Binds skills to modes, output types, task families
  {keyword}/SKILL.md     # Per-skill definition (or Python callable)
```

## MVP gate

Ollama runs locally; harness server starts; Lua client (terminal or IUP) can list models and chat.

## Post-MVP gates

1. Skill dispatcher intercepts `generate` and `summarize` deterministically.
2. Mode field exists in SETTINGS.json; chat mode blocks file writes.
3. Session-keyed conversations prevent cross-client state pollution.
4. Health endpoint responds at `GET /health`.
