# UX/UI & Wireframe Agent Prompt — dotAi Chat App

**Purpose:** Use this document as the master prompt when an AI coding agent generates UX/UI designs, mockups, and wireframes for the dotAi desktop chat application. The agent must stay aligned with the current project and produce interfaces that feel like Ollama while supporting the listed chat features.

---

## 1. Project alignment — read first

Before producing any wireframes or mockups, the agent **must** internalize the following.

### 1.1 What this project is

- **Product:** Desktop AI coding harness (Electron app). Local LLM backend by default (Ollama; API at `http://localhost:11434` or project-configured llama-server at `http://localhost:8080`). OpenCode-style coding assistant.
- **System:** dotAi — markdown-first agent orchestration. No protocol servers; files and jj commits are the protocol. See `Project/README.md` and `Project/SYSTEM_PROMPT.md` (convention) (system prompt / convention).
- **Roles:** Lightweight **chatbot** (interactive chat, project navigation, code help, jj commits) and optional **orchestrator** (multi-agent, Docker). UX should support both “simple chat” and “agent/task” mental models.
- **Constraints:** Local-first, no telemetry, FOSS-friendly. No server-side logging of agent activity. Settings and state live in project config (e.g. `Orchestration/Memories/SETTINGS.json`); UI should reflect “config path override” and “local-only” as first-class options.

### 1.2 Authoritative project docs (agent must read)

- `Project/README.md` — User guide, directory layout, base repo guidelines.
- `Project/SYSTEM_PROMPT.md` (convention) — System prompt (convention), rules, tasks, bootstrap.
- `Orchestration/Memories/system/runtime.md`, `system/model_serving.md` — Runtime (OS, GPU, model endpoint, llama-server).
- `Project/Product/PRDs/FEATURES_PRD.md` — Settings, backend URL, model, execution level, session restore, personas.
- `Project/Product/PRDs/MVP_PRD.md` — MVP gate (Ollama/backend works locally), success criteria.
- `Orchestration/Tasks/SWE/CHATBOT.md` — Chatbot capabilities and behavior.

### 1.3 Design principles from the repo

- **Declarative over imperative** — Prefer clear, scannable UI that describes state and options rather than hidden flows.
- **Lean and opinionated** — Strong, consistent patterns over many one-off screens.
- **Backward compatibility** — New UI should not assume removal of existing concepts (e.g. personas, tasks, jj).
- **Separation of concerns** — Base = shared; project-specific customizations stay documented and overridable.

---

## 2. Visual and UX reference: “Ollama-like”

Wireframes and mockups should **evoke Ollama’s UI/UX** while extending it with the features in §3. Do not copy Ollama pixel-for-pixel; capture its spirit.

### 2.1 Ollama-like traits to adopt

- **Clean, minimal chrome** — Plenty of whitespace; no heavy borders or decorative clutter. Sidebar or top bar for model/settings, main area for content.
- **Model-centric** — Current model is visible and switchable without digging into a deep settings tree. Model list or selector (name, size, maybe icon) is a primary control.
- **Dark-first with light option** — Dark theme as default; light theme supported. Neutral, low-saturation palette (grays, soft accents). No garish gradients.
- **Chat as the hero** — Single main conversation view; message bubbles or blocks that are easy to scan. Input anchored at bottom; optional compact top bar (model, menu).
- **Typography** — Clear hierarchy: one primary sans for UI, monospace only for code/code blocks. Readable body size; avoid tiny labels.
- **Feedback** — Subtle loading (e.g. typing indicator, skeleton or pulse), clear idle/thinking/sending states. No unnecessary animations.
- **Desktop-native feel** — Window chrome, resize, and layout appropriate for Electron (macOS/Windows/Linux). Consider system title bar and traffic lights on macOS.

### 2.2 References (for inspiration only)

- Ollama’s own desktop app: model list, chat layout, settings placement.
- “Ollama-style” community UIs: simple model switcher, single-column or sidebar chat, dark theme, code blocks in chat.

---

## 3. Required chat features (must appear in wireframes)

The following capabilities **must** be represented in flows and wireframes. Prefer integration into one coherent chat experience (Ollama-like) rather than separate apps.

| Feature | Description | Wireframe / UX note |
|--------|-------------|----------------------|
| **Steering** | User can steer model behavior (e.g. tone, verbosity, role, or “system” instructions). | Dedicated control or panel: inline “steer” chip, dropdown, or collapsible “Steering” section near input or in a sidebar. Persist per-session or per-persona. |
| **Modes** | Distinct modes (e.g. “Chat”, “Code”, “Review”, “Plan”). | Mode selector visible in or near the input area or header (tabs, segmented control, or dropdown). Clear label for current mode. |
| **Attachments** | User can attach files (e.g. code, docs, images) to a message. | Attachment affordance at input (clip/plus). List of attached files with remove; optional preview. Consider paste-from-clipboard and drag-drop. |
| **Mods** | Extensions or “mods” that change behavior (e.g. persona, tool set, style). | “Mods” or “Extensions” entry in settings or sidebar; list of available/active mods; enable/disable per chat or global. Align with `Orchestration/Agents/Personas/` and project notion of persona. |
| **Voice entry** | Speech-to-text for the user message. | Mic button at input; clear recording state (idle / recording / processing). Optional: shortcut and accessibility note. |
| **Model / backend** | Backend URL (Ollama host/port or llama-server), model choice. | Visible in header or settings; match FEATURES_PRD (configurable base URL, default local). Model switcher as in §2.1. |
| **Session & restore** | Session restore on reopen; optional “start fresh”. | Reflected in app state (e.g. “Restore last session” in settings). Wireframes can show “reopen → same conversation” vs “new conversation” entry points. |
| **Execution level / safety** | Restricted vs full access (confirm destructive actions). | Settings or per-chat: “Execution level” or “Confirm before run/commit/delete”. Wireframe: where this is set and how it’s surfaced (e.g. banner or badge when restricted). |

Additional project-relevant items to consider in flows (not necessarily separate screens):

- **Personality / persona** — Default personality on startup (last used, fixed, or ask). One clear place to choose or change persona.
- **jj / commits** — Chatbot can perform jj operations. UX: optional “Commit” or “VCS” action or confirmation when the agent proposes a commit.
- **Local-only** — Checkbox or statement that no external requests are made; reinforces trust.

---

## 4. Deliverables the agent must produce

1. **Wireframes** (low- or mid-fidelity) for:
   - Main chat view (with input, attachments, steering, modes, voice).
   - Model selector and backend/settings (Ollama-like placement).
   - Mods/personas management (sidebar or settings).
   - Optional: settings screen(s) for execution level, session restore, config path, local-only.

2. **UI mockups** (high-fidelity, Ollama-like) for:
   - Same key screens; use a consistent design system (colors, type, spacing) that matches §2.1.

3. **Short UX notes** (bullet or table) per screen:
   - Which project doc or PRD item the screen supports.
   - Which of the features in §3 it implements and how (e.g. “Steering: dropdown next to input”).

4. **Consistency checklist** (agent self-check):
   - [ ] All screens respect “Ollama-like” (minimal, dark-first, model-centric, chat as hero).
   - [ ] Every feature in §3 appears in at least one wireframe/mockup with a clear control.
   - [ ] No telemetry or server-side logging implied in UI.
   - [ ] References to backend, config path, and “local-only” align with FEATURES_PRD and Memories/system/.
   - [ ] Persona/mod concept aligns with `Orchestration/Agents/Personas/` and CHATBOT.md.

---

## 5. Instructions to the UX/wireframe agent (copy-paste summary)

You are generating UX/UI designs and wireframes for the **dotAi desktop chat application**. Your output must:

1. **Align with the project**  
   Read and use: `Project/README.md`, `Project/SYSTEM_PROMPT.md` (convention), `Orchestration/Memories/system/` (runtime.md, model_serving.md), `Project/Product/PRDs/FEATURES_PRD.md`, `Project/Product/PRDs/MVP_PRD.md`, and `Orchestration/Tasks/SWE/CHATBOT.md`. Respect local-first, no telemetry, and the base-repo guidelines.

2. **Match Ollama’s UI/UX spirit**  
   Clean, minimal, dark-first with light option, model-centric, chat as hero. Single main conversation view, clear model selector, subtle feedback, desktop-native layout.

3. **Include these chat features in your wireframes and mockups**  
   Steering, modes, attachments, mods (personas/extensions), voice entry, model/backend configuration, session restore, and execution level / confirm-before-destructive. Add brief UX notes tying each screen to project docs and to these features.

4. **Produce**  
   Wireframes and UI mockups for the main chat view, model/settings, and mods/personas; plus a short consistency checklist confirming Ollama-like look, feature coverage, and project alignment.

Use this document as your single source of alignment and scope when generating mockups and wireframes.
