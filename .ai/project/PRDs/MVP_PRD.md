---
name: "dotAi MVP -- Chatbot Agent with Local GGUF Model"
overview: "Minimum viable prototype: a single chatbot agent that reads START_HERE.md, uses a local GGUF model via llama-server, follows dotAi conventions, and communicates via jj commits. This PRD is the continuation plan for an AI agent picking up work on this repo."
todos:
  - id: scaffold
    content: "Complete .ai/ directory structure with all skills, references, memories, config, and documentation"
    status: completed
  - id: start-here
    content: "START_HERE.md driver prompt that bootstraps the chatbot agent"
    status: completed
  - id: guide
    content: "GUIDE.md explaining the full dotAi system"
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
    content: "Rename .ai/References/ to .ai/references/ (lowercase) to match the directory convention. Currently tracked as uppercase on git but should be lowercase per GUIDE.md."
    status: pending
  - id: validate-skills
    content: "Read every skill file in .ai/skills/ and verify content is substantive (not just a title). Some skills were written by subagents and may need review. Fix any that are stubs or have placeholder content."
    status: pending
  - id: docker-test
    content: "Test docker-compose.yml: run 'docker-compose up llama-server' with a real GGUF model. Verify OpenAI-compatible API responds at localhost:8080. Document any fixes needed."
    status: pending
  - id: jj-setup
    content: "Install jj (jujutsu) in the dev environment. Initialize jj on the repo with 'jj git init --colocate'. Verify jj operations work alongside git. Document setup in GUIDE.md or a new SETUP.md."
    status: pending
  - id: chatbot-test
    content: "Test the chatbot flow end-to-end: point an AI agent at START_HERE.md, have it read the system, navigate skills, and perform a simple coding task. Record what works and what breaks."
    status: pending
  - id: settings-validation
    content: "Add JSON Schema validation for config/SETTINGS.json. Create .ai/config/SETTINGS.schema.json so agents and tools can validate config programmatically."
    status: pending
  - id: agent-template
    content: "Create a template agent directory at .ai/agents/TEMPLATE/ with AGENT.md and PERSONA.md showing the expected format. This helps new agents bootstrap their own state files."
    status: pending
  - id: memories-bootstrap
    content: "Run system detection and populate project/SYSTEM.md with actual hardware info (OS, arch, GPU, RAM). Update memories/MENTAL_MAP.md with real project patterns after the first agent session."
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
isProject: true
---

# dotAi MVP -- Chatbot Agent with Local GGUF Model

## Context for Continuing Agent

This repo contains the dotAi system -- a declarative, markdown-first AI agent orchestration system. The scaffold is complete (40+ files in `.ai/`). Your job is to pick up the pending tasks above and make the system actually runnable.

**Start by reading:** `.ai/START_HERE.md` -- it links to everything you need.

**Key files to understand the system:**
- `.ai/GUIDE.md` -- full system explanation, directory layout, conventions
- `.ai/project/RULES.md` -- your rules (no guardrails by default)
- `.ai/project/SYSTEM.md` -- runtime environment (populate with real data)
- `.ai/project/BASE_REPO_GUIDELINES.md` -- this repo is both a template and a shared foundation

**What has been done:**
- Complete `.ai/` scaffold: 30+ skills, memory system, config, references (80+ URLs), whitepaper outline
- All old branches cleaned up (epub/library files removed, branches renamed/deleted)
- Merged to main, pushed to GitHub at shahzebqazi/Codex

**What needs doing (priority order):**

### P0 -- Fix Before Anything Else
1. **Case sensitivity fix** -- `.ai/References/` is uppercase but should be `.ai/references/` per convention. Fix this carefully (macOS is case-insensitive).
2. **Validate skills** -- read every skill file, ensure content is real (not stubs from subagent failures).

### P1 -- Make It Runnable
3. **jj setup** -- install jujutsu, init colocated repo, verify it works
4. **Docker test** -- test llama-server with a real GGUF model
5. **Chatbot test** -- end-to-end test of an agent reading START_HERE.md
6. **System detection** -- populate SYSTEM.md with real hardware info

### P2 -- Polish and Harden
7. **Agent template** -- create .ai/agents/TEMPLATE/ with example files
8. **Settings schema** -- JSON Schema for SETTINGS.json
9. **README expansion** -- prerequisites, getting started, troubleshooting
10. **GitHub templates** -- issue templates for agent-generated reports

### P3 -- Release
11. **Basic CI** -- GitHub Actions to validate the scaffold
12. **Version tag** -- tag as template/v0.1.0

## Architecture

```
User
  |
  v
START_HERE.md (reads this first)
  |
  +-> GUIDE.md (system explanation)
  +-> project/RULES.md (constraints)
  +-> project/SYSTEM.md (hardware/runtime)
  +-> skills/ (30+ capability files)
  +-> memories/MENTAL_MAP.md (project knowledge)
  +-> config/SETTINGS.json (settings)
  |
  v
Chatbot Agent (local GGUF via llama-server)
  |
  v
jj commits (communication + audit trail)
```

## Success Criteria

1. A fresh AI agent reading START_HERE.md can understand and work within the system
2. `docker-compose up llama-server` serves a GGUF model at localhost:8080
3. jj is initialized and working alongside git
4. All skill files contain substantive content (no empty stubs)
5. README.md enables a human to go from clone to running in under 5 minutes
6. First version tag (template/v0.1.0) is created
