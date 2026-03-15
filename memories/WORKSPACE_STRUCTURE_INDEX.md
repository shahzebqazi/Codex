# Workspace Structure Index — Data Structure for File/Dir Whereabouts

<!-- AI: When available, use the workspace structure index to answer "where is X?" or "does dir Y exist?" instead of relying only on glob. Reduces missing subdirs and structure hallucinations. -->

## Purpose

AI agents sometimes **miss that a directory or file lives in a subdir** when they rely only on glob or on documentation. A **workspace structure index** (or directory manifest) is a **harness technique**: maintain a single data structure that records the whereabouts of directories and optionally key files, so agents can query it instead of (or before) globbing. This gives a shared "map" of the tree and reduces structure hallucinations.

## Technique

1. **Build a data structure** that represents the workspace layout:
   - **Directories:** all directories under the workspace root (or under a configured root), with path and optionally depth or parent.
   - **Optionally:** key files (e.g. entrypoints, configs, READMEs) with their paths so "where is the API?" is one lookup.

2. **Store it** in a known location so any agent or tool can read it:
   - **File-based:** e.g. `Orchestration/Memories/workspace_structure.json` (or project-configured path). Format: tree or flat list of `{ "path": "...", "type": "dir"|"file", "children": [...] }` (or flat `dirs: ["...", ...]`).
   - **Prolog KB:** extend `Orchestration/Memories/kb.pl` with facts such as `directory(Path)`, `child_dir(Parent, Child)`, `file_in(File, Dir)` so agents can query "which dirs exist?" or "where is X?" without scanning the repo.

3. **When to build or update:**
   - **Session start:** run a directory scan (e.g. `find . -type d` or equivalent, or a small script that walks the tree and writes JSON) and write the index.
   - **On demand:** when an agent needs "whereabouts" and the index is missing or stale, build it then.
   - **After structure change:** optionally refresh after commits or file creation that add/remove dirs (can be deferred to next session or on-demand).

4. **How agents use it:**
   - **Before asserting "dir X exists" or "there is no such dir":** read the index (or query the KB); if the path is in the index, it exists; if not, then glob or run_terminal_cmd to confirm (and optionally update the index).
   - **"Where might X be?":** search the index for paths containing X (or query KB), then verify with a tool if needed. Avoids guessing only top-level dirs and missing subdirs.

## Relation to existing pieces

- **FILE_STRUCTURE_VERIFICATION:** Agents must still **verify** with a tool when in doubt; the index is a **fast first source** for "whereabouts". If the index says "no such path", still confirm with glob or `test -d` before asserting it doesn't exist (index may be stale).
- **Prolog KB (MENTAL_MAP):** The KB already stores relational facts (references, dependencies, traceability). Adding `directory/1` and `child_dir/2` (and optionally `file_in/2`) makes the KB the canonical "which dirs exist and where" store; the same file can hold both reference facts and structure facts.
- **CHATBOT "repo map":** CHATBOT says "Build a repo map (tree-sitter or directory scan) for project awareness". This document defines one concrete form of that: a **workspace structure index** as a data structure (file or KB) that persists and is reused so agents don't miss subdirs.
- **Aider / tree-sitter:** External tools (e.g. Aider) use tree-sitter for a repo map. Here the technique is **format-agnostic**: any method that produces a directory (and optionally file) list or tree and writes it to a known location or KB qualifies.

## Minimal implementation (file-based)

- **Script or command:** e.g. `find . -type d` (Unix) or a small script that outputs a JSON array of directory paths relative to workspace root. Optionally include a shallow file list for key names (e.g. `package.json`, `README.md`).
- **Output path:** `Orchestration/Memories/workspace_structure.json` (or per-project config). Schema example:
  ```json
  { "root": ".", "dirs": [".", "Orchestration", "Orchestration/Harness", "Orchestration/Skills", "Project", ...], "updated": "ISO8601" }
  ```
- **Agent instruction:** At session start or before answering "does X exist?" or "where is Y?", read this file if present. Use it to know all known dirs; if the path in question is in `dirs`, it exists; if not, run glob or `test -d` and optionally append to the index.

## Minimal implementation (Prolog KB)

- In `kb.pl`, add (or ensure) facts:
  - `directory(Path)` for each directory path.
  - `child_dir(Parent, Child)` for each parent/child pair (e.g. `child_dir('Orchestration', 'Orchestration/Harness')`).
- **Query:** "Is there a dir at path P?" → `directory(P)`. "Which dirs are under Orchestration?" → `child_dir('Orchestration', X)` or transitive closure.
- **Update:** Same as file-based: run a directory scan at session start or on demand; emit Prolog facts and append/write to `kb.pl` (or a dedicated `structure.pl` included by kb.pl).

## Summary

| Problem | Technique |
|--------|-----------|
| Agents miss dirs in subdirs | Maintain a **workspace structure index** (list or tree of dir paths, optionally key files) in a known place (JSON file or Prolog KB). |
| "Does dir X exist?" | Check index first; if present, yes; if absent, verify with Glob or `test -d` and optionally update index. |
| "Where is X?" | Search index for paths containing X; then verify with a tool if needed. |
| When to build | Session start, on demand, or after structure-changing operations. |

This gives the harness a **data structure for whereabouts** instead of relying only on glob, so agents are less likely to miss subdirs or hallucinate structure.
