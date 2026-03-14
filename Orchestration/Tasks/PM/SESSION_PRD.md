# SESSION_PRD — Populate a Session PRD

<!-- AI: When the user wants a session PRD, follow the Instructions below. PRDs live under Project/Product/PRDs/. -->

## Role

Create a **session PRD** — a product requirement document that captures the work the user wants to do in this session. Do not execute the todos unless the user asks; this task is only for **populating** the PRD.

## Instructions

### Step 1 — Bootstrap (minimal)

- Read `SYSTEM_PROMPT.md` (or `Project/SYSTEM_PROMPT.md` from repo root).
- Read `Project/Product/PRDs/MVP_PRD.md` **only the YAML frontmatter (lines 1–65)** to learn the PRD format: `name`, `overview`, `todos` (each with `id`, `content`, `status`), `isProject`.

### Step 2 — Gather session scope

Ask the user (or use the message they provided):

**"What work do you want to do in this session? List goals, tasks, or outcomes. I'll turn this into a session PRD with trackable todos."**

If they already pasted their goals in the same message, use that. Otherwise ask once, then proceed.

### Step 3 — Write the session PRD

Create **one new file**:  
`Project/Product/PRDs/SESSION_<YYYY-MM-DD>.md`  
(use today's date, e.g. `SESSION_2025-03-13.md`).

Format:

- **YAML frontmatter** (same as MVP_PRD):
  - `name`: Short title, e.g. `"Session 2025-03-13 — [brief theme]"`
  - `overview`: 1–2 sentences describing this session's focus and why it matters.
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

## What the user can paste

Paste this into a new chat with the repo open:

```
Read Orchestration/Tasks/PM/SESSION_PRD.md and follow the Instructions. I want to do the following work this session:

[Describe your goals here.]
```

Replace the bracketed part with actual session goals. The agent will create `Project/Product/PRDs/SESSION_<date>.md` and fill it with todos derived from the list.
