# Hugging Face Integration

Compatibility stub for Hugging Face LLMs, APIs, benchmarks, skills, and documentation.

## Purpose

- **LLMs**: Use HF-hosted or local models (Transformers, PEFT) for inference; optional routing in dotAi.
- **APIs**: Inference API, Serverless Inference, programmatic access to models and datasets.
- **Benchmarks**: Integrate evaluation runs (e.g. Open LLM Leaderboard, custom benchmarks) with dotAi research protocol and reporting.
- **Skills**: Reuse or mirror HF-focused skills (e.g. dataset viewer, model trainer, jobs) in Orchestration/Skills where relevant.
- **Documentation**: Link to HF docs for model cards, datasets, and best practices; keep references in Documentation/References/URLS.md.

## Config / Environment

- **Token**: `HUGGING_FACE_HUB_TOKEN` or `HF_TOKEN` for gated models and write access (no tokens in repo).
- **Endpoint**: Default Hugging Face API; optional self-hosted or enterprise endpoints.

## Integration Points

- **model_serving.md**: Add HF Inference API or self-hosted endpoints to model routing when used.
- **runtime.md**: List `transformers`, `datasets`, `hf-hub` (or equivalent) in runtime dependencies when adopted.
- **Orchestration/Skills**: Add or link HF-related skills (e.g. “run HF benchmark”, “push model to Hub”) as deterministic commands.
- **Documentation/References/URLS.md**: Central list of HF docs and leaderboard URLs.

## To Populate

- Chosen client (Inference API, `transformers` pipeline, vLLM, etc.).
- Mapping of dotAi task types to HF models or endpoints.
- Benchmark workflow: run config, metrics, and where results are stored (local vs Hub).
- List of HF skills to adopt or reference from Cursor/Codex HF skills.
