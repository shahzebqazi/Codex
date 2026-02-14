# START_HERE -- dotAi System Entry Point

You are an AI agent in the dotAi system. Everything in this `.ai/` directory is a prompt for you. Read this file first, then follow the links below.

## System Overview

dotAi is a declarative, markdown-first agent orchestration system. There are no protocol servers. Files are the protocol. jj commits are the communication bus. You have full autonomy unless rules say otherwise.

Read: [GUIDE.md](GUIDE.md) for the complete system explanation.

## Base Repo Guidelines

This repo is a **base repo** -- both a project template and a shared foundation for dependent repos. All changes must respect both roles.

Read: [project/BASE_REPO_GUIDELINES.md](project/BASE_REPO_GUIDELINES.md)

## Your Rules

Read: [project/RULES.md](project/RULES.md)

Default: no guardrails. You can create, edit, delete, commit, and reorganize anything. Exceptions: architectural decisions require human approval (LEAD_ARCHITECT enforces this). User may add private rules in `config/local/RULES.md`.

## Your Environment

Read: [project/SYSTEM.md](project/SYSTEM.md)

Covers: OS, GPU, RAM, local model config (llama-server), runtime dependencies, Docker state. Update this file when you detect system changes.

## Your Skills

Load skills relevant to your current task:

### Core Roles
- [skills/SWE/ORCHESTRATOR.md](skills/SWE/ORCHESTRATOR.md) -- perpetual coordination agent
- [skills/SWE/LEAD_ARCHITECT.md](skills/SWE/LEAD_ARCHITECT.md) -- planning (requires human approval)
- [skills/SWE/CHATBOT.md](skills/SWE/CHATBOT.md) -- lightweight interactive agent

### Version Control
- [skills/VCS/JJ.md](skills/VCS/JJ.md) -- **READ THIS** -- jj is how you communicate
- [skills/VCS/GIT.md](skills/VCS/GIT.md) -- git conventions for humans

### Infrastructure
- [skills/INFRA/DOCKER_COMPOSE.md](skills/INFRA/DOCKER_COMPOSE.md) -- container management
- [skills/INFRA/LLAMA_CPP.md](skills/INFRA/LLAMA_CPP.md) -- local GGUF model serving

### Software Engineering
- [skills/SWE/PERMISSIONS.md](skills/SWE/PERMISSIONS.md) -- access control
- [skills/SWE/TECHNICAL_WRITER.md](skills/SWE/TECHNICAL_WRITER.md) -- structured logging
- [skills/SWE/XP_PLUS.md](skills/SWE/XP_PLUS.md) -- XP+ methodology
- [skills/SWE/BEHAVIOR_CONFIG.md](skills/SWE/BEHAVIOR_CONFIG.md) -- behavior configuration
- [skills/SWE/AGENT_TRAINING.md](skills/SWE/AGENT_TRAINING.md) -- learning and proficiency

### Data and Methods
- [skills/DATA/MEMORY_MANAGEMENT.md](skills/DATA/MEMORY_MANAGEMENT.md) -- memory system
- [skills/DATA/SYMBOLIC_LANGUAGE.md](skills/DATA/SYMBOLIC_LANGUAGE.md) -- status shorthand
- [skills/DATA/RESEARCH_PROTOCOL.md](skills/DATA/RESEARCH_PROTOCOL.md) -- structured research
- [skills/DATA/PEER_REVIEW.md](skills/DATA/PEER_REVIEW.md) -- review framework
- [skills/DATA/MARKDOWN.md](skills/DATA/MARKDOWN.md) -- markdown conventions
- [skills/DATA/JSON.md](skills/DATA/JSON.md) -- JSON conventions
- [skills/DATA/TODO.md](skills/DATA/TODO.md) -- task management
- [skills/DATA/JOURNAL.md](skills/DATA/JOURNAL.md) -- daily logging

### Tools
- [skills/TOOLS/CURSOR.md](skills/TOOLS/CURSOR.md) -- Cursor IDE
- [skills/TOOLS/AGENDA.md](skills/TOOLS/AGENDA.md) -- agenda generation

### Agent Framework Patterns
- [skills/AGENTS/RALPHY.md](skills/AGENTS/RALPHY.md) -- Ralph-loop perpetual agents
- [skills/AGENTS/OPENCLAW.md](skills/AGENTS/OPENCLAW.md) -- OpenClaw gateway
- [skills/AGENTS/AIDER.md](skills/AGENTS/AIDER.md) -- Aider pair-programming
- [skills/AGENTS/SWE_AGENT.md](skills/AGENTS/SWE_AGENT.md) -- SWE-agent patterns
- [skills/AGENTS/OPENCODE.md](skills/AGENTS/OPENCODE.md) -- OpenCode (future)

### Operating Systems
- [skills/OS/LINUX.md](skills/OS/LINUX.md)
- [skills/OS/MACOS.md](skills/OS/MACOS.md)

### Other
- [skills/WEB.md](skills/WEB.md) -- web (user-defined)

## Your Memory

Read at session start: [memories/MENTAL_MAP.md](memories/MENTAL_MAP.md)

This contains project code style, LSP/linter config, agent performance history, user preferences, and optimization data. Update it as you learn.

Defaults: [memories/DEFAULTS.md](memories/DEFAULTS.md)

## Your Config

Machine-readable settings: [config/SETTINGS.json](config/SETTINGS.json)

Includes: agent email, execution level, model endpoint, GitHub integration flags, budget parameters, self-update settings.

## Current Tasks

Check: [project/PRDs/](project/PRDs/) for active product requirements.

PRDs use Cursor plan.md format (YAML frontmatter with todos).

## References

External links and research: [references/URLS.md](references/URLS.md)

## Bootstrap Protocol

If no agents are running:
1. Read this file (done)
2. Read GUIDE.md for full context
3. Read project/RULES.md and project/SYSTEM.md
4. Read memories/MENTAL_MAP.md
5. Check project/PRDs/ for pending work
6. If no pending work, enter chatbot mode (skills/SWE/CHATBOT.md)
7. If pending work, evaluate whether to start as chatbot or request orchestrator

## Conventions

- All AI docs are UPPERCASE.md
- Commit via jj, not git
- Commit email: ai@dotai.dev (or per config/SETTINGS.json)
- Commit style: `[your-name] action @mention`
- Update this file if the system evolves
- Register yourself in `.ai/agents/<YOUR_NAME>/AGENT.md`
