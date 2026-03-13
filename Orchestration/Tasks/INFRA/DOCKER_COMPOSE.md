# DOCKER_COMPOSE -- Multi-Agent Orchestration

## Overview

Manages the dotAi multi-agent stack: orchestrator, agent workers, and llama-server. The `orchestrator-compose.yml` lives in the **Orchestration** directory (not in .ai or Codex). All agents share the same repo via volume mounts.

## Architecture

```
orchestrator    -- control plane, long-running, manages lifecycle
agent-*         -- task workers, spawned per-task, ephemeral
llama-server    -- local GGUF inference, shared by all agents
```

## Volume Mounts

- **Full repo mount**: Every container gets the entire repo read-write at the same path. Agents and orchestrator operate on the same working tree.
- Mount pattern: `.:/workspace` or project-specific root. Ensures jj, git, and files stay in sync across containers.

## Networking

- All services on the same user-defined bridge network
- llama-server exposed on host (e.g. `8080:8080`) for local tooling
- Agents reach llama-server via service name `llama-server:8080` or `http://llama-server:8080`
- Orchestrator can reach agent containers for health checks

## Lifecycle Management

| Phase | Action |
|-------|--------|
| Start | `docker compose -f orchestrator-compose.yml up -d orchestrator llama-server` (from Orchestration/) |
| Spawn agent | `docker compose -f orchestrator-compose.yml run -d agent-worker -e TASK_ID=...` |
| Stop agent | `docker compose -f orchestrator-compose.yml stop agent-<id>` |
| Full down | `docker compose -f orchestrator-compose.yml down` |

Orchestrator spawns agents based on `project/TASK_GRAPH.md`. Each agent runs in its own container with:
- jj working copy on branch `agent-name/task-id`
- Task PRD and acceptance criteria
- Relevant skill files (via volume)
- LLM endpoint: `http://llama-server:8080`

## Orchestrator Container

- Restart policy: `unless-stopped` (perpetual)
- Depends on: `llama-server` (for model availability)
- Env: project path, jj config, agent limits

## Agent Containers

- No restart policy (ephemeral)
- Same image as orchestrator, different entrypoint/command
- Receive task context via env or mounted file

## Reference

- `orchestrator-compose.yml` in Orchestration/
- ORCHESTRATOR.md for spawn/merge protocol
