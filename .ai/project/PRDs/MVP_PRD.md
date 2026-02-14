---
name: "dotAi MVP -- Chatbot Agent with Local GGUF Model"
overview: "Minimum viable prototype: a single chatbot agent that reads START_HERE.md, uses a local GGUF model via llama-server, follows dotAi conventions, and communicates via jj commits."
todos:
  - id: scaffold
    content: "Create complete .ai/ directory structure with all skills, references, memories, config, and documentation"
    status: completed
  - id: start-here
    content: "Write START_HERE.md driver prompt that bootstraps the chatbot agent"
    status: in_progress
  - id: guide
    content: "Write GUIDE.md explaining the full dotAi system"
    status: in_progress
  - id: docker
    content: "Create docker-compose.yml with orchestrator and llama-server services"
    status: pending
  - id: gitignore
    content: "Update .gitignore for config/local/, .env, models/, *.gguf"
    status: pending
  - id: readme
    content: "Write human-facing README.md"
    status: pending
  - id: push
    content: "Push dotai branch to GitHub"
    status: pending
  - id: branch-ops
    content: "Rename openaipluginmsgapi to openai, delete dev/ios-notes/ralphloop branches"
    status: pending
isProject: false
---

# dotAi MVP -- Chatbot Agent with Local GGUF Model

## Summary

The MVP demonstrates the core dotAi thesis: a single markdown file (START_HERE.md) can bootstrap a fully functional AI coding agent without any protocol server, using only files as the protocol and VCS commits as communication.

## Problem Statement

Current AI coding agent systems require protocol servers (MCP), proprietary configurations, and runtime dependencies to coordinate agents. This creates unnecessary complexity for the common case: a developer who wants an AI assistant that understands their project and can code autonomously.

## Goals

- Demonstrate that a `.ai/` directory with markdown prompts is sufficient to configure an AI coding agent
- Show that a local GGUF model (Kimi K2.5) via llama-server can power autonomous coding
- Establish the jj-as-communication-bus pattern for agent VCS operations
- Provide a complete, documented scaffold that others can clone and use
- Validate the "everything is a prompt" philosophy

## Non-Goals

- Multi-agent orchestration (future: requires Docker Compose agent spawning)
- Formal RL training (future: requires compute and training data)
- IDE-specific integrations beyond Cursor plan.md compatibility
- Production deployment or scaling

## Success Criteria

1. An AI agent reading START_HERE.md can understand the full dotAi system
2. The agent can navigate the .ai/ directory and invoke skills
3. The agent can use jj for commits with group-chat style messages
4. The agent can read/update memories and config
5. The docker-compose.yml can start llama-server with a GGUF model
6. The complete scaffold is pushed to GitHub on the dotai branch

## Architecture

```
User
  |
  v
START_HERE.md (driver prompt)
  |
  +-> GUIDE.md (system explanation)
  +-> project/RULES.md (constraints)
  +-> project/SYSTEM.md (runtime config)
  +-> skills/ (capabilities)
  +-> memories/MENTAL_MAP.md (project knowledge)
  +-> config/SETTINGS.json (settings)
  |
  v
Chatbot Agent (reads all above, uses llama-server for inference)
  |
  v
jj commits (communication, audit trail)
```

## User Stories

### US-001: Agent Bootstrap
As a developer, I want to point an AI agent at START_HERE.md and have it understand the entire dotAi system so that it can begin working on my project immediately.

**Acceptance Criteria:**
- START_HERE.md links to all essential files
- An agent reading it can describe the system accurately
- No external documentation needed

### US-002: Local Model Inference
As a developer, I want to run a local GGUF model via docker-compose so that I have AI inference without cloud API dependencies.

**Acceptance Criteria:**
- `docker-compose up llama-server` starts serving
- OpenAI-compatible API available at localhost:8080
- Any GGUF model works (Kimi K2.5 default)

### US-003: Agent Communication via jj
As a developer, I want AI agents to commit via jj with brief group-chat messages so that I can follow agent activity in the commit log.

**Acceptance Criteria:**
- Commits use `ai@dotai.dev` email
- Messages follow `[agent-name] action @mention` format
- jj revsets can query agent activity

### US-004: Self-Update without Data Loss
As a developer, I want to pull updates from the base repo without losing my agent's memories or local config.

**Acceptance Criteria:**
- `config/local/` is gitignored
- `memories/` agent-created files survive updates
- jj conflict-as-data preserves modifications

## Risks

- jj adoption is early; some developers may not have it installed -> mitigate with install instructions in GUIDE.md
- GGUF model quality varies -> mitigate with model routing guide in SYSTEM.md
- Docker dependency for llama-server -> mitigate with bare-metal install instructions in LLAMA_CPP.md skill
