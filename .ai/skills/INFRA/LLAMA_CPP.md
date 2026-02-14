# LLAMA_CPP -- Local GGUF Inference via llama-server

## Overview

Serves GGUF models locally using llama.cpp's `llama-server`. OpenAI-compatible API. Source: https://github.com/ggml-org/llama.cpp

## Build & Run

```bash
# Build llama-server (from llama.cpp repo)
cmake -B build -DGGML_CUDA=ON    # or -DGGML_METAL=ON, -DGGML_VULKAN=ON
cmake --build build --config Release

# Run
./build/bin/llama-server -m /path/to/model.gguf -c 8192 --port 8080
```

## Model Loading

- GGUF format only
- `-m` path to .gguf file
- `-c` context length (default 512, raise for long contexts)
- `--n-gpu-layers` number of layers on GPU (-1 = all)

## Quantization Tradeoffs

| Quantization | Size | Quality | Latency | Use case |
|--------------|------|---------|---------|----------|
| Q4_K_M | Small | Good | Fast | Default, best size/quality |
| Q5_K_M | Medium | Very good | Medium | Higher quality |
| Q8_0 | Large | Near lossless | Slower | When VRAM allows |
| F16 | Largest | Full precision | Slowest | Research, tiny models |

Prefer Q4_K_M for general use. Use Q8_0/F16 only when quality demands it and resources allow.

## API Endpoints

OpenAI-compatible:

- `POST /v1/chat/completions` — chat completion
- `POST /v1/embeddings` — embedding generation

Base URL for agents: `http://localhost:8080` (or `http://llama-server:8080` in Docker).

## Grammar-Constrained Generation (GBNF)

Force valid JSON or structured output:

- Define grammar in GBNF (GGML BNF) format
- Pass `grammar` in the API request
- Use `json-schema-to-grammar.py` to convert JSON schema → GBNF
- Supported in main and server; improves accuracy and eliminates malformed output

## Speculative Decoding

- Use a smaller draft model to propose tokens; main model verifies
- Speeds up generation
- Configure via server flags or examples/speculative

## RPC Backend (Distributed Inference)

- `ggml-rpc` for offloading layers to remote backends
- Useful for CPU + GPU hybrid or multi-node setups
- See `examples/rpc/` in llama.cpp repo

## Model Selection Guide

| Task | Default model | Alternative |
|------|---------------|--------------|
| General | Kimi K2.5 (GGUF) | — |
| Vision (screenshot, UI) | Moondream2 | Small, fast vision |
| Code generation | Qwen 2.5 Coder | Kimi K2.5 |
| Architecture, reasoning | Largest available | — |

Routing is adaptive; `memories/MENTAL_MAP.md` tracks model performance per task type.
