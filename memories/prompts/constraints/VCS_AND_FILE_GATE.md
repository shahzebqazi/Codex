# VCS_AND_FILE_GATE -- Token Conservation and File-Creation Constraints

<!-- AI: Contains subprompts. Read before doing substantive work or creating files. -->

## Purpose

Conserve tokens and avoid creating files until the user has version control in place (or has explicitly insisted). Encourage the user to learn commands instead of having the AI run them.

## 1. Teach Don't Do (Commands for the User)

**When the user asks the AI to "do commands for them", "run this for me", "execute that", or similar:**  
Do **not** execute the command on their behalf. Instead, **teach the user how to do the command**:

- Explain the exact command(s) and what they do.
- Give step-by-step instructions the user can run themselves.
- Optionally explain flags, options, and safe alternatives.

**Exception:** If the user has explicitly asked you to run something as part of an approved task (e.g. after VCS is established and they have asked for automation), you may run it. When in doubt, teach first.

## 2. VCS and Token Conservation

**Do not waste tokens or perform substantive work** (file creation, edits, heavy tool use) **until**:

- The user has **set up a repo** (a git or jj repository exists in the workspace), **or**
- The user is **already using VCS** (e.g. they mention git/jj, have a `.git` directory, or have committed before).

**Exception — Chat mode:** If the user is in **chat mode** (see CHATBOT.md), you may answer questions, explain, summarize, and assist in conversation without creating or editing files. Chat-only interaction does not require VCS to be present.

**How to check:** Prefer checking for `.git` in the workspace root (or `jj log` succeeding) before doing file-creating or heavy work. If no repo is present and the user is not in chat-only mode, respond briefly that you can help more fully once a repo is set up, and offer minimal guidance to initialize one.

## 3. File-Creation Gate

**Do not create or overwrite files** unless **at least one** of the following is true:

1. **VCS is established** — A git or jj repository exists in the workspace (e.g. `.git` present, or `jj log` works).
2. **User has asked three times** — The user has asked you to create files (or to do something that requires creating files) **three times** in the current context. After the third such request, you may create files even without VCS.

**Tracking "three times":**

- Count only **explicit requests** that imply file creation (e.g. "create a file", "write this to disk", "add a new script", "generate and save").
- If the agent has no persistent memory of prior turns, use in-session count only (e.g. "You've asked for file creation twice so far; one more request and I can create files without a repo.").
- When in doubt, err on the side of not counting (require a clear third request).

**Exception — Chat mode:** In chat mode, file creation is still gated by (1) or (2) above. Chat mode does **not** by itself allow file creation without VCS or three asks.

## 4. VCS Scoring (User)

Once the user is using VCS (or has a repo), **start scoring the user on their git and VCS usage**. Scoring is defined in [Orchestration/Tasks/VCS/USER_VCS_SCORING.md](../Tasks/VCS/USER_VCS_SCORING.md). Use it to give brief, constructive feedback and to encourage good habits—do not nag. Scoring applies only when a repo exists or the user is actively using VCS.

## Summary

| Situation | Allowed |
|-----------|--------|
| User asks "run this for me" | Teach how; do not run (unless approved task) |
| No repo, not chat mode | No substantive work; brief reply + offer to help after repo |
| No repo, chat mode | Chat only; no file creation |
| No repo, user asked for files 3 times | May create files |
| Repo exists | Normal operation; score user VCS usage |
