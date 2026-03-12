deliverables for the dotAi desktop chat application.
1. Structural Wireframes (Mid-Fidelity)
Screen A: Main Chat View (Ollama-like Hero Chat)

This is the primary interface. It prioritizes the conversation and model selection while keeping advanced tools subtly accessible.
Plaintext

[ macOS/Win Window Chrome ] 
-----------------------------------------------------------------------------------------
|  [Sidebar Toggle]     [ Mode: Chat | Code | Plan ▾ ]      [ Model: Llama-3-8B-Local ▾ ] |
|---------------------------------------------------------------------------------------|
|  [ Sidebar (Collapsible) ]  |  [ Main Chat Area ]                                     |
|                             |                                                         |
|  Recents:                   |   User: Can you draft a jj commit for the PRD updates?  |
|  - UI layout planning       |                                                         |
|  - Docker orchestrator      |   dotAi: I have analyzed the working directory and      |
|                             |   prepared the following commit.                        |
|  Active Mods / Personas:    |                                                         |
|  [⚙️ SWE Developer]          |   ╭──────────────────────────────────────────────────╮  |
|                             |   │ > jj describe -m "docs: update MVP PRD"          │  |
|  Status:                    |   ╰──────────────────────────────────────────────────╯  |
|  🟢 Local (No Telemetry)    |   [ Action: Confirm & Run ] (Execution Level: Safe)     |
|                             |                                                         |
|                             |---------------------------------------------------------|
|                             | [📎] [🎤]  Message dotAi...           [🎯 Steer: Auto ▾]|
-----------------------------------------------------------------------------------------

Screen B: Settings & Backend Configuration

Accessed via a minimal gear icon or system menu. It is declarative and highlights the local-first architecture.
Plaintext

-----------------------------------------------------------------------------------------
|  < Back to Chat                                                 [ Settings ]          |
|---------------------------------------------------------------------------------------|
|  Configuration Path: ~/Project/Orchestration/Memories/SETTINGS.json                   |
|                                                                                       |
|  [ Backend & Routing ]                                                                |
|  (•) Local Default (http://localhost:11434)                                           |
|  ( ) Custom Llama-Server (http://localhost:8080)                                      |
|  [✓] Strictly Local-Only (Disable external API fallback)                              |
|                                                                                       |
|  [ Security & Execution ]                                                             |
|  Execution Level: [ Ask for Confirmation ▾ ] (Confirm all jj / destructive actions)   |
|                                                                                       |
|  [ Session Management ]                                                               |
|  [✓] Restore last session on app start                                                |
|  [ Start Fresh Session ]                                                              |
-----------------------------------------------------------------------------------------

Screen C: Mods & Personas Overlay

Triggered from the sidebar or by typing a shortcut (like /mod).
Plaintext

-----------------------------------------------------------------------------------------
|  [ Select Active Mod / Persona ]                                                      |
|---------------------------------------------------------------------------------------|
|                                                                                       |
|  (•) Standard Chatbot (Default)                                                       |
|      Lightweight, general assistance and project navigation.                          |
|                                                                                       |
|  ( ) SWE Developer (Orchestrator)                                                     |
|      Full access to jj commits, Docker management, and file creation.                 |
|                                                                                       |
|  ( ) Tech Lead / Reviewer                                                             |
|      Read-only mode. Criticizes code quality and checks against base repo guidelines. |
|                                                                                       |
|  [ + Load custom persona from Orchestration/Agents/Personas/ ]                        |
-----------------------------------------------------------------------------------------

2. High-Fidelity UI Mockup Specifications (Visual System)

To evoke the "Ollama-like" spirit without pixel-copying, the UI must rely heavily on contrast, typography, and negative space rather than borders and boxes.

    Color Palette (Dark-First):

        App Background: #121212 (Deep, distraction-free black).

        Surface/Panels (Sidebar/Header): #1A1A1A (Slightly elevated).

        User Message Bubble: Transparent, text aligned right.

        AI Message Area: #1E1E1E block or transparent, text aligned left.

        Accents (Buttons/Active States): #E5E5E5 (High-contrast neutral) or subtle #3B82F6 (Muted blue) for primary calls to action.

        Code Blocks: #0D0D0D background with subtle syntax highlighting.

    Typography:

        UI & Body: Inter or system default sans-serif (San Francisco/Segoe UI). Clean, readable at 14px-16px.

        Code & Logs: JetBrains Mono or Fira Code (13px).

    Component Styling:

        Inputs: Borderless or very subtle 1px #333333 stroke.

        Buttons: Flat, ghost-style by default, slightly rounded corners (6px radius).

        Loading States: A subtle pulsing dot or a skeleton text block. No spinning wheels.

3. UX Notes & PRD Mapping
View / Component	Relevant Project Doc	Feature Implementation Details
Main Chat Layout	MVP_PRD.md, CHATBOT.md	Single conversation view acts as the hero. Supports both "simple chat" and complex "agent tasks" within one unified timeline.
Input Area	MVP_PRD.md, §3 Features	Includes [📎] Attachment affordance, [🎤] Voice entry toggle, and a [🎯 Steer] dropdown to adjust model verbosity/tone dynamically.
Header Bar	FEATURES_PRD.md	Features a segmented Mode selector (Chat/Code/Plan) and a prominent Model dropdown.
jj Commit Card	CHATBOT.md	Shows the Execution level safety check. If the model proposes a git/jj action, it renders as an actionable card with a "Confirm & Run" button.
Settings Panel	FEATURES_PRD.md, SYSTEM.md	Exposes the Backend URL, enforces the Local-only trust model, and houses the Session & restore toggles.
Mods/Personas	Personas/, CHATBOT.md	Allows hot-swapping behavior without restarting the app. Aligns with the local directory structure for custom extensions.
4. Agent Consistency Checklist

    [x] Ollama-like UX: Clean, minimal, dark-first, model-centric, chat is the hero.

    [x] Feature Coverage (§3): Steering, modes, attachments, mods, voice, backend configuration, session restore, and execution level are all accounted for in the wireframes.

    [x] Privacy/Local-first: Explicit "Strictly Local-Only" UI element reinforces no telemetry/server-side logging.

    [x] Backend Alignment: Direct references to localhost:11434 and localhost:8080 (llama-server) as per SYSTEM.md.

    [x] Persona Alignment: Mod selection directly maps to the Orchestration/Agents/Personas/ directory concept.