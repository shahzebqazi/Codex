# Orchestration — README (Orchestrator Agent & Stack)

This README is derived from the comments in `orchestrator-compose.yml`. It describes the Orchestration system and the Orchestrator Agent for the dotAi multi-agent stack.

---

## AI / Subprompts

The compose file and this directory contain subprompts. Scan for task-specific instructions. Project-specific preferences: see the PREFERENCES block in the compose file (edit for your project).

---

## Stack Overview

### llama-server

**Local GGUF model serving via llama.cpp.**

- Provides an **OpenAI-compatible API** at `http://localhost:8080`.
- Load your GGUF model by setting the `MODEL` environment variable.
- Example: `MODEL=/models/kimi-k2.5-Q4_K_M.gguf`
- Default: `MODEL=/models/default.gguf` if unset.
- Volumes: `./models` → `/models` (mount your GGUF files here).
- Command: `--model` (from env), `--host 0.0.0.0`, `--port 8080`, `--ctx-size 8192`, `--n-gpu-layers -1`.
- Restart policy: `unless-stopped`.

### Orchestrator Agent (future)

The Orchestrator is the long-running control plane. When multi-agent is ready, uncomment the `orchestrator` service in `orchestrator-compose.yml`.

- **Build:** `.` (build from Orchestration context).
- **Volumes:** `.:/workspace` (full repo at same path).
- **Environment:**
  - `DOTAI_ROLE=orchestrator`
  - `DOTAI_MODEL_ENDPOINT=http://llama-server:8080`
- **Depends on:** `llama-server` (for model availability).

Until then, the orchestrator is documented in `Tasks/SWE/ORCHESTRATOR.md` and runs outside Docker (e.g. as chatbot or single-agent mode).

---

## Running the Stack

- Start local model only:  
  `docker compose -f orchestrator-compose.yml up -d llama-server`
- When orchestrator service is enabled:  
  `docker compose -f orchestrator-compose.yml up -d orchestrator llama-server`

---

## Related Docs

- **Orchestrator behavior and protocol:** `Tasks/SWE/ORCHESTRATOR.md`
- **Compose lifecycle and multi-agent:** `Tasks/INFRA/DOCKER_COMPOSE.md`
- **System state and model config:** `Memories/system/` (runtime.md, model_serving.md)
