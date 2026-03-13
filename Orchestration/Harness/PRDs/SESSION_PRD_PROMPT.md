# Prompt: Populate a Session PRD

Copy the text below into a new AI agent (new chat or new session). The agent will bootstrap from the Codex dotAi context and produce a PRD for your session work.

---

## Instructions for the new agent

You are in the Codex repo (dotAi system). Your only task right now is to create a **session PRD** — a product requirement document that captures the work the user wants to do in this session.

### Step 1 — Bootstrap (minimal)

- Read `Codex/.ai/START_HERE.md` (or `.ai/START_HERE.md` if your cwd is Codex).
- Read `Codex/.ai/project/PRDs/MVP_PRD.md` **only the YAML frontmatter (lines 1–65)** to learn the PRD format: `name`, `overview`, `todos` (each with `id`, `content`, `status`), `isProject`.

### Step 2 — Gather session scope

Ask the user (or use the message they provided):

**"What work do you want to do in this session? List goals, tasks, or outcomes. I’ll turn this into a session PRD with trackable todos."**

If they already pasted their goals in the same message, use that. Otherwise ask once, then proceed.

### Step 3 — Write the session PRD

Create **one new file**:  
`Codex/.ai/project/PRDs/SESSION_<YYYY-MM-DD>.md`  
(use today’s date, e.g. `SESSION_2025-02-14.md`).

Format:

- **YAML frontmatter** (same as MVP_PRD):
  - `name`: Short title, e.g. `"Session 2025-02-14 — [brief theme]"`
  - `overview`: 1–2 sentences describing this session’s focus and why it matters.
  - `todos`: One todo per discrete task the user mentioned. Each:
    - `id`: short slug (e.g. `task-1`, `fix-readme`)
    - `content`: one clear, actionable sentence
    - `status`: `pending` for all (this is the plan, not the result).
  - `isProject`: `false` (this is a session PRD, not the main project PRD).

- **Markdown body** (below the frontmatter):
  - A brief **Context** section: what this session is for and how it fits the repo.
  - A **Scope** section: bullet list of in-scope and out-of-scope for this session.
  - Optional: **Acceptance criteria** or **Done when** for the session as a whole.

### Step 4 — Confirm

After writing the file:

1. Show the user the path to the new PRD and a short summary of the todos.
2. Say: "Session PRD is ready. I can work through these todos next, or you can hand this PRD to another agent. Should I start on the first task?"

Do not start executing the todos unless the user asks you to. This prompt is only for **populating** the PRD.

---

## What to paste when starting the new agent

Paste this into a new chat with the repo open (workspace including `Codex/`):

```
Read Codex/.ai/project/PRDs/SESSION_PRD_PROMPT.md and follow the "Instructions for the new agent" section. I want to do the following work this session:

[Describe your goals here. For example:]
- Goal 1
- Goal 2
- Outcome or deliverable you want by end of session
```

Replace the bracketed part with your actual session goals. The agent will create `Codex/.ai/project/PRDs/SESSION_<date>.md` and fill it with todos derived from your list.
