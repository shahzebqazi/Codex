# Skills — Command Keywords (Deterministic Effects)

Skills are **user-typed commands or keywords** that trigger **deterministic behavior** in the AI harness. They are not capability units or task descriptions; they are literal triggers with well-defined effects.

## Location and convention

- **Directory:** `Orchestration/Skills/`
- **Definition:** Each command lives in a **one-word directory** (e.g. `negotiation/`, `consensus/`). Inside the directory:
  - **SKILL.md** (or main prompt file): trigger keyword(s), effect, and when to apply.
  - **FUTURE_FEATURES.md**: prompts for additional future features or extensions.
  - **LEARNING.md**: prompts for learning that occurs when the skill runs at scale or across many runs.
- **Trigger:** When the user (or client) sends a message that matches a skill keyword, the harness or agent applies the corresponding effect.

## Registered commands

| Keyword        | Effect |
|----------------|--------|
| **summarize**  | Summarize the current context window of the chat so the user can copy-paste it or start a new agent with that summary. No file output unless the user explicitly asks to save. See [Summarize/](Summarize/). |
| **generate**   | Produce the requested text on screen only. Do not create or overwrite files unless the user explicitly asks for a persisted artifact. See [Generate/](Generate/). |
| **swarm**      | Generate an agent swarm plan for one PRD, all PRDs in parallel, or a workflow (e.g. consolidate PRDs, code review pipeline, release readiness). Uses mixture-of-experts/model routing, sub-agent capabilities, and workflow definitions in [Swarm/Workflows/](Swarm/Workflows/). See [Swarm/](Swarm/). |
| **negotiation**| Resolve conflicts among distributed AI agents via negotiation (progressive negotiation, concessions, facilitator). Goal and resource conflicts. See [negotiation/](negotiation/). |
| **consensus**  | Resolve conflicts by voting or consensus (majority, quorum, BFT-style). Information and decision conflicts among peers. See [consensus/](consensus/). |
| **priority**   | Resolve conflicts by priority or authority rules (arbiter, role hierarchy, policy overrides). Predictable, low-latency, safety-critical. See [priority/](priority/). |
| **auction**    | Resolve resource contention via auction or market mechanisms (bids, clearing, allocation). See [auction/](auction/). |
| **argumentation** | Resolve information conflicts via belief merging or argumentation frameworks (arguments, attacks, accepted conclusions). See [argumentation/](argumentation/). |

## Conflict-resolution skills (distributed AI)

The skills **negotiation**, **consensus**, **priority**, **auction**, and **argumentation** implement five strategies for resolving conflicts in distributed AI systems. Each has:

- **SKILL.md** — main prompt (trigger, effect, when to apply)
- **FUTURE_FEATURES.md** — prompts for designing additional features
- **LEARNING.md** — prompts for learning when the skill scales or runs repeatedly

## Adding a new skill

1. Create a **one-word directory** under `Orchestration/Skills/` (e.g. `mycommand/`).
2. Add **SKILL.md** (or main prompt file): trigger keyword(s), exact effect, and when to apply. Optionally add **FUTURE_FEATURES.md** and **LEARNING.md** for extensions and scaling/learning.
3. Register the command in the table above in this README.
4. (Future) Prefer a tool call, script, or small program over a markdown spec so the harness can execute it deterministically.

## Relation to tasks

- **Skills** = command keywords with deterministic, immediate effects (summarize, generate, swarm, negotiation, consensus, etc.).
- **Tasks** = families of actions under `Orchestration/Tasks/` (SWE, VCS, INFRA, DATA, TOOLS, PM, OS, etc.). Tasks are units of work the AI performs; they may eventually be implemented as tool calls, scripts, or bundled programs rather than `.md` files.

See `Orchestration/Tasks/README.md` (or project TASK_SYSTEM doc) for the task model and migration path.
