# Model Serving — Local LLM Config and Routing

<!-- AI: Contains subprompts. Model routing is adaptive; see MENTAL_MAP.md for learned performance. -->

## llama-server Configuration

```
endpoint: http://localhost:8080
model: [currently loaded GGUF model]
quantization: [Q4_K_M | Q5_K_M | Q8_0 | F16]
context_length: [model-dependent]
gpu_layers: [auto or manual]
```

## Default Model

Kimi K2.5 (GGUF) — the system default. Any GGUF model loaded in llama-server works.

## Model Routing

| Task Type | Recommended Model | Rationale |
|-----------|-------------------|-----------|
| Code generation | Kimi K2.5 or Qwen 2.5 Coder | Best open coding performance |
| Simple edits (rename, fix typo) | Smallest available model | Minimize latency |
| Screenshot/UI analysis | Moondream2 (1B) | Lightweight vision |
| Architecture planning | Largest available model | Complex reasoning |
| Code review | Mid-size model | Balance speed and quality |

Routing is adaptive — `Memories/MENTAL_MAP.md` tracks which models succeed at which tasks.
