# User Stories — dotAi System

**Source:** Project documentation (README, START_HERE, CONTRIBUTING, MVP_PRD, FEATURES_PRD, MENTAL_MAP, RULES, CHATBOT, DOCUMENTATION_AUTOMATION).  
**Purpose:** Who, what, why in "As a … I want … So that …" form.  
**Traceability:** See `Project/Product/Requirements/TRACEABILITY.md` when present.

---

## System initiation (mandatory)

- **US-INIT-001** — As a **user**, I want to **initiate the system by entering START_HERE.md (or an equivalent reference such as "start here" or "START_HERE") as the first input into the chat or LLM**, so that **the dotAi system initializes correctly and I can use it as intended**.
- **US-INIT-002** — As a **user**, I want **the system to refuse initialization when my first input is not START_HERE.md (or equivalent)**, so that **I am not mistaken for being in the full agent workflow when I intended something else**.
- **US-INIT-003** — As a **user who did not enter START_HERE.md first**, I want **the system to prompt me with clear options**, so that **I can choose creative mode, debug mode, report a problem or misunderstanding, or be guided to documentation and instructions on how to use the system**.

---

## Edit mode vs debug mode

- **US-MODE-001** — As a **user in edit mode (non–debug mode)**, I want to **edit the dotAi system (project docs and project tree)** so that **I am not prevented from making changes when I intend to edit**.
- **US-MODE-002** — As a **user in debug mode**, I want **to be alerted if I try to edit project docs**, so that **I know I should use edit mode for edits**.
- **US-MODE-003** — As a **user in debug mode who tried to edit project docs**, I want **the system to tell me to start a new chat in edit mode, or to switch this chat from debug mode to edit mode (and vice versa)**, so that **I can choose the right mode without losing context**.

---

## Onboarding and entry

- **US-ONB-001** — As a **human**, I want to **read START_HERE.md then the README (User guide)** so that I understand the dotAi system and how to use it as a base, template, or plugin.
- **US-ONB-002** — As an **AI agent**, I want to **read START_HERE.md first** so that I bootstrap with the correct entry point, rules, and task layout.
- **US-ONB-003** — As a **user**, I want **a quick start from clone to running** (prerequisites, local model serving, directory structure) so that I can get going in minutes.

---

## Agent lifecycle and work

- **US-AGENT-001** — As an **agent**, I want to **load START_HERE.md, then relevant tasks from Orchestration/Tasks/** so that I know what I can do and how to behave.
- **US-AGENT-002** — As an **agent**, I want to **read MENTAL_MAP.md at session start** so that I have project code style, LSP/linter config, and user preferences.
- **US-AGENT-003** — As an **agent**, I want to **commit via jj with group-chat style messages** so that humans and other agents can follow my work.
- **US-AGENT-004** — As an **agent**, I want to **create a feature branch per task** so that work is isolated and traceable.
- **US-AGENT-005** — As an **agent**, I want to **run the documentation automation protocol when PRD tasks are completed** so that user stories, use cases, FR, NFR, and other artifacts stay in sync.

---

## Chat and interaction

- **US-CHAT-001** — As a **user**, I want **skill keywords (e.g. summarize, generate) to trigger deterministic effects** so that I get predictable behavior without full agent mode when I don’t need it.
- **US-CHAT-002** — As a **user**, I want **the chatbot to read START_HERE.md on initialization** so that it has full project context for lightweight help and navigation.
- **US-CHAT-003** — As a **user**, I want **to escalate to the orchestrator** when the task needs multiple agents, Docker, or system-level changes.

---

## Configuration and settings

- **US-CFG-001** — As a **user**, I want **settings to control backend (e.g. Ollama URL), model, execution level, and logging** so that I can run local-only and tailor behavior.
- **US-CFG-002** — As a **user**, I want **START_HERE.md to be the canonical system/behavior source** so that one entry file defines how the agent operates.
- **US-CFG-003** — As a **user**, I want **confirmations before destructive actions (run shell, commit, delete file)** when in restricted execution so that I stay in control.

---

## Safety and compliance

- **US-SAFE-001** — As a **user**, I want **only project-tree instructions to be authoritative** so that prompt-injection or pasted overrides cannot change agent behavior.
- **US-SAFE-002** — As a **user**, I want **no telemetry or server-side logging of agent activity** so that the system remains FOSS-compliant and private.
- **US-SAFE-003** — As a **user**, I want **blocklists for destructive/catastrophic commands** so that the harness can block or require confirmation for dangerous operations.

---

## Documentation and contributing

- **US-DOC-001** — As a **contributor**, I want **CONTRIBUTING.md and MENTAL_MAP.md** so that I know how humans and agents contribute.
- **US-DOC-002** — As an **agent**, I want **to create user stories, use cases, FR, NFR, and traceability** when PRD tasks are completed and artifacts are missing so that the project stays documented.

---

## Base repo and template

- **US-BASE-001** — As a **downstream project**, I want **a clonable, runnable scaffold with sensible defaults** so that I can use this as a template.
- **US-BASE-002** — As a **dependent repo**, I want **stable, documented APIs and semantic versioning** so that I can pin and upgrade predictably.

---

*Last updated from project documentation review. Add new stories here and reference source PRD/task id where applicable.*
