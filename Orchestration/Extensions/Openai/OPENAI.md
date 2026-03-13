# OpenAI API Integration

Compatibility stub for OpenAI API access in dotAi.

## Purpose

- Call OpenAI models (GPT-4, GPT-4o, o1, etc.) for chat, reasoning, and embeddings.
- Optional model routing: use OpenAI for specific task types (e.g. complex reasoning, embeddings) alongside local or other providers.

## Config / Environment

- **API key**: `OPENAI_API_KEY` (do not commit; use env or secrets).
- **Endpoint**: Default API; override for Azure OpenAI or proxy (e.g. `OPENAI_BASE_URL`).
- **Models**: e.g. `gpt-4o`, `gpt-4o-mini`, `o1`; document in model_serving.md when adopted.

## Integration Points

- **SETTINGS.json**: Optional secondary `openai` block (base URL, default model) for agents that call OpenAI.
- **model_serving.md**: Add OpenAI models to model routing table when in use.
- **Agents / scripts**: All clients must read key from environment; no keys in repo.

## To Populate

- SDK or HTTP usage pattern (openai package, direct API).
- Rate limits, retries, and cost considerations.
- Mapping of dotAi task types to OpenAI model names.
- Embeddings usage and vector store conventions if used.
