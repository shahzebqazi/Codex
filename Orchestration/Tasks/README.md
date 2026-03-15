# Task System — Families of Actions

Tasks are **families of actions** the AI performs. Each task belongs to a **family**: a set of related (or disjoint) capabilities. The system mirrors common AI/skills conventions: tasks are the units of work, grouped by domain.

## Task families (current layout)

| Family | Path | Description |
|--------|------|-------------|
| SWE | `Tasks/SWE/` | Software engineering: orchestrator, architect, chatbot, elicitation, permissions, XP+, behavior, agent training, technical writer |
| VCS | `Tasks/VCS/` | Version control: jj, git |
| INFRA | `Tasks/INFRA/` | Infrastructure: Docker, llama-cpp, terraform |
| DATA | `Tasks/DATA/` | Data and methods: memory, symbolic language, research, peer review, markdown, JSON, TODO, journal |
| TOOLS | `Tasks/TOOLS/` | Tooling: Cursor, Agenda |
| PM | `Tasks/PM/` | Product and documentation: documentation automation, requirements elicitation |
| OS | `Tasks/OS/` | Operating systems: Linux, macOS |
| Compliance | `Tasks/Compliance/` | Compliance-related tasks |
| Other | `Tasks/WEB.md` etc. | User-defined or single-file tasks |

Families can be **loosely connected** (e.g. SWE and PM) or **totally disjoint** (e.g. OS and DATA). The AI loads tasks relevant to the current work.

## Current format: markdown

Task definitions today are **markdown files** (e.g. `ORCHESTRATOR.md`, `JJ.md`). They describe roles, capabilities, and behavior for the agent.

## Target format: no .md, executable artifacts

**Goal:** Over time, task definitions should **not** rely on `.md` files. Instead, tasks should be:

- **Tool calls** — Invocations the harness or agent can execute (e.g. via MCP, CLI, or internal API).
- **Scripts** — Small programs (shell, Python, Lua, etc.) that implement the task behavior.
- **Bundled programs** — Binaries or scripts shipped with the harness.

Migration path:

1. Keep existing `.md` task files as **specifications** until an executable artifact exists.
2. Add a script, tool binding, or small program that implements the same behavior.
3. Harness/config prefers the executable when available; falls back to `.md` for description-only.
4. Once every task in a family has an executable form, the `.md` can be removed or kept as doc only.

This aligns the task system with other AI/skills systems where capabilities are invoked as tools or scripts rather than as prose to be "followed."

## Relation to skills

- **Skills** (`Orchestration/Skills/`) = User-typed **command keywords** with deterministic effects (e.g. `summarize`, `generate`). See `Orchestration/Skills/README.md`.
- **Tasks** (`Orchestration/Tasks/`) = **Families of actions** the AI performs; eventually implemented as tool calls, scripts, or bundled programs.

System prompt (convention) for the full system: `Orchestration/Harness/SYSTEM_PROMPT.md`.
