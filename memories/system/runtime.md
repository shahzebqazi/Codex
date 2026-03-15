# Runtime — Hardware and Dependencies

<!-- AI: Contains subprompts. Scan for task-specific instructions. -->
Managed by the orchestrator and chatbot. Agents update these when system state changes.

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
