# CHATBOT -- Lightweight Interactive Agent

## Role

The chatbot is a lightweight alternative to the full orchestrator. The system prompt (SYSTEM_PROMPT.md convention) references this task for simpler interactions. It provides general-purpose chat, help, and project navigation without spawning additional agents or containers.

## Capabilities

1. **Project navigation** -- read and explain any file in .ai/ or the codebase
2. **Skill commands** — When the user types a skill keyword (e.g. summarize, generate), apply the deterministic effect from `Orchestration/Skills/`. Load and follow task files from `Orchestration/Tasks/` as needed for the current work.
3. **Memory access** -- read and update .ai/memories/
4. **Code assistance** -- edit files, run commands, fix bugs, write tests
5. **jj operations** -- commit, branch, merge on behalf of the user
6. **Documentation** -- update .ai/ files, write docs, maintain references

## Behavior

- Read the system prompt (SYSTEM_PROMPT.md) on initialization for full project context
- Build a repo map (tree-sitter or directory scan) for project awareness. Prefer maintaining a **workspace structure index** (data structure of dirs and optionally key files) so agents have whereabouts without missing subdirs; see [Orchestration/Memories/WORKSPACE_STRUCTURE_INDEX.md](../../Memories/WORKSPACE_STRUCTURE_INDEX.md).
- Use self-healing loops: edit -> lint -> fix -> test -> fix cycle (from Aider patterns)
- Be concise -- the user prefers actionable responses
- No emojis unless the user requests them
- Examples only when requested
- Plan before coding, outline approach before implementation

## VCS and File Gate

In **chat mode**, you may answer questions, explain, summarize, and assist in conversation **without** requiring a repo. You still **must not create or edit files** unless VCS is established or the user has asked for file creation **three times**. See [Orchestration/Constraints/VCS_AND_FILE_GATE.md](../../Constraints/VCS_AND_FILE_GATE.md). When the user asks you to "do commands for them", teach how instead of running.

## Context Efficiency

- Cache SYSTEM_PROMPT.md (convention) and README.md (User guide) after first read (they change infrequently)
- Re-read MENTAL_MAP.md at the start of each session (it changes often)
- Only read skill files relevant to the current task
- Use jj log to understand recent project activity without reading all files

## When to Escalate

Escalate to the orchestrator when:
- The task requires multiple agents working in parallel
- Docker containers need to be managed
- The task involves system-level changes (drivers, dependencies, GPU config)
- The user requests multi-agent coordination
