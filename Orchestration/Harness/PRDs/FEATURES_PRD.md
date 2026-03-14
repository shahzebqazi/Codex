---
name: "Features PRD — dotAi (Electron + Ollama + LFM2)"
overview: "Product requirements for the AI coding harness desktop app: settings menu, chat, models, UI. Includes MVP decisions and deferred items for refinement."
todos:
  - id: settings-endpoint-config
    content: "Refine: Model endpoint configuration (per-provider base URL/path) in Settings PRD"
    status: pending
  - id: settings-api-keys
    content: "Refine: API keys & secrets management in Settings PRD (when cloud/paid backends added)"
    status: pending
  - id: settings-execution-level
    content: "Refine: Execution level / autonomy beyond two presets (restricted, full access)"
    status: pending
  - id: settings-budget
    content: "Refine: Budget / rate limits in Settings PRD (when non-local backends supported)"
    status: pending
  - id: settings-self-update
    content: "Refine: Self-update behavior in Settings PRD; for now recommend nightly (development) branch"
    status: pending
  - id: settings-jj-memory
    content: "Determine solution: JJ + local project memory + JJ commits + dotai GitHub repo integration"
    status: pending
  - id: settings-file-allow-blocklist
    content: "Refine: File and path allowlist/blocklist in Settings PRD (deferred for MVP)"
    status: pending
  - id: settings-blocklist-memories
    content: "Add blocklists for destructive/catastrophic commands in Orchestration/Memories"
    status: pending
  - id: hotkeys-prd
    content: "Create HOTKEYS_PRD.md and determine all hotkeys and default mappings"
    status: pending
  - id: feature-41
    content: "Settings persistence & config source: where UI settings stored, relationship to SETTINGS.json"
    status: pending
  - id: feature-42
    content: "Backend URL (Ollama host/port): user-configurable base URL, default localhost:11434"
    status: pending
  - id: feature-43
    content: "Max output tokens: user-configurable per response, optional per-persona default"
    status: pending
  - id: feature-44
    content: "Confirm before destructive actions: run shell, commit, delete/move file"
    status: pending
  - id: feature-45
    content: "Default Personality on startup: last used, fixed, or ask each time"
    status: pending
  - id: feature-46
    content: "Local-only / no external requests: user-facing checkbox or statement"
    status: pending
  - id: feature-47
    content: "Session restore on reopen: restore last session or always start fresh"
    status: pending
  - id: feature-48
    content: "Config path override: optional path to SETTINGS.json or config directory"
    status: pending
  - id: feature-49
    content: "Agent hierarchies: parent/child agent roles, delegation, escalation, and policy inheritance"
    status: pending
  - id: feature-50
    content: "Ancient quorums: configurable quorum approvals for high-risk agent actions and multi-agent decisions"
    status: pending
  - id: feature-51
    content: "Agent/task sandboxing: isolation boundaries for filesystem, network, and command execution"
    status: pending
  - id: feature-52
    content: "Per-agent/per-task virtual environments for isolated dependencies and reproducible execution"
    status: pending
  - id: feature-53
    content: "JJ failure-event schema and startup handoff so new agents inherit unresolved failures and validated lessons"
    status: pending
  - id: feature-54
    content: "Hybrid memory pipeline: short-term JJ signals with curated promotion into long-term project memory"
    status: pending
  - id: feature-55
    content: "Strict Ancient Quorum gate for promoting lessons into durable memory (supermajority with critical-path rules)"
    status: pending
  - id: feature-56
    content: "Harness performance optimization from failure learning: avoidance hits, rework reduction, recovery-time metrics"
    status: pending
  - id: feature-57
    content: "Layered memory system with file/dir facts layer and knowledge base: research and solution per LAYERED_MEMORY_AND_KNOWLEDGE_BASE_PRD.md; harness eliminates file-knowledge misalignment and hallucination via KB and tool verification"
    status: pending
  - id: research-haskell-input-filter
    content: "Research: determine feasibility of a Haskell-based input filtering program that classifies and intercepts prompts before they reach the LLM — blocks malicious prompts (criminal intent, illegal activities, abusive/dangerous content) and diverts junk/trivial prompts (e.g. 'hi', 'hello', meaningless inputs) to static responses instead of wasting AI inference cycles"
    status: pending
  - id: design-static-response-map
    content: "Design: define a static response map for trivial/junk prompts (greetings, empty inputs, gibberish) that returns deterministic messages (e.g. 'hi' → VCS status or onboarding message) without invoking the LLM, saving energy and compute"
    status: pending
  - id: research-haskell-prompt-taxonomy
    content: "Research: build a prompt classification taxonomy for the Haskell filter — categories: (1) malicious/criminal, (2) illegal activity, (3) abusive/dangerous, (4) trivial/greeting, (5) off-topic/useless, (6) valid — with examples, false-positive analysis, and escalation rules"
    status: pending
  - id: research-haskell-perf-safety
    content: "Research: evaluate Haskell's suitability for the input filter — parse performance, type-safety guarantees, pattern matching for prompt classification, integration path with the Electron/Ollama harness (FFI, subprocess, or microservice), and comparison with alternatives (Rust, OCaml, plain regex)"
    status: pending
  - id: arch-skill-dispatcher
    content: "Skill dispatcher: add keyword→directory lookup in harness between input reception and LLM call. Match first token of user input against Orchestration/Skills/{keyword}/ directories. When matched, load SKILL.md (or callable) and inject into system prompt or prepend to messages. Turns skill convention into harness-enforced deterministic behavior."
    status: pending
  - id: arch-harness-mode
    content: "Harness mode field: add 'mode' (chat | agent | swarm) to SETTINGS.json and config.py. Chat = text-only output, file writes blocked by guard rails. Agent = file writes allowed, gated by VCS_AND_FILE_GATE. Swarm = fan-out to sub-agents, each sub-agent follows its own mode rules. Mode is per-session when session-keyed conversations are implemented."
    status: pending
  - id: arch-session-conversations
    content: "Session-keyed conversations: replace global Conversation() singleton in server.py with dict[str, Conversation] keyed by session_id from request header or parameter. Each client gets isolated conversation state, mode, and skill context. Prevents cross-client state pollution."
    status: pending
  - id: arch-skills-tasks-manifest
    content: "Skills-Tasks manifest: create a single JSON manifest (e.g. Orchestration/Skills/manifest.json) binding each skill keyword to its allowed modes, output type (text | plan+config | resolution | artifact), and associated task families (SWE, VCS, INFRA, etc.). Validated at harness startup — catches broken paths from renamed Tasks/ dirs before runtime."
    status: pending
  - id: arch-mode-guard-rails
    content: "Mode-aware guard rails: extend guard_rails.py beyond length checks to enforce mode constraints (chat mode blocks file writes, generate skill blocks file writes regardless of mode) and budget constraints (count actions against max_actions_before_pivot from SETTINGS.json). Add check_policy(mode, skill, action) function."
    status: pending
  - id: arch-task-feedback
    content: "Task completion feedback loop: when agent or swarm tasks complete, write a .status.json in the task output directory with task_id, status, acceptance_criteria_met, and artifacts list. Skills (summarize, swarm) read these status files to report progress. Filesystem-as-event-bus pattern."
    status: pending
  - id: arch-empty-skill-dirs
    content: "Empty skill directory cleanup: handle empty skill directories (Chat/, Check/, Debug/, Plan/, Refactor/) — either populate each with a minimal SKILL.md defining intended behavior, or delete the empty dirs and create them only when the skill is implemented. Dispatcher must never match on an empty directory."
    status: pending
  - id: arch-skill-executable-migration
    content: "Skill-to-executable migration: convert summarize and generate from SKILL.md-only convention to Python callables that the dispatcher invokes directly. Harness prefers executable when available, falls back to .md for LLM interpretation. Prove the pattern on the two simplest skills before migrating swarm and conflict-resolution skills."
    status: pending
  - id: arch-health-endpoint
    content: "Health endpoint: add GET /health to Python harness server.py returning 200 + JSON with Ollama reachability status. Required for orchestration probes, load balancers, and headless-mode liveness checks."
    status: pending
isProject: false
---

# Features PRD — dotAi (Electron + Ollama + LFM2)

**Purpose:** Product requirements for the AI coding harness (desktop app). Includes settings menu decisions and deferred items for later refinement.

**Project:** Desktop chat app (Electron) for local LLM (Ollama, default model LFM2). OpenCode-style coding assistant; vanilla HTML/CSS/JS. **Agents:** Modular agents with interchangeable **personas** (see `Orchestration/Agents/Personas/`). **Personality** = persona + skillset.

---

## Platform and LLM strategy

The application targets multiple platforms; each serves an LLM with a local-first approach.

| Platform   | LLM behavior |
|-----------|----------------|
| **iPhone** | The iPhone app serves a **small local LLM** on-device (fits in device RAM, client-side inference). |
| **Android** | The Android app serves an **LLM locally**, client-side. |
| **Desktop** | The desktop app allows the user to **download various LLMs** and run them **client-side** on the machine. |
| **Web**     | The client-side web app **serves an LLM** (local/client-side where supported by the browser and environment). |

- **Future:** The application will support **offsite and AI servers** (remote backends, optional cloud).  
- **MVP:** For the minimum viable product we ship **local AI only**: inference runs **client-side** (mobile, web) or **on the machine** (desktop). No dependency on remote LLM services for MVP.

---

## Settings Menu (Decisions & Deferred)

Settings control backend, model, execution level, VCS, logging, and updates. Below: current decisions for MVP vs. items deferred to this PRD for refinement.

### 1. Backend / MVP gate
- **Decision:** MVP acceptance test is whether **Ollama CLI server works locally as the backend**. See MVP_PRD.md success criteria.

### 2. Model endpoint configuration
- **Status:** Add to this feature PRD to refine later. (Future: per-provider base URL/path overrides when multiple backends exist.)

### 3. API keys & secrets
- **Status:** Add to this feature PRD to refine later. (MVP: local Ollama only, no API keys required.)

### 4. Default model
- **Decision:** For now use **Liquid LFM2**.

### 5. Execution level / autonomy
- **Status:** Add to this feature PRD to refine later.
- **Decision for now:** Two presets only — **restricted** and **full access**.

### 6. Commit identity
- **Decision:** Default commit must include **`<ai_model_name>`** (or another stable way to identify that the commit was made by an AI). Configurable in settings.

### 7. VCS integration
- **Decision:** Default VCS is **JJ** (jujutsu).
- **Open:** Models should use the **local project memory system** with JJ commits of the project/product and JJ commit messages on the **dotai GitHub repo**. Solution TBD (to be determined).

### 8. Logging
- **Decision:** Settings must offer **options for logging types** (e.g. level, destination, format). No server-side telemetry; FOSS-compliant local-only logging.

### 9. Budget / rate limits
- **Status:** Add to this feature PRD to refine later. For now: **local LLM only, no cost**; budget/limits apply when cloud or paid backends are added.

### 10. Self-update
- **Status:** Add to this feature PRD to refine later.
- **Decision for now:** Recommend users use the **nightly build** (nightly/development branch). No auto-update requirement for MVP.

### 11. System prompt / behavior source
- **Decision:** Use **START_HERE.md** as the system/behavior source (canonical entry point for the agent).

### 12. Context window / chat history
- **Decision for MVP:** Setting to **select how far back chat history goes** (e.g. last N turns or last N tokens).
- **Checkbox:** **Automatic Context refresh** — when enabled, the AI compresses context when it reaches **33%** and **66%** context saturation to avoid hitting the model limit while retaining recent and salient content.

### 13. Temperature & sampling (explained)
- **What it does:** Temperature controls randomness of the model’s next-token choices. Lower (e.g. 0.1–0.3) = more deterministic, reproducible, good for code and edits. Higher (e.g. 0.7–0.9) = more varied, creative, good for brainstorming or open-ended text.
- **Settings:** Global or per-model **temperature**; where supported by the backend, **top_p** (nucleus sampling) and **top_k** (limit vocabulary per step).
- **Presets:** e.g. **Precise** (low temp), **Balanced** (mid), **Creative** (higher) for quick selection without exposing raw numbers.
- **Integration:** Temperature and sampling are **integrated into Personas**. Each persona under `Orchestration/Agents/Personas/` defines its own temperature (and optional top_p/top_k). See Personas README and § Personality below.

### 14. Streaming (explained)
- **What it does:** When **on**, the harness shows the model’s reply token-by-token as it arrives (faster perceived response, progressive display). When **off**, the UI waits for the full response then shows it at once (simpler, easier to copy/paste, sometimes required for tool-use or parsing).
- **Settings:** Toggle **Streaming on/off** in settings. **Fallback:** If the backend or network doesn’t support streaming, the harness automatically uses non-streaming and may show a short “buffering” state.
- **Integration:** Streaming preference is **integrated into Personas**. Each persona can specify streaming on/off; global settings can default from the selected persona. See `Orchestration/Agents/Personas/README.md`.

### 15. Tool use / function calling
- **Decision:** Maintain a **list of functions in the system** and a **list of functions enabled/disabled** (per function or per category). User can turn specific tools on or off (e.g. `read_file` on, `run_terminal` off).

### 16. Guard rails / safety (MVP)
- **Decision for MVP:** Provide **blocklists for system destructive and catastrophic commands**, stored in or referenced from **`Orchestration/Memories`** (e.g. a dedicated blocklist file or section in existing Memory files). The harness blocks or requires confirmation for any command matching these lists.

### 17. Workspace / project root
- **Decision:** **Prompt the user** on whether they want to change the project root (e.g. on first run or when opening a different folder). **Default:** use or create a **`Project/`** directory as the workspace root.

### 18. File and path allowlist/blocklist
- **Status:** Add to this feature PRD to refine later. **MVP:** no file/path allowlist or blocklist; implement in a future release.

### 19. Theme / appearance
- **Decision for now:** Use a **White, Grays and Black** color palette (no accent colors or density options for MVP).

### 20. Keyboard shortcuts
- **Decision:** Create a **PRD to determine all hotkeys and default mappings** (see `HOTKEYS_PRD.md`). Settings will expose view/customize and, later, export/import of keymap.

### Personality: persona + skillset

- The project uses **modular agents** with **interchangeable personas** that have **skillsets**.
- **Personality** (term used in the project) = a **persona** paired with a **skillset** (and optionally an agent implementation). The runnable “who + what” the user selects.
- **Persona:** Behavioral profile (tone, style, temperature/sampling, streaming) — defined under `Orchestration/Agents/Personas/`.
- **Skillset:** The set of tools/functions enabled for that agent (see §15). Personas are interchangeable; the same persona can be paired with different skillsets to form different Personalities.

### Settings Menu Features 21–30 (candidate list)

21. **Persona selector** — Choose active persona from `Personas/`; display name and short description; optionally edit or add custom persona.
22. **Skillset selector** — Choose which skillset (tool set) is active for the current agent; show enabled/disabled functions (ties to §15).
23. **Personality quick-switch** — One-click or dropdown to switch between saved Personalities (persona + skillset combinations) without reconfiguring each part.
24. **New chat from Personality** — Start a new chat with the currently selected Personality (persona + skillset) and optional system prompt override.
25. **Export / import chat** — Export current conversation to file (e.g. markdown or JSON); import to resume or share context.
26. **System prompt override** — Per-session or per-chat override of the default system prompt (START_HERE or persona); plain text or load from file.
27. **Notification preferences** — When to show notifications (e.g. long run complete, error, backend disconnect): always, only when minimized, or off.
28. **Audio / sound** — Toggle sound for send, receive, error (or mute all); optional distinct tones for different events.
29. **Language / locale** — **MVP:** **English-US** only. (Future: UI language, date/time format, optional locale for model output.)
30. **Accessibility** — **MVP:** Optimize for **high DPI screens**. Default **screen goal compliance** is the **MacBook Pro** display (resolution and scaling). Font size, contrast, reduced motion, and screen-reader-friendly labels remain in scope; optional high-contrast within White/Grays/Black palette.

### Settings Menu Features 31–40 (candidate list)

31. **Backend health / connection status** — Show backend (e.g. Ollama) connection status in UI; auto-detect reachable/unreachable; optional retry or “Start Ollama” hint.
32. **Request timeout** — Configurable timeout for chat/completion requests (seconds); show “Request timed out” and option to retry or increase.
33. **Retry on failure** — Number of automatic retries on transient errors (e.g. 0, 1, 2); optional exponential backoff; user can disable.
34. **Copy response format** — When copying assistant message: plain text only, or markdown preserved; optional “Copy as code block” for code snippets.
35. **Chat title / naming** — Auto-name or user-editable chat title (e.g. first message summary); show in sidebar or tab; used for export filename.
36. **Sidebar visibility & width** — Toggle sidebar on/off; persist state; **each bar has a minimum width**; optional adjustable sidebar width (min/max) or collapse to icons only.
37. **Message density** — Compact vs comfortable spacing for message bubbles and input area; persist per user.
38. **Headless mode** — Run the harness without a visible window (e.g. for scripts, CI, or background agent); optional CLI flag or config to enable headless.
39. **Default new chat behavior** — On “New chat”: start blank, or preload a template (e.g. “Coding task”, “Explain”), or reuse last Personality only.
40. **Diagnostics / debug mode** — Toggle debug or verbose logging; optional “Copy diagnostics” (version, backend URL, last error, OS) for support; dev-only options in non-release builds.

---

## Critical settings review — gaps and additions

Review of settings 1–40 against a full configuration surface for the harness. **Already well covered:** backend, model, execution level, commit identity, VCS, logging, context/history, temperature & streaming (personas), tools, guard rails, workspace root, theme, hotkeys, persona/skillset/Personality, export/import, notifications, audio, locale, accessibility, backend health, timeout, retry, copy format, chat title, sidebar, message density, headless, default new chat, diagnostics.

**Critical or important gaps identified:**

| Gap | Risk | Addition |
|-----|------|----------|
| **Settings persistence** | Unclear where UI settings are stored and whether they override or merge with `config/SETTINGS.json`. | Define: user-level vs project-level storage; sync with or document relationship to `config/SETTINGS.json` and `Orchestration/Memories/SETTINGS.json`. |
| **Ollama URL / host:port** | MVP assumes localhost:11434; users may run Ollama on another host or port. | Make **backend URL** (e.g. Ollama base URL) a first-class MVP setting, not only “refine later” (extends §2). |
| **Max output tokens** | Input context is covered (§12); per-response output cap is only implicit in guard rails. | Add **max output tokens** (or max response length) as a user-configurable setting; can default from persona. |
| **Confirm before destructive actions** | Blocklists (§16) block commands; “confirm before run” for shell/commit/delete is a key safety control. | Add **confirm before: run shell, commit, delete file** (or tie explicitly to execution level “restricted” and §5). |
| **Default Personality on startup** | Quick-switch (§23) and new-chat-from-Personality (§24) exist; “which Personality to use when app opens” is not explicit. | Add **default Personality on app start** (or “remember last Personality”) as a preference. |
| **Local-only / no telemetry (user-facing)** | START_HERE states no telemetry; users may want a visible guarantee. | Add **“Local only” / “No external requests”** checkbox or short note in settings (reassurance, no new behavior). |
| **Session restore** | Whether to restore last chat(s) on app reopen vs always start fresh is not specified. | Add **session restore**: restore last session (chats, sidebar state) on reopen, or always start fresh; persist per user. |
| **Config file path override** | Power users / multi-project may need to point at a specific SETTINGS.json or config dir. | Add **config path override** (optional): path to SETTINGS.json or config directory for profile/project-specific config. |

The following **settings (41–48)** are first-class features in this PRD; see todos in frontmatter. Items already partly covered elsewhere (e.g. BEHAVIOR_CONFIG, RULES) are aligned by reference.

### Settings Menu Features 41–48

41. **Settings persistence & config source** — Define where harness UI settings are stored (e.g. Electron user data, or project `config/SETTINGS.json`). Document relationship to `config/SETTINGS.json` and `Orchestration/Memories/SETTINGS.json`; support user-level vs project-level where applicable.
42. **Backend URL (Ollama host/port)** — **MVP:** User-configurable **Ollama base URL** (e.g. `http://localhost:11434` or custom host:port). Default: `http://localhost:11434`. Required for non-default Ollama setups.
43. **Max output tokens** — User-configurable **max tokens (or max length) per assistant response**; optional per-persona default; hard cap to avoid runaway output (align with guard rails).
44. **Confirm before destructive actions** — Explicit settings: **confirm before run shell**, **confirm before commit**, **confirm before delete/move file**. Can map to execution level “restricted”; must be clearly visible in UI.
45. **Default Personality on startup** — Preference: **default Personality when app starts** (e.g. “last used”, “always [named Personality]”, or “ask each time”). Persist choice.
46. **Local-only / no external requests (user-facing)** — **Checkbox or short statement** in settings: “Local only — no data sent to external servers” (reassurance; behavior already required by START_HERE). No telemetry; optional “strict local” that disables any future optional network features.
47. **Session restore on reopen** — **On app reopen:** restore last session (open chats, sidebar state, selected Personality) or **always start fresh**. Persist preference and state.
48. **Config path override** — **Optional:** Path to **SETTINGS.json** or config directory for profile/project-specific config (power users, multi-project, CI/headless).

The following **settings (49–52)** add agent governance and execution isolation as first-class requirements.

### Settings Menu Features 49–52

49. **Agent hierarchies** — Support hierarchical agent structures (e.g. lead agent, specialist agents, worker agents) with explicit parent/child relationships, delegation rules, escalation paths, and inherited policy constraints.
50. **Ancient quorums** — Add configurable quorum policies for sensitive or high-impact actions (e.g. destructive file changes, broad refactors, release actions), including quorum size, voter set, timeout, and tie/deny behavior. Preserve the term **Ancient Quorums** as a named governance mode for compatibility with project language.
51. **Sandboxing for agents and tasks** — Run agents/tasks in constrained sandboxes with scoped filesystem access, network policy, process controls, and command restrictions; surface sandbox profile selection in settings and per-task overrides where permitted.
52. **Virtual environments for isolation** — Support per-agent and per-task virtual environments (e.g. Python `venv`, Node tool/runtime isolation) to prevent dependency leakage, improve reproducibility, and reduce cross-task side effects.

The following **settings (53–57)** include failure-aware coordination, learning optimization, and layered memory with a file/dir facts layer and knowledge base.

### Settings Menu Features 53–56

53. **Failure-aware JJ handoff** — Define a compact failure-event schema in jj commit messages and require new agents to load unresolved failures plus validated lessons at startup.
54. **Hybrid memory pipeline (short-term + long-term)** — Use jj as short-term active memory and promote validated patterns into durable project memory (`MENTAL_MAP.md` and curated docs).
55. **Strict Ancient Quorum promotion gate** — Promotion to long-term memory must pass a stricter Ancient Quorum policy (default supermajority, with critical-path 3/3 requirement) to avoid polluting durable guidance.
56. **Failure-learning performance optimization** — Track and optimize harness outcomes using metrics such as repeat-failure reduction, avoidance hits, time-to-recovery, and token/cycle savings from reusing verified lessons.
57. **Layered memory and knowledge base** — Research and implement a layered memory system with at least one layer holding facts about files and directories in the project; harness uses this layer (and tool verification) to eliminate misalignments and hallucinations about file knowledge. Knowledge embeddings and a symbolic KB (e.g. Prolog, file-based index) assist the harness. See [LAYERED_MEMORY_AND_KNOWLEDGE_BASE_PRD.md](LAYERED_MEMORY_AND_KNOWLEDGE_BASE_PRD.md).

### Feature 58: Haskell-Based Input Filtering Program (Research Required)

**Status:** Research — determine feasibility before implementation.

**Problem:** Every prompt sent to the LLM costs inference cycles, energy, and time. Many inputs should never reach the model at all:

- **Malicious prompts** — requests with criminal intent, instructions for illegal activities, abusive or dangerous content. These must be blocked and logged.
- **Junk/trivial prompts** — greetings ("hi", "hello"), empty inputs, gibberish, off-topic nonsense. These waste AI resources and should receive fast, deterministic static responses instead.

**Proposal:** A standalone input filtering program written in **Haskell** that sits between user input and the LLM backend. Haskell's strong type system, pattern matching, and parse performance make it a candidate for building a robust, auditable classification pipeline.

#### 58.1 Malicious Prompt Filtering

The filter must identify and block prompts that:

1. **Express criminal intent** — requests to plan, execute, or facilitate crimes (theft, fraud, violence, exploitation).
2. **Request illegal activity** — drug synthesis, weapons manufacturing, hacking instructions, doxxing, CSAM, or other legally prohibited content.
3. **Are abusive or dangerous** — harassment, threats, self-harm instructions, hate speech, targeted abuse.

Blocked prompts receive a standard rejection message and are logged (locally, per FOSS compliance) for audit. The filter must have low false-positive rates on legitimate security research, fiction writing, and educational queries.

#### 58.2 Junk/Trivial Prompt Diversion with Static Responses

Certain inputs should **never invoke the AI model**. Instead, the filter returns a static, deterministic response:

| Input Pattern | Static Response (example) |
|--------------|--------------------------|
| "hi", "hello", "hey", greetings | "No VCS session initialized. Run `jj status` or open a project to begin." (or configurable onboarding message) |
| Empty / whitespace-only | "Empty input. Type a question or task to get started." |
| Single-character / gibberish | "Input not recognized. Please describe what you'd like to do." |
| Repeated/spam input | "Duplicate input detected. Please provide a new question or task." |
| Off-topic non-coding small talk | Configurable: redirect to task, or short static reply |

**Rationale:** Static responses save energy, reduce latency, and prevent the LLM from generating verbose answers to meaningless inputs. The response map is configurable in a data file (e.g. JSON or YAML) so users and projects can customize messages without recompiling.

#### 58.3 Prompt Classification Taxonomy

Research must produce a taxonomy with at least these categories:

1. **Malicious / criminal** — block, log, reject
2. **Illegal activity** — block, log, reject
3. **Abusive / dangerous** — block, log, reject
4. **Trivial / greeting** — static response, no LLM
5. **Off-topic / useless** — static response or short redirect, no LLM
6. **Valid** — pass through to LLM

Each category needs: definition, example prompts, false-positive examples (legitimate queries that look similar), and escalation rules (e.g. borderline cases forwarded to LLM with a safety system prompt prefix).

#### 58.4 Haskell Feasibility Evaluation

Research must evaluate:

- **Parse and classification performance** — can Haskell pattern matching + regex handle prompt classification at acceptable latency (sub-10ms for trivial, sub-50ms for malicious scan)?
- **Type-safety benefits** — does Haskell's type system provide meaningful safety guarantees for the filter rules (exhaustive pattern matching, no unhandled categories)?
- **Integration with the harness** — how does the Haskell filter connect to the Electron + Ollama stack? Options: FFI binding, subprocess/pipe, HTTP microservice, or compiled CLI tool invoked per prompt.
- **Comparison with alternatives** — Rust (performance + safety), OCaml (similar FP guarantees), plain regex/JS in-process (simplest, no new dependency). Produce a decision matrix.
- **Maintainability** — can the team (primarily AI agents + one human) maintain Haskell code? What is the onboarding cost vs. benefit?

#### 58.5 Configuration and Extensibility

- Static response map stored as a data file (JSON/YAML), editable without recompilation.
- Malicious prompt patterns as a separate blocklist file, updatable independently.
- Filter operates as a **pre-processing gate** — all input passes through it before reaching the LLM backend.
- Filter results are available to the harness (pass/block/static with category label) for logging and UX (e.g. showing "Input blocked: policy violation" vs. a static greeting response).

---

## Architecture: Skill Dispatch, Mode Awareness, and Runtime Enforcement

The harness architecture has a structural gap: the directory layout (Skills/, Tasks/, modes) defines an API surface, but the runtime does not enforce it. The features below close this gap.

### Skill Dispatcher (arch-skill-dispatcher)

**Problem:** User input goes straight to Ollama. The LLM is expected to "know" about skills from the system prompt, making skill behavior probabilistic rather than deterministic.

**Solution:** Add a dispatcher between input reception and LLM call in `server.py`:

1. Extract the first token of user input.
2. Check if `Orchestration/Skills/{token}/` exists and contains `SKILL.md` or `{token}.md`.
3. If matched: load the skill definition and either (a) inject it into the system prompt for that request, or (b) invoke a Python callable if the skill has been migrated to executable form.
4. If no match: pass through to LLM as normal chat.

**Dispatch flow:**

```
Input (one word or phrase)
    │
    ├─ skill directory exists + has SKILL.md or callable
    │   ├─ callable exists → invoke directly (deterministic)
    │   └─ SKILL.md only → inject into system prompt (LLM-interpreted)
    │
    └─ no match → normal LLM chat
```

### Harness Mode (arch-harness-mode)

**Problem:** No runtime distinction between chat, agent, and swarm modes. The `generate` skill says "no file writes" but nothing enforces it.

**Solution:** Add a `mode` field to `SETTINGS.json` and `config.py`:

| Mode | Skill output | Task output | File writes |
|------|-------------|-------------|-------------|
| `chat` | Text in conversation only | Not applicable | Blocked unless user explicitly requests |
| `agent` | Text + can write to `Tasks/{family}/` | Executable artifacts allowed | Gated by VCS_AND_FILE_GATE |
| `swarm` | Text + swarm plan + config | Sub-agents produce artifacts | Each sub-agent follows its own mode |

Mode is per-session when session-keyed conversations are implemented (see below).

### Session-Keyed Conversations (arch-session-conversations)

**Problem:** `server.py` has a single global `Conversation()`. Multiple clients share state.

**Solution:** Replace with `dict[str, Conversation]` keyed by session ID from request header or parameter. Each session carries its own conversation history, active mode, and skill context.

### Skills-Tasks Manifest (arch-skills-tasks-manifest)

**Problem:** Skills and Tasks are two independent directory trees with no formal binding. Renaming `Tasks/SWE/` breaks swarm plans silently.

**Solution:** A single `Orchestration/Skills/manifest.json` that maps each skill keyword to its allowed modes, output type, and task families. Validated at harness startup.

### Mode-Aware Guard Rails (arch-mode-guard-rails)

**Problem:** `guard_rails.py` only checks input length and truncates responses. It does not enforce mode constraints or budget tracking.

**Solution:** Extend with `check_policy(mode, skill, action)`:
- Chat mode + file_write → blocked
- Generate skill + file_write → blocked regardless of mode
- Action count > `max_actions_before_pivot` → PIVOT signal

### Task Completion Feedback (arch-task-feedback)

**Problem:** No mechanism for completed tasks to feed back into the skill system. Swarm plans have no runtime progress tracking.

**Solution:** Tasks write `.status.json` on completion. Skills read these for reporting.

### Skill-to-Executable Migration (arch-skill-executable-migration)

**Migration priority:**

| Priority | Skill | Reason |
|----------|-------|--------|
| 1 | `summarize` | Pure text transformation, trivial to script, high frequency |
| 2 | `generate` | Text only, enforcing "no file write" is easier in code |
| 3 | `swarm` | Has JSON schema; validation is code, not prose |
| 4 | Conflict resolution | Complex multi-step protocols benefit from state machines |

---

## Executive

*High-level goals, success criteria, and scope.*

### Vision & objectives
- [To be populated by AI agent]

### Success criteria
- [To be populated by AI agent]

### Out of scope
- [To be populated by AI agent]

### Stakeholders / users
- [To be populated by AI agent]

---

## Software

*Product features, UX, and functional requirements.*

### User-facing features
- [To be populated by AI agent]

### Chat & conversation
- [To be populated by AI agent]

### Models & parameters
- [To be populated by AI agent]

### UI/UX & accessibility
- [To be populated by AI agent]

### Settings & persistence
- Backend: Ollama as local backend (MVP gate).
- Model: Liquid LFM2 default; endpoint config deferred (see Settings Menu above).
- Execution: Two presets — restricted, full access; refinement deferred.
- Commit: Include `<ai_model_name>` (or equivalent) in default commit identity.
- VCS: JJ default; project memory + JJ commits + dotai GitHub integration solution TBD.
- Logging: Options for logging types (level, destination, format); local-only, no telemetry.
- Budget/rate limits and self-update: deferred; for now local-only (no cost), recommend nightly branch.
- Personas: temperature/sampling and streaming integrated into Personas; Personality = persona + skillset (see § Settings 13–14 and § Personality).
- **Full settings menu:** Features 1–57 (see § Settings Menu and § Critical settings review). Features 41–48: settings persistence & config source, backend URL, max output tokens, confirm before destructive actions, default Personality on startup, local-only (user-facing), session restore, config path override. Features 49–52: agent hierarchies, Ancient Quorums, sandboxing, and virtual environments for agent/task isolation. Features 53–56: failure-aware jj handoff, hybrid memory promotion, strict Ancient Quorum promotion gate, and failure-learning performance optimization. Feature 57: layered memory system with file/dir facts layer and knowledge base (see LAYERED_MEMORY_AND_KNOWLEDGE_BASE_PRD.md).
- **Architecture (runtime enforcement):** Skill dispatcher (keyword→directory lookup before LLM call), harness mode (chat | agent | swarm with per-mode guard rails), session-keyed conversations (isolated per-client state), skills-tasks manifest (validated bindings at startup), mode-aware guard rails (policy enforcement + budget tracking), task completion feedback (.status.json), and skill-to-executable migration path. See § Architecture section and arch-* todos.

### Non-functional (performance, security, guard rails)
- [To be populated by AI agent]
- Isolation by default: agent/task execution must honor selected sandbox profile and deny disallowed filesystem/network/process actions.
- Reproducibility: task runs should be replayable with deterministic environment setup using selected virtual environment strategy.
- Governance: high-risk actions should support quorum approval flow with auditable decision records (including Ancient Quorums mode).
- Knowledge integrity: only quorum-approved lessons may enter durable memory; unverifiable or low-confidence lessons remain short-term. File/dir knowledge is grounded in a dedicated facts layer (and tool verification) to eliminate structure hallucination and misalignment (see LAYERED_MEMORY_AND_KNOWLEDGE_BASE_PRD.md).
- Anti-propagation: new agents must consume unresolved-failure context before edits to reduce repeated mistakes and overcorrections.
- Input filtering: malicious, illegal, abusive, and dangerous prompts must be intercepted before reaching the LLM; trivial/junk prompts must receive static responses without consuming inference cycles (see Feature 58: Haskell-Based Input Filtering Program).

---

## Infrastructure

*Runtime, deployment, and operational requirements.*

### Runtime & dependencies
- [To be populated by AI agent]

### Ollama integration
- [To be populated by AI agent]

### Build, package & distribution
- [To be populated by AI agent]

### Observability & diagnostics
- [To be populated by AI agent]

### Platform support
- **iPhone:** Small local LLM on-device (see § Platform and LLM strategy).
- **Android:** Local LLM client-side (see § Platform and LLM strategy).
- **Desktop:** User-downloadable LLMs, run on-machine (Ollama/llama-server; see MVP_PRD and § Platform and LLM strategy).
- **Web:** LLM served client-side where supported (see § Platform and LLM strategy).
- MVP: local AI only; offsite/AI server support is post-MVP.
