# Project

<!-- AI: Read [Orchestration/Harness/SYSTEM_PROMPT.md](Orchestration/Harness/SYSTEM_PROMPT.md) (system prompt / convention) first. Do not use this README as your entry point. -->

This folder holds project-level docs, rules, and the guide for this repo. **Humans:** start here for a quick orientation.

## For humans

- **What this is** — A base/template/plugin repo with a markdown-first AI agent system (dotAi). Everything an agent needs is in markdown; no protocol servers.
- **Live branch** — The **production** branch is the live/deployment branch. Deploys and blue-green releases run from `production` only. All other branches (e.g. main, desktop-app) are not live until merged into production.
- **Orchestration** — A single `Orchestration/` folder at repo root (tasks, harness, memories, agents). CI and deploy use these paths; there is no duplicate under `Project/`.
- **Quick start** — Clone the repo, read the **User guide** below (prerequisites, local model serving, structure). To run a local model: from repo root run `docker compose -f Orchestration/orchestrator-compose.yml up -d llama-server` (API at `http://localhost:8080`).

For license and contributing, see the repo root [LICENSE](../LICENSE) and [CONTRIBUTING](../CONTRIBUTING.md) if present.

---

## User guide

This section covers what this repo is, how the dotAi system works, and how to use it as a base, template, or plugin.

### What this repo is

This repository can serve as a **base repo**, **template**, or **plugin repo** — a scaffold other projects build on. Design decisions support flexible AI and problem-solving workflows.

#### AI and problem-solving directories

You can use this repo (or any downstream project) with **dotai** or similar tooling. AI-facing content can live in any of these; pick one and use it consistently, or specify a custom directory:

- **Common locations:** `.config/ai`, `.ai`, `dotai`, `.Ai`, `AI`, `ai`
- **Project context:** `Project`, `Product`
- **Custom:** Set any directory in config or env and point your tooling at it

No single layout is required.

### What is dotAi?

dotAi is a **markdown-first** AI agent system. Everything in the `.ai/` directory (or your chosen AI directory) is a prompt: no JSON-RPC, no protocol servers — just markdown, version control (e.g. jj), and optional local inference (GGUF via llama-server). Clone a repo, point your agent at the entry file, and go.

#### Philosophy

- **Everything is a prompt** — Tasks, rules, config, memories: markdown for AI to read. In this repo tasks live under `Orchestration/Tasks/` (SWE, VCS, INFRA, etc.).
- **Files are the protocol** — Agents discover capabilities by scanning the filesystem; no servers.
- **VCS commits are the group chat** — Agents coordinate via short jj commit messages to each other and to humans.
- **Declarative over imperative** — Describe what agents should know, not step-by-step how.
- **Local-first** — GGUF via llama-server; no cloud API required.
- **No guardrails by default** — Full agent autonomy unless you set rules.

### Prerequisites

- **Docker** — for llama-server (local GGUF inference)
- **jj (Jujutsu)** — optional; colocated with git for agent commits
- **A GGUF model** — place in `./models/` for local serving

### Quick start (detailed)

1. Clone this repo.
2. Read `Orchestration/Harness/SYSTEM_PROMPT.md` (agent entry), then this README.
3. To run a local model: `docker compose -f Orchestration/orchestrator-compose.yml up -d llama-server` — API at `http://localhost:8080`.

### Local model serving

```bash
# From repo root. Put a GGUF model in Orchestration/models/ (or mount your own).
docker compose -f Orchestration/orchestrator-compose.yml up -d llama-server
```

OpenAI-compatible API at `http://localhost:8080`.

### Directory layout

```
.ai/   (or Project/, .ai, etc. — see “AI and problem-solving directories” above)
  Orchestration/Harness/SYSTEM_PROMPT.md   System prompt / convention for agents
  README.md              This file (user guide)

  project/               Project management
    BASE_REPO_GUIDELINES.md  → see “Base repo guidelines” in this guide
    RULES.md             Agent parameters and constraints
    Memories/system/     Runtime (runtime.md, model_serving.md); Memories/prompts/ (CONTEXT_REFRESH.md)
    PRDs/                Product requirement documents

  Skills/                Command keywords (summarize, generate, …); deterministic effects. See Orchestration/Skills/README.md
  Tasks/                 Task families (SWE, VCS, INFRA, DATA, TOOLS, PM, OS, …); eventually tool calls/scripts/programs. See Orchestration/Tasks/README.md

  agents/                Per-agent state (agents maintain these)
  memories/              Reinforcement and project knowledge
  config/                Settings (gitignored local overrides)
  references/            Cataloged external links
  documentation/         Formal docs
  extensions/            Add-ons
```

**In this repo** the AI content is under `Project/`: rules in `Orchestration/Constraints/RULES.md`, system and config in `Orchestration/Memories/` (system/, MENTAL_MAP.md, DEFAULTS.md, SETTINGS.json), tasks in `Orchestration/Tasks/` (SWE, VCS, INFRA, DATA, TOOLS, PM, OS, etc.), agent patterns in `Orchestration/Agents/Tools/` and `Extensions/`, PRDs in `Project/Product/PRDs/`, references in `Project/Product/References/`. Documents/ contains only a README; all project and user docs live under Project/Product/. See [Orchestration/Harness/SYSTEM_PROMPT.md](Orchestration/Harness/SYSTEM_PROMPT.md) for linked paths.

**Naming:** AI doc filenames are `UPPERCASE.md`; task subdirs under `Orchestration/Tasks/` are UPPERCASE (e.g. `SWE/`, `DATA/`).

### Base repo guidelines

#### As a project template / scaffold

- **Clonable and runnable** — Standalone starting point for new projects.
- **Sensible defaults** — Linting, formatting, CI/CD, directory structure, dependency management.
- **Clear README** — Setup, prerequisites, “Getting started” so clone → run is quick.
- **Placeholder values** — Clearly marked project-specific details (name, env vars, etc.) for easy find-and-replace.
- **Minimal dependencies** — Only what is shared across downstream projects.
- **Version tags** — e.g. `template/v1.0` for stable, template-ready snapshots.

#### As a shared foundation for dependent repos

- **Export shared pieces** — Libraries, utilities, config, conventions as a dependency (not copy).
- **Semantic versioning** — So dependents can pin and upgrade predictably.
- **Stable, documented APIs** — Breaking changes = major version + migration guide.
- **Extension points** — Hooks, overrides, plugin interfaces so downstream can customize without forking.
- **Test suite** — Covers shared functionality for all consumers.
- **Downstream impact** — Consider a compatibility matrix or integration tests before merging.

#### General principles

- **Backward compatibility** — Deprecate before removing.
- **Separation of concerns** — Base = shared only; project-specific stays in downstream.
- **Documentation over convention** — Downstream should not need to read source to use or extend.
- **Lean and opinionated** — Strong, clear foundation over bloated coverage.
- **Automate** — Dependency updates, changelog, version bumps, template validation in CI.

### Subprompts and persistence

Repo files can start with a **recognition comment** so agents know they contain subprompts or task-specific instructions:

- **Markdown:** `<!-- AI: Contains subprompts. Scan for task-specific instructions. Preferences: see PREFERENCES block if present. -->`
- **Code:** `# AI: Contains subprompts. …`

An optional **PREFERENCES** block (in a comment) holds user or project guidance (e.g. “prefer async”, “use library X”). Config in `Orchestration/Memories/SETTINGS.json` is agent-readable and overrides defaults (in this repo).

### VCS and commits

| Who    | Tool | Purpose |
|--------|------|--------|
| Humans | git  | Intentional commits, feature branches, PRs |
| Agents | jj   | Frequent auto-commits, group-chat messages, parallel branches |

Both use the same `.git` directory; jj sits on top of git.

**Humans:** Conventional commits (`feat:`, `fix:`, `docs:`, etc.)

**Agents:** Group-chat style:

```
[agent-name] action summary @mention-if-relevant
```

Keywords: SUCCESS, PIVOT, BLOCKED, CONTINUE

### Agent lifecycle

1. Read `Orchestration/Harness/SYSTEM_PROMPT.md`
2. Load relevant tasks from `Orchestration/Tasks/`
3. Read `Orchestration/Memories/MENTAL_MAP.md` for project context
4. Read task (PRD, issue, or user instruction)
5. Work: edit code, run tests, update files
6. Commit via jj with group-chat message
7. Update memories with learnings
8. Repeat or signal SUCCESS / PIVOT / BLOCKED

#### Budget-aware protocol

Agents track remaining resources and decide:

- **CONTINUE** — Making progress
- **PIVOT** — Try a different approach
- **SUCCESS** — Done, criteria met

#### Context refresh

When context is full:

1. Commit with a descriptive jj message
2. Start fresh with: task PRD + MENTAL_MAP.md + recent jj log
3. Use jj commit history as external memory

#### Self-update

Agents can pull updates from the base repo. Local overrides (if present) are never overwritten (gitignored). `Orchestration/Memories/` and `Orchestration/Agents/` state are preserved; upstream project changes are merged, conflicts surfaced for resolution.

### Future plans

- Codex (OpenAI) agent runtime
- nvim / emacs integration
- Formal SWE-bench evaluation
- Multi-agent Docker orchestration
- RL training on agent trajectories
