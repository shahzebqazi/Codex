# GUIDE -- The dotAi System

## What is dotAi?

dotAi is a declarative, markdown-first alternative to protocol-based agent orchestration systems like MCP. Everything in the `.ai/` directory is a prompt. There are no protocol servers, no JSON-RPC, no runtime negotiation. AI agents read markdown files to understand their capabilities, communicate through jj (Jujutsu) commits, and persist knowledge in memory files.

## Philosophy

**Everything is a prompt.** Skills, rules, configurations, memories, references -- all markdown documents designed for AI agents to read.

**Files are the protocol.** Agents discover capabilities by scanning `.ai/skills/`. No servers needed.

**VCS commits are the group chat.** Agents communicate via brief jj commit messages addressed to each other and to humans.

**Declarative over imperative.** Describe what agents should know, not step-by-step how to do it.

**Local-first.** GGUF models via llama-server. No cloud API dependency required.

**No guardrails by default.** Agents have full autonomy unless the user sets rules.

## Directory Layout

```
.ai/
  START_HERE.md          Entry point -- bootstraps any agent
  GUIDE.md               This file -- system explanation

  project/               Project management
    RULES.md             Agent parameters and constraints
    SYSTEM.md            Runtime environment (OS, GPU, models, deps)
    PRDs/                Product requirement documents (plan.md format)

  skills/                Agent capabilities organized by domain
    SWE/                 Software engineering roles
      ORCHESTRATOR.md    Perpetual coordination agent
      LEAD_ARCHITECT.md  Planning agent (requires human approval)
      CHATBOT.md         Lightweight interactive agent
      PERMISSIONS.md     Access control and execution safety
      TECHNICAL_WRITER.md  Structured logging
      XP_PLUS.md         Modified XP for AI-solo development
      BEHAVIOR_CONFIG.md Behavior configuration with precedence
      AGENT_TRAINING.md  Active learning and proficiency tracking
    VCS/                 Version control
      GIT.md             Git conventions for humans
      JJ.md              Jujutsu conventions for AI agents
    OS/                  Operating systems
      LINUX.md
      MACOS.md
    INFRA/               Infrastructure
      DOCKER_COMPOSE.md  Container orchestration
      LLAMA_CPP.md       Local GGUF model serving
    DATA/                Data formats and methods
      MARKDOWN.md        Markdown conventions
      JSON.md            JSON conventions
      MEMORY_MANAGEMENT.md  Memory system operations
      SYMBOLIC_LANGUAGE.md  Universal status/priority shorthand
      RESEARCH_PROTOCOL.md  Structured research methodology
      PEER_REVIEW.md     Review framework
      TODO.md            Task management format
      JOURNAL.md         Daily logging
    TOOLS/               Tool-specific skills
      CURSOR.md          Cursor IDE integration
      AGENDA.md          Intelligent agenda generation
    AGENTS/              External agent framework patterns
      RALPHY.md          Ralph-loop perpetual agents
      OPENCLAW.md        OpenClaw gateway patterns
      OPENCODE.md        OpenCode (placeholder)
      AIDER.md           Aider pair-programming patterns
      SWE_AGENT.md       SWE-agent ACI patterns
    WEB.md               Web skill (user-defined)

  agents/                Per-agent state (agents maintain these)
    <NAME>/
      AGENT.md           Current state, status, task, branch
      PERSONA.md         Project-specific identity

  documentation/         Formal documents
    WHITEPAPER.md        CS research paper (draft/outline)

  references/            External resources catalog
    URLS.md              Master index (80+ links)
    ralph/               Ralph loop patterns
    agents/              Coding agent framework summaries
    models/              GGUF model references
    research/            RL scaling paper summaries
    docs/                Technology documentation links

  extensions/            Vetted add-ons not in base repo

  memories/              Agent knowledge and RL data
    MENTAL_MAP.md        Project knowledge, code style, performance tracking
    DEFAULTS.md          System-wide default preferences

  config/                Settings
    SETTINGS.json        Project-level config (JSON for machine access)
    local/               Gitignored -- user-specific private overrides
      RULES.md           Hidden rules
      MEMORIES.md        Private memories
```

## Naming Conventions

- **All AI doc filenames are UPPERCASE.md** (e.g. `START_HERE.md`, `RULES.md`)
- Directories are lowercase (e.g. `skills/`, `project/`)
- Skill subdirectories are UPPERCASE (e.g. `SWE/`, `VCS/`, `DATA/`)

## VCS Split

| Who | Tool | Purpose |
|---|---|---|
| Humans | git | Intentional commits, feature branches, PRs |
| AI agents | jj | Frequent auto-commits, group-chat messages, parallel branches |

Both operate on the same `.git` directory. jj is a layer on top of git.

## Commit Message Style

**Humans:** Conventional commits (`feat:`, `fix:`, `docs:`, etc.)

**AI agents:** Group-chat style:
```
[agent-name] action summary @mention-if-relevant
```

Special keywords: SUCCESS, PIVOT, BLOCKED, CONTINUE

## Agent Lifecycle

1. Agent reads `START_HERE.md`
2. Agent loads relevant skills from `skills/`
3. Agent reads `memories/MENTAL_MAP.md` for project context
4. Agent reads task (PRD, issue, or user instruction)
5. Agent works: edit code, run tests, update files
6. Agent commits via jj with group-chat message
7. Agent updates memories with learnings
8. Repeat from step 4 or signal SUCCESS/PIVOT/BLOCKED

## Budget-Aware Protocol

From BATS research -- agents track remaining resources and decide:
- **CONTINUE** -- making measurable progress, keep going
- **PIVOT** -- not working, try a different approach
- **SUCCESS** -- done, acceptance criteria met

## Context Refresh

When context fills up:
1. Commit current work with descriptive jj message
2. Start fresh with: task PRD + MENTAL_MAP.md + recent jj log
3. jj commit history = external memory

## Self-Update

Agents can pull updates from the base repo (`shahzebqazi/Codex`):
- `config/local/` is never overwritten (gitignored)
- `memories/` agent files preserved via jj conflict-as-data
- `agents/` state files preserved
- Upstream `.ai/` changes merged, conflicts surfaced for resolution

## Future Plans

Skills planned but not yet implemented:
- Codex (OpenAI) agent runtime
- nvim integration
- emacs integration
- Formal SWE-bench evaluation
- Multi-agent Docker orchestration
- Formal RL training on agent trajectories
