# AI System Compatibility Review

**Scope:** dotAi system compatibility with Anthropic, OpenAI, OpenCode, Ollama, LLM Studio, and interfaces (CLI, Desktop, API).  
**Date:** 2025-03-12.

**Human verification:** Even when an item is documented as compatible, a **human must verify** compatibility before release or reliance. See **[PRDs/AI_SYSTEM_COMPATIBILITY_PRD.md](PRDs/AI_SYSTEM_COMPATIBILITY_PRD.md)** for the verification checklist and todos.

---

## Summary

| Provider / Interface | Compatibility | Notes |
|----------------------|---------------|--------|
| **Ollama** | ✅ Supported | Desktop app (Electron) and any client using OpenAI-compatible API |
| **llama-server (GGUF)** | ✅ Primary | Default in config; Docker; OpenAI-compatible at `:8080` |
| **OpenAI (cloud)** | ⚠️ Not implemented | Docs reference Codex; no API key or endpoint config |
| **Anthropic (Claude)** | ⚠️ Not implemented | Docs reference Claude Code; no integration |
| **OpenCode** | 🔜 Future | Placeholder only; no API/CLI wiring |
| **LLM Studio** | ❌ Not referenced | No docs or config |
| **CLI** | ⚠️ Indirect | No dotAi CLI; “Codex agent runtime” in future plans |
| **Desktop** | ✅ Defined | Electron + Ollama + LFM2 (AGENT_PROMPT, FEATURES_PRD) |
| **API** | ✅ OpenAI-shaped | Single `model_endpoint`; OpenAI-compatible `/v1/chat/completions` |

---

## 1. Protocol and Config

- **API shape:** The system assumes an **OpenAI-compatible** chat API:
  - `POST /v1/chat/completions` with `model`, `messages`, optional `stream`, `max_tokens`.
  - Optional: `POST /v1/embeddings` (documented for llama-server).
- **Config:** One endpoint only:
  - `Orchestration/Memories/SETTINGS.json`: `agents.model_endpoint` (default `http://localhost:8080`), `agents.default_model` (e.g. `kimi-k2.5`).
  - No provider type, API keys, or alternate base URLs.
- **Implication:** Anything that exposes an OpenAI-compatible HTTP API can be used by changing `model_endpoint` (and optionally `default_model`). No built-in multi-provider or provider-specific logic.

---

## 2. Provider-by-Provider

### 2.1 Ollama

- **Compatibility:** ✅ **Supported.**
- **Where:** Desktop stack in `Orchestration/Agents/AGENT_PROMPT.md` and `Project/Product/PRDs/FEATURES_PRD.md`.
- **Details:**
  - Base URL: `http://localhost:11434/v1/`.
  - Uses `POST /v1/chat/completions`, streaming, `GET .../api/tags` for model list.
  - Default model: LFM2; model selector in UI.
- **CLI:** Ollama’s own CLI (`ollama run`, `ollama pull`) is the way to run/pull models; dotAi does not implement a separate CLI for Ollama.
- **Desktop:** Electron app is specified to talk to Ollama only (no llama-server in that app).

### 2.2 llama-server (GGUF / llama.cpp)

- **Compatibility:** ✅ **Primary backend** for the repo’s “local model” story.
- **Where:** `Orchestration/orchestrator-compose.yml`, `Orchestration/Memories/system/model_serving.md`, `Orchestration/Tasks/INFRA/LLAMA_CPP.md`, `SETTINGS.json`.
- **Details:**
  - Default endpoint: `http://localhost:8080` (or `http://llama-server:8080` in Docker).
  - OpenAI-compatible chat and embeddings; GBNF/grammar, speculative decoding documented.
- **CLI:** No dotAi CLI; you run `docker-compose up llama-server` (or llama-server binary) yourself.

### 2.3 OpenAI (cloud)

- **Compatibility:** ⚠️ **Not implemented.**
- **Where referenced:** README “Future plans” (Codex agent runtime), WHITEPAPER, References/URLS (Codex CLI, platform docs). No env vars, no API key, no `openai` base URL in config.
- **To support:** Would need: `model_endpoint` (e.g. `https://api.openai.com/v1`), API key (env or config), and possibly provider id (e.g. `openai`) if the code path ever branches by provider.

### 2.4 Anthropic (Claude)

- **Compatibility:** ⚠️ **Not implemented.**
- **Where referenced:** WHITEPAPER, References/URLS (Claude Code, docs). No Anthropic base URL, API key, or request format.
- **Note:** Anthropic’s API is not OpenAI-compatible (different request/response shape). Supporting it would require a dedicated client or adapter (e.g. translate to/from OpenAI format) and config (e.g. `provider: anthropic`, `model`, API key).

### 2.5 OpenCode

- **Compatibility:** 🔜 **Future / placeholder.**
- **Where:** `Project/START_HERE.md` links to `skills/AGENTS/OPENCODE.md` “(future)”; `Project/Extensions/Opencode/OPENCODE.md` is a short placeholder (“document OpenCode-compatible patterns”, “API compatibility notes”).
- **Gap:** No OpenCode CLI integration, no API contract, no wiring in SETTINGS or agent flow. Compatibility is “document when we integrate,” not “works today.”

### 2.6 LLM Studio

- **Compatibility:** ❌ **Not referenced.**
- **Finding:** No mentions of “LLM Studio” or “LlmStudio” in the repo. No docs, no config, no skills.
- **Note:** If “LLM Studio” means a specific product (e.g. LM Studio), then: many such UIs expose an OpenAI-compatible local endpoint (e.g. `http://localhost:1234/v1`). In that case, **compatibility is by API shape**: set `model_endpoint` to that URL and use a model name the server knows. No code changes in dotAi are required; only config and possibly MODEL env for clarity.

---

## 3. Interfaces (CLI, Desktop, API)

### 3.1 CLI

- **Current:** No dotAi-specific CLI. Agents are assumed to run inside Cursor, Codex, Claude Code, or similar; they read markdown (e.g. START_HERE, skills) and use a single configured endpoint.
- **Future:** README lists “Codex (OpenAI) agent runtime” as a future plan—CLI would come from that runtime, not from dotAi itself.
- **Ollama CLI:** Used only to run/pull models for the Ollama-backed Electron app; not part of dotAi’s agent config.

### 3.2 Desktop

- **Current:** One defined desktop app: **Electron + Ollama + LFM2** (OpenCode-style chat). Spec in `AGENT_PROMPT.md` and `FEATURES_PRD.md`. Vanilla HTML/CSS/JS, no React; streaming, model selector, system prompt, guard rails.
- **llama-server:** Not part of this desktop spec; that app is Ollama-only. Using llama-server on the desktop would require a separate client or extending the app to support a second endpoint (e.g. configurable base URL).

### 3.3 API

- **LLM API:** Not implemented as a dotAi service. “API” here means: agents/clients call an **external** OpenAI-compatible endpoint (llama-server or Ollama). Config is `model_endpoint` + `default_model` in SETTINGS.
- **dotai-api:** The Node service under `Orchestration/Tasks/SWE/api/` is a separate product API (health, status, Stripe, etc.). It does not proxy or host LLM calls.

---

## 4. Recommendations

1. **Document “OpenAI-compatible” clearly**  
   In README or a dedicated “Supported backends” section, state that any server offering OpenAI-compatible `POST /v1/chat/completions` (and optional `/v1/embeddings`) works with dotAi by setting `model_endpoint` (and optionally `default_model`). Explicitly mention Ollama, llama-server, and LM Studio–style local UIs if applicable.

2. **LLM Studio / LM Studio**  
   If the project intends to support it: add one line to that “Supported backends” doc (e.g. “LM Studio: set `model_endpoint` to your LM Studio server URL”). No code change if the server is OpenAI-compatible.

3. **OpenCode**  
   When OpenCode has a stable API or CLI: add a skill under `Orchestration/Tasks/AGENTS/` (or Extensions/Opencode) with concrete endpoints, env vars, and wiring; then add a compatibility row to this review.

4. **Anthropic / OpenAI cloud**  
   For cloud providers:
   - **OpenAI:** Add optional env/config for API key and base URL; keep using the same client if it already speaks OpenAI API.
   - **Anthropic:** Add a small adapter (Anthropic request/response ↔ OpenAI-shaped internal format) and config (provider, model, API key) so one “model endpoint” abstraction can point to Claude.

5. **Multi-provider config (optional)**  
   If you want to support several backends at once (e.g. local Ollama + cloud OpenAI), extend SETTINGS to a list of named backends (e.g. `backends: [{ name, endpoint, provider?, api_key? }]`) and document which agent or task uses which backend. Today, single-endpoint is enough for the documented use cases.

---

## 5. References in Repo

- Config: `Orchestration/Memories/SETTINGS.json`
- System/env: `Orchestration/Memories/system/runtime.md`, `system/model_serving.md`
- Local model: `Orchestration/Tasks/INFRA/LLAMA_CPP.md`, `Orchestration/orchestrator-compose.yml`
- Desktop (Ollama): `Orchestration/Agents/AGENT_PROMPT.md`, `Project/Product/PRDs/FEATURES_PRD.md`
- OpenCode: `Project/Extensions/Opencode/OPENCODE.md`, `START_HERE.md` (skills/AGENTS/OPENCODE)
- External refs: `Project/References/URLS.md`, `Project/Product/Papers/WHITEPAPER.md`
