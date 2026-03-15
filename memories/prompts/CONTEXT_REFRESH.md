# Context Refresh — Agent Instructions

<!-- AI: Use this when context is full or on session resume. -->

Agents should refresh their context efficiently:

1. Read `START_HERE.md` for system overview (cached after first read)
2. Read task-specific PRD and acceptance criteria
3. Read `Memories/MENTAL_MAP.md` for project patterns
4. Read relevant task files in `Orchestration/Tasks/` for the current task; apply skill commands from `Orchestration/Skills/` when the user uses keywords (e.g. summarize, generate)
5. Minimize re-reading unchanged files between iterations

For runtime and model config, see `Memories/system/runtime.md` and `Memories/system/model_serving.md`.
