# AI Agent Prompt: dotAi — Electron + Ollama + LFM2

Use this prompt to start an AI agent on the project. **Replace `[FEATURE_LIST]` with the user’s chosen feature numbers (e.g. "2, 4, 7, 10, 12, 15, 20, 21, 23, 25").**

---

## Project brief

**Name:** dotAi  
**Stack:** Electron (desktop), vanilla HTML/CSS/JS (no React), Ollama (local LLM).  
**Purpose:** OpenCode-style coding-assistant chat UI that talks to a local Ollama instance via the OpenAI-compatible API. Default model: LFM2 (configurable). Fully local; no internet required after setup.

**Repo location:** This repo (dotAi project). The codebase was cleared; the agent must recreate the app from scratch and then add the requested features.

---

## Core requirements (implement first)

1. **Electron app**
   - Single window; `main.js` (BrowserWindow, contextIsolation: true, nodeIntegration: false), `preload.js` (expose minimal safe API if needed), load `index.html`.

2. **Ollama integration**
   - Base URL: `http://localhost:11434/v1/`.
   - `POST /v1/chat/completions` with `model`, `messages`, `stream: true`, optional `max_tokens`.
   - Parse streaming response (SSE or NDJSON); support both `choices[0].delta.content` and `message.content`.
   - Error handling: connection refused → "Ollama not running"; 404 → "Model not found. Run: ollama pull <model>".

3. **Chat UI**
   - Message list (user + assistant bubbles), textarea + Send at bottom.
   - Assistant messages: render markdown and fenced code blocks (inline `code`, ``` blocks).
   - Model selector populated from `GET http://localhost:11434/api/tags` (installed models). Prefer selecting `lfm2` if present.
   - Optional system prompt field. Conversation history in memory; send last N messages (e.g. 20) as context to the API.

4. **Guard rails (memory / stability)**
   - Cap context: only last 20 messages sent to Ollama.
   - Cap response length: e.g. `max_tokens: 4096` and stop streaming after ~16k characters; append "[Response truncated to avoid high memory use.]"
   - Cap in-memory conversation: e.g. last 50 messages; when exceeded, drop oldest and remove from DOM.
   - Textarea `maxlength` (e.g. 32k characters).
   - "New chat" button to clear conversation and message list.

5. **Security**
   - CSP in HTML: allow `connect-src` to `http://localhost:11434`.
   - No nodeIntegration in renderer; use preload only for intentional APIs.

6. **Docs**
   - README: prerequisites (Ollama, `ollama pull lfm2`), how to run (`npm install`, `npm start`), usage notes.

**File layout (minimal):**
- `package.json` (main: main.js, scripts: start, dev; devDependency: electron)
- `main.js`, `preload.js`, `index.html`, `renderer.js`, `styles.css`, `README.md`

---

## Feature list to implement

The user has chosen the following features from `FEATURES_1-25.md`. Implement each in a clean, maintainable way; keep the app runnable after each addition.

**Requested feature numbers:** [FEATURE_LIST]

Refer to `FEATURES_1-25.md` for the exact description of each number (e.g. 2 = Conversation history sidebar, 7 = Stop generation, etc.). If a feature overlaps with core requirements (e.g. "New chat" is already in core), implement or refine it as specified in the list.

---

## Execution instructions for the agent

1. Create the project scaffold and implement the **core requirements** above so the app runs and can chat with Ollama (streaming, model selector, system prompt, guard rails, New chat).
2. For each feature number in [FEATURE_LIST], add the corresponding capability from `FEATURES_1-25.md`. Implement in a logical order (e.g. settings persistence and connection settings before tray; stop generation before regenerate).
3. Do not remove or break existing behavior unless the feature explicitly replaces it.
4. Run `npm install` and `npm start` to verify the app launches and can send/receive messages.
5. If the user provided no list, implement only the core requirements and leave a short note that they can add feature numbers later.

---

**End of prompt.** Replace `[FEATURE_LIST]` with the user’s comma-separated list (e.g. `2, 4, 7, 10, 12, 15, 20, 21, 23, 25`) before sending to the agent.
