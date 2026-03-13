# Skills — Command Keywords (Deterministic Effects)

Skills are **user-typed commands or keywords** that trigger **deterministic behavior** in the AI harness. They are not capability units or task descriptions; they are literal triggers with well-defined effects.

## Location and convention

- **Directory:** `Orchestration/Skills/`
- **Definition:** Each command has a spec (and eventually a tool call, script, or small program). Specs may be `.md` until replaced by executable artifacts.
- **Trigger:** When the user (or client) sends a message that matches a skill keyword (e.g. `summarize`, `generate`), the harness or agent applies the corresponding effect.

## Registered commands

| Keyword    | Effect |
|-----------|--------|
| **summarize** | Summarize the current context window of the chat so the user can copy-paste it or start a new agent with that summary. No file output unless the user explicitly asks to save. |
| **generate**  | Produce the requested text on screen only. Do not create or overwrite files unless the user explicitly asks for a persisted artifact. |
| **swarm**     | Generate an agent swarm plan for one PRD or all PRDs in parallel. Uses mixture-of-experts/model routing and sub-agent capabilities. See [Orchestration/Skills/Swarm/](Swarm/README.md). |

## Adding a new skill

1. Add a short spec under `Orchestration/Skills/` (e.g. `COMMAND_NAME.md` or a script). For multi-file skills, use a subdirectory (e.g. `Swarm/`).
2. Define: trigger keyword(s), exact effect, and whether output is in-chat only or may write files.
3. Document the command in this README.
4. (Future) Prefer a tool call, script, or small program over a markdown spec so the harness can execute it deterministically.

## Relation to tasks

- **Skills** = command keywords with deterministic, immediate effects (summarize, generate, etc.).
- **Tasks** = families of actions under `Orchestration/Tasks/` (SWE, VCS, INFRA, DATA, TOOLS, PM, OS, etc.). Tasks are units of work the AI performs; they may eventually be implemented as tool calls, scripts, or bundled programs rather than `.md` files.

See `Orchestration/Tasks/README.md` (or project TASK_SYSTEM doc) for the task model and migration path.
