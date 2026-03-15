# FILE_STRUCTURE_VERIFICATION — Avoid Misalignments and Hallucinations

<!-- AI: Read before asserting that a path, file, or directory exists. Verify with a tool call; do not rely on documentation or memory alone. -->

## Purpose

Avoid misalignments and hallucinations about the program's file structure. Documentation and system prompts describe an *intended* layout; the actual workspace may differ (branch differences, renames, missing dirs). Always **ground** claims about existence or contents with a **tool call**.

## Rule

**Do not assert that a path, file, or directory exists (or does not exist) based only on:**
- Documentation (README, PRDs, this file tree)
- Previous conversation or memory
- Assumptions about "standard" layout

**Before stating that a path exists, listing its contents, or creating files under it:** use a **tool** to verify. If the tool fails or returns empty, treat the path as missing or empty and respond accordingly.

## Tool calls by environment

### In Cursor IDE

| Intent | Tool to use | How |
|--------|-------------|-----|
| Check if a **directory** exists | **Glob** or **run_terminal_cmd** | **Glob:** `target_directory` = the path, `glob_pattern` = `*` or `**/*`. If results are returned, the directory exists (and you get contents). Empty result or error suggests it does not exist or is empty. **run_terminal_cmd:** `test -d "<path>"` (Unix) or `if exist "<path>"` (Windows); exit code 0 = exists. |
| Check if a **file** exists | **Read** or **Glob** | **Read:** Try to read the file; success = exists. **Glob:** Use a pattern that matches the filename under the parent dir. |
| List contents of a directory | **Glob** | Set `target_directory` to the directory path and `glob_pattern` to `*` or `**/*` (depending on whether you need recursive). Use results as the list of files/dirs. |
| Search for files by name or content | **Grep** or **Glob** | **Grep** for content or path patterns; **Glob** for filename patterns. |

There is no separate `list_dir` in Cursor's default tool set. **Glob** with `target_directory` + `glob_pattern` is the way to list a directory or confirm it exists.

### Generic / other runtimes

- If **list_dir** or **list_directory** is available, use it for directory existence and listing.
- If **run_terminal_cmd** (or equivalent) is available: `test -d <path>` (Unix) or `ls <path>`.
- **read_file** / **Read** only confirms a *file* exists when the read succeeds; do not use it to assert that a *directory* exists.

## When to verify

- User asks "does X exist?" or "is there a folder Y?" → run a tool, then answer from the result.
- Before creating a file under a path → verify the parent directory exists (or create it via a tool/command).
- Before referencing "the files in Z" or "the structure of Z" → list Z with a tool first.
- When documentation says "artifacts live under P" → confirm P exists in this workspace before writing there.

## Summary

| Claim | Action |
|-------|--------|
| "Directory D exists" | Use **Glob** (target_directory=D, pattern=*) or **run_terminal_cmd** (`test -d D`) first. |
| "File F exists" | Use **Read** on F or **Glob** for F. |
| "D has no contents" | Use **Glob** on D; empty result = no contents. |
| "The project layout is X" | Prefer tool-based discovery (Glob/list_dir) over doc-based assertion. |

This reduces hallucinations and keeps agent behavior aligned with the actual workspace.

## When a workspace structure index exists

Some setups maintain a **workspace structure index** (a data structure listing directories and optionally key files) so agents have "whereabouts" without globbing every time. See [Orchestration/Memories/WORKSPACE_STRUCTURE_INDEX.md](../../WORKSPACE_STRUCTURE_INDEX.md). If that index exists (e.g. `Orchestration/Memories/workspace_structure.json` or directory facts in `kb.pl`):

- **Use it first** to answer "does dir X exist?" or "where might X be?" — check the index; if the path is listed, treat it as existing (and optionally still verify with a tool when the index might be stale).
- **Still verify** when the index says a path is absent (it may be new or the index stale); then run Glob or `test -d` and consider updating the index.
- This reduces missed subdirs because the index holds the full tree (or flat list of dirs), not only what the agent has globbed so far.
