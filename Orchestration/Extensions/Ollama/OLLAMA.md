# Ollama Integration

Compatibility stub for Ollama as a local LLM runtime in dotAi.

## Purpose

- Run local LLMs via Ollama (e.g. Llama, Mistral, Codellama) as an alternative or complement to llama-server.
- Same machine, no cloud; align with dotAi’s local-first model serving.

## Config / Environment

- **Endpoint**: `OLLAMA_HOST` (default `http://localhost:11434`).
- **Models**: Pull and use via `ollama run <model>`; document preferred models in model_serving.md when adopted.
- **Runtime**: Ollama binary; add to runtime.md dependencies when used.

## Integration Points

- **SETTINGS.json**: Optional `model_endpoint` or `ollama` block (e.g. base URL, default model) for agents that call Ollama.
- **model_serving.md**: Add Ollama to model routing table; task-type-to-model mapping.
- **runtime.md**: List `ollama` version and status (running/stopped) alongside llama-server.
- **INFRA**: Optional note in DOCKER_COMPOSE or LLAMA_CPP docs: when to use Ollama vs llama-server (e.g. ease of use vs GGUF flexibility).

## To Populate

- API usage (generate, chat, embeddings if used).
- Recommended models for code, chat, and vision (if supported).
- Decision: Ollama as default local runtime vs llama-server, or both with clear routing.
