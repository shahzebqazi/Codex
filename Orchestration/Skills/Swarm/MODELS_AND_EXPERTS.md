# Models and Experts — Mixture-of-Experts and Model Routing

When generating a swarm, assign **task types** to the best available **model or expert** so that each slice of work uses a state-of-the-art or cost-appropriate capability.

## Task type → expert mapping

Use these categories to choose a model (or subagent role). Override per project via `Orchestration/Memories/SETTINGS.json` or a local Swarm override file.

| Task type | Suggested expert role | Rationale |
|-----------|------------------------|-----------|
| **Planning / decomposition** | Reasoning-heavy model (large context, strong chain-of-thought) | Architecture and task graphs need consistency and dependency reasoning. |
| **Code implementation** | Code-specialized or general coding model | Fast iteration, lint/test awareness; can be smaller for simple edits. |
| **Review / QA** | Different model from implementer (fresh “eyes”) | Reduces blind spots; can use same tier or smaller. |
| **Refactor / subtle bugs** | Largest or most capable model available | Hard tasks benefit from scaling (see MENTAL_MAP § Adaptive Compute). |
| **Docs / copy** | Lighter model or generalist | Lower compute; quality matters but scope is bounded. |
| **Research / exploration** | Model with good search/reasoning (e.g. explore subagent) | Multi-step lookup and synthesis. |

## Model keys in swarm config

In [swarm_config.schema.json](swarm_config.schema.json), each task can reference a **model_key**. Define models in the `models` block:

- **default** — Fallback from `SETTINGS.json` or project default (e.g. `model_endpoint`, `default_model`).
- **local** — Local inference (e.g. llama-server, Ollama); use for high-volume or private work.
- **reasoning** — Best available for planning/reasoning (may be same as default or a larger model).
- **code** — Best available for code (may be same as default or a code-specialist).
- **review** — Model used for review/QA (often same as default but distinct from implementer).

Projects can add keys (e.g. **cloud-fast**, **cloud-deep**) and map them in `Orchestration/Memories/SETTINGS.json` or in the swarm config itself.

## State-of-the-art and local-first

- **Local-first:** Prefer `model_endpoint` (e.g. http://localhost:8080) and local model names from SETTINGS.json for all tasks unless the user or PRD explicitly requests a cloud or paid backend.
- **State-of-the-art:** When multiple backends are configured, assign “reasoning” and “code” to the best available per task type; document the choice in the swarm plan so it’s auditable.
- **Budget:** If budget or rate limits are specified (SETTINGS.json or PRD), reserve larger models for hard tasks and use smaller/faster models for simple ones (see MENTAL_MAP § Adaptive Compute Allocation).

## Usage in generator protocol

When building the swarm plan:

1. For each task, set **task_type** (planning, code, review, refactor, docs, research, etc.).
2. Map **task_type** to a **model_key** using the table above and project overrides.
3. Emit **model_key** per task in the swarm config and, in the markdown plan, list which model/expert handles which tasks.
