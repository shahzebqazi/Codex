# Anthropic API Integration

Compatibility stub for Anthropic (Claude) API in dotAi.

## Purpose

- Call Claude models via Anthropic API for chat, long context, and tool use.
- Optional model routing: use Claude for specific task types (e.g. long docs, safety-sensitive) alongside or instead of local llama-server.

## Config / Environment

- **API key**: `ANTHROPIC_API_KEY` (do not commit; use env or secrets).
- **Endpoint**: Default API; override if using proxy or custom deployment.
- **Models**: e.g. `claude-3-5-sonnet`, `claude-3-opus`; document preferred model per task in model_serving.md when adopted.

## Integration Points

- **SETTINGS.json**: Optional `model_endpoint` override or secondary `anthropic` block for API base + default model.
- **model_serving.md**: Add Claude to model routing table when in use.
- **Agents / scripts**: Any client that calls Anthropic API must read key from environment; no keys in repo.

## To Populate

- SDK or HTTP usage pattern (langchain, direct API, etc.).
- Rate limits and retry policy.
- Mapping of dotAi task types to Claude model names.
