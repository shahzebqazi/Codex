# SYSTEM -- Runtime Environment Configuration

<!-- AI: Contains subprompts. Scan for task-specific instructions. Preferences: see PREFERENCES block if present. -->
<!-- PREFERENCES (edit for your project): -->
<!-- (none) -->

Managed by the orchestrator and chatbot. Agents update this file when system state changes.

## Hardware Detection

```
os: [auto-detect on init]
arch: [auto-detect: x86_64, arm64, etc.]
cpu: [auto-detect]
ram_total: [auto-detect]
ram_available: [auto-detect after drivers/deps]
gpu: [auto-detect]
gpu_driver: [Metal | ROCm | CUDA | Vulkan | CPU-only]
vram: [auto-detect if GPU present]
```

## Local Model Serving

### llama-server Configuration

```
endpoint: http://localhost:8080
model: [currently loaded GGUF model]
quantization: [Q4_K_M | Q5_K_M | Q8_0 | F16]
context_length: [model-dependent]
gpu_layers: [auto or manual]
```

### Default Model

Kimi K2.5 (GGUF) -- the system default. Any GGUF model loaded in llama-server works.

### Model Routing

| Task Type | Recommended Model | Rationale |
|---|---|---|
| Code generation | Kimi K2.5 or Qwen 2.5 Coder | Best open coding performance |
| Simple edits (rename, fix typo) | Smallest available model | Minimize latency |
| Screenshot/UI analysis | Moondream2 (1B) | Lightweight vision |
| Architecture planning | Largest available model | Complex reasoning |
| Code review | Mid-size model | Balance speed and quality |

Routing is adaptive -- `memories/MENTAL_MAP.md` tracks which models succeed at which tasks.

## Runtime Dependencies

```
jj: [version, install path]
git: [version]
docker: [version, running: yes/no]
docker-compose: [version]
bun: [version, if present]
node: [version, if present]
python: [version, if present]
llama-server: [version, running: yes/no, pid]
```

## Docker State

```
orchestrator: [running/stopped, container id]
llama-server: [running/stopped, container id, loaded model]
active_agents: [list of running agent containers]
```

## Context Refresh

Agents should refresh their context efficiently:
1. Read `START_HERE.md` for system overview (cached after first read)
2. Read task-specific PRD and acceptance criteria
3. Read `memories/MENTAL_MAP.md` for project patterns
4. Read relevant skill files for the current task
5. Minimize re-reading unchanged files between iterations
