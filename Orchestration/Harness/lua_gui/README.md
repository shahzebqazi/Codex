# Lua GUI — Harness client

Lua front-end for the Python harness (Ollama backend). Talk to the harness API over HTTP.

## Requirements

- Lua 5.1+ (or LuaJIT)
- **Terminal client:** `luarocks install luasocket` (or system package `lua-socket`)
- **IUP GUI:** Install IUP and IUP Lua bindings (e.g. `luarocks install iup`), then run the GUI script.

## Usage

1. Start the harness server (from repo root or Harness):

   ```bash
   cd Orchestration/Harness/python
   pip install -r requirements.txt
   python -m harness --port 15555
   ```

2. Run the Lua client:

   - **Terminal (no GUI):** `lua client_terminal.lua` — interactive chat in the terminal.
   - **IUP GUI:** `lua client_gui_iup.lua` — window with chat log and input (requires IUP).

## API (Harness server)

- `GET /api/models` → `{ "models": ["..."], "default": "lfm2" }`
- `POST /api/chat` → body `{ "content": "user message", "model": "lfm2", "stream": false }` → `{ "content": "assistant reply" }` (or stream via SSE if `stream: true`)
- `POST /api/new_chat` → clear conversation
- `GET /api/system_prompt` / `POST /api/system_prompt` → get/set system prompt
- `GET /api/history` → `{ "messages": [...] }`

Default server: `http://127.0.0.1:15555`. Override with env `HARNESS_URL`.
