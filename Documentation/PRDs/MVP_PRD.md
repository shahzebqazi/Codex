---
name: "dotAi MVP -- Chatbot Agent with Local GGUF Model"
overview: "Minimum viable prototype: a single chatbot agent that reads START_HERE.md, uses a local GGUF model via llama-server, follows dotAi conventions, and communicates via jj commits. This PRD is the continuation plan for an AI agent picking up work on this repo."
todos:
  - id: scaffold
    content: "Complete .ai/ directory structure with all tasks (Orchestration/Tasks/), skills (Orchestration/Skills/), references, memories, config, and documentation"
    status: completed
  - id: start-here
    content: "START_HERE.md driver prompt that bootstraps the chatbot agent"
    status: completed
  - id: guide
    content: "README.md (User guide) explaining the full dotAi system"
    status: completed
  - id: whitepaper-outline
    content: "WHITEPAPER.md outline with research notes, references, and templates for human authorship"
    status: completed
  - id: base-repo-guidelines
    content: "BASE_REPO_GUIDELINES.md defining dual role as template and shared foundation"
    status: completed
  - id: branch-cleanup
    content: "Rename openaipluginmsgapi->openai, delete dev/ios-notes/ralphloop, push dotai, fix merge order on main"
    status: completed
  - id: case-sensitivity-fix
    content: "Audit all .ai/ paths for case-sensitivity issues between macOS (insensitive) and Linux/git (sensitive). Ensure no duplicate paths exist. Consider renaming .ai/References/ to .ai/references/ to match convention."
    status: pending
  - id: references-dir-case
    content: "Rename .ai/References/ to .ai/references/ (lowercase) to match the directory convention. Currently tracked as uppercase on git but should be lowercase per README.md User guide."
    status: pending
  - id: validate-skills
    content: "Read every task file in Orchestration/Tasks/ and verify content is substantive (not just a title). Some tasks were written by subagents and may need review. Fix any that are stubs or have placeholder content."
    status: pending
  - id: ollama-mvp-gate
    content: "MVP acceptance test: verify Ollama CLI server works locally as the backend. Gate: MVP ships when this passes."
    status: pending
  - id: docker-test
    content: "Test orchestrator-compose.yml: run 'docker compose -f orchestrator-compose.yml up llama-server' with a real GGUF model. Verify OpenAI-compatible API responds at localhost:8080. Document any fixes needed."
    status: pending
  - id: jj-setup
    content: "Install jj (jujutsu) in the dev environment. Initialize jj on the repo with 'jj git init --colocate'. Verify jj operations work alongside git. Document setup in README.md (User guide) or a new SETUP.md."
    status: pending
  - id: chatbot-test
    content: "Test the chatbot flow end-to-end: point an AI agent at START_HERE.md, have it read the system, navigate tasks (Orchestration/Tasks/) and skills (Orchestration/Skills/), and perform a simple coding task. Record what works and what breaks."
    status: pending
  - id: settings-validation
    content: "Add JSON Schema validation for config/SETTINGS.json. Create .ai/config/SETTINGS.schema.json so agents and tools can validate config programmatically."
    status: pending
  - id: agent-template
    content: "Create a template agent directory at .ai/agents/TEMPLATE/ with AGENT.md and PERSONA.md showing the expected format. This helps new agents bootstrap their own state files."
    status: pending
  - id: memories-bootstrap
    content: "Run system detection and populate memories/system/runtime.md with actual hardware info (OS, arch, GPU, RAM). Update memories/MENTAL_MAP.md with real project patterns after the first agent session."
    status: pending
  - id: readme-setup
    content: "Expand README.md with Prerequisites section (jj, Docker, a GGUF model), step-by-step Getting Started, and Troubleshooting. Align with BASE_REPO_GUIDELINES requirement for clone-to-running-in-minutes."
    status: pending
  - id: version-tag
    content: "Tag the first stable scaffold as template/v0.1.0 per BASE_REPO_GUIDELINES versioning convention."
    status: pending
  - id: ci-basic
    content: "Add a basic GitHub Actions workflow (.github/workflows/validate.yml) that checks: all .ai/ markdown files exist, SETTINGS.json is valid JSON, no case-sensitivity path conflicts."
    status: pending
  - id: github-issues-template
    content: "Create .github/ISSUE_TEMPLATE/ with templates for: bug reports (agent-generated), feature suggestions (agent-generated), and human feature requests. Enable the github.report_bugs_to_issues config path."
    status: pending
  - id: skill-dispatcher-mvp
    content: "Add skill dispatcher to harness: keyword→directory lookup in server.py before LLM call. First token match against Skills/{keyword}/ dirs, inject SKILL.md into context. Minimum: generate and summarize must be deterministically intercepted."
    status: pending
  - id: harness-mode-config
    content: "Add 'mode' field (chat | agent | swarm) to SETTINGS.json and config.py. Chat mode blocks file writes in guard rails. Mode wiring — no full enforcement needed for MVP, but field must exist and be readable."
    status: pending
  - id: session-conversations
    content: "Replace global Conversation() singleton in server.py with session-keyed dict[str, Conversation]. Pass session_id in /api/chat requests. Prevents cross-client state pollution."
    status: pending
  - id: health-endpoint
    content: "Add GET /health to harness server.py returning 200 + JSON with Ollama reachability check. Required for orchestration and headless-mode liveness."
    status: pending
isProject: true
---

# dotAi MVP -- Chatbot Agent with Local GGUF Model

## Context for Continuing Agent

This repo contains the dotAi system -- a declarative, markdown-first AI agent orchestration system. The scaffold is complete (40+ files in `.ai/`). Your job is to pick up the pending tasks above and make the system actually runnable.

**Start by reading:** `.ai/START_HERE.md` -- it links to everything you need.

**Key files to understand the system:**
- `.ai/README.md` (User guide) -- full system explanation, directory layout, conventions
- `.ai/project/RULES.md` -- your rules (no guardrails by default)
- `memories/system/runtime.md`, `system/model_serving.md` -- runtime and model config (populate with real data)
- `.ai/project/BASE_REPO_GUIDELINES.md` -- this repo is both a template and a shared foundation

**What has been done:**
- Complete `.ai/` scaffold: 30+ tasks (in Orchestration/Tasks/), skills (Orchestration/Skills/), memory system, config, references (80+ URLs), whitepaper outline
- All old branches cleaned up (epub/library files removed, branches renamed/deleted)
- Merged to main, pushed to GitHub at shahzebqazi/Codex

**What needs doing (priority order):**

### P0 -- Fix Before Anything Else
1. **Case sensitivity fix** -- `.ai/References/` is uppercase but should be `.ai/references/` per convention. Fix this carefully (macOS is case-insensitive).
2. **Validate tasks** — read every task file in Orchestration/Tasks/, ensure content is real (not stubs from subagent failures).

### P1 -- Make It Runnable
3. **jj setup** -- install jujutsu, init colocated repo, verify it works
4. **Docker test** -- test llama-server with a real GGUF model
5. **Chatbot test** -- end-to-end test of an agent reading START_HERE.md
6. **System detection** -- populate Memories/system/runtime.md with real hardware info

### P2 -- Polish and Harden
7. **Agent template** -- create .ai/agents/TEMPLATE/ with example files
8. **Settings schema** -- JSON Schema for SETTINGS.json
9. **README expansion** -- prerequisites, getting started, troubleshooting
10. **GitHub templates** -- issue templates for agent-generated reports

### P2.5 -- Architecture Runtime (from architecture review)
11. **Skill dispatcher** -- add keyword→directory lookup before LLM call in server.py; intercept `generate` and `summarize` deterministically
12. **Harness mode** -- add `mode` field (chat | agent | swarm) to SETTINGS.json and config.py; chat mode blocks file writes
13. **Session-keyed conversations** -- replace global Conversation() singleton with per-session state
14. **Health endpoint** -- add GET /health to server.py (200 + Ollama reachability)

### P3 -- Release
11. **Basic CI** -- GitHub Actions to validate the scaffold
12. **Version tag** -- tag as template/v0.1.0

## Architecture

```
User
  |
  v
SYSTEM_PROMPT.md (reads this first)
  |
  +-> README.md (User guide; system explanation)
  +-> memories/CONSTRAINTS.md (rules)
  +-> Memories/system/ (runtime, model_serving)
  +-> Orchestration/Tasks/ (30+ tasks), Orchestration/Skills/ (command keywords)
  +-> memories/MENTAL_MAP.md (project knowledge)
  +-> memories/SETTINGS.json (settings, mode)
  |
  v
Harness (Python Flask API)
  |
  +-> Skill Dispatcher (keyword → Skills/{keyword}/ lookup)
  |     ├─ match → inject SKILL.md or invoke callable
  |     └─ no match → pass through
  |
  +-> Mode Guard Rails (chat=text-only, agent=artifacts, swarm=fan-out)
  |
  +-> Session-Keyed Conversations (per-client state)
  |
  v
Chatbot Agent (local GGUF via Ollama or llama-server)
  |
  v
jj commits (communication + audit trail)
```

## Success Criteria

1. **MVP acceptance test:** Ollama CLI server works locally as the backend (primary MVP gate).
2. A fresh AI agent reading START_HERE.md can understand and work within the system
3. `docker compose -f orchestrator-compose.yml up llama-server` serves a GGUF model at localhost:8080
4. jj is initialized and working alongside git
5. All task files in Orchestration/Tasks/ contain substantive content (no empty stubs)
6. README.md enables a human to go from clone to running in under 5 minutes
7. First version tag (template/v0.1.0) is created
