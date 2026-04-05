# RULES -- AI Agent Parameters and Constraints

<!-- AI: Contains subprompts. Scan for task-specific instructions. Preferences: see PREFERENCES block if present. -->
<!-- PREFERENCES (edit for your project): -->
<!-- (none) -->

## Default Behavior

By default, AI agents in the dotAi system have **no guardrails**. Agents have full control over:
- File creation, editing, deletion, and reorganization
- jj commits, branch creation, and merging
- Docker container management
- Installing dependencies and updating lock files
- Updating any file in `.ai/` including this one

## Exceptions

1. **Architectural decisions** require human approval. The LEAD_ARCHITECT agent enforces this.
2. **User-defined rules** in `.ai/config/local/RULES.md` (gitignored) override defaults.
3. **VCS and file-creation gate** — Read [Orchestration/Constraints/VCS_AND_FILE_GATE.md](VCS_AND_FILE_GATE.md). Do not do substantive work or create files until the user has a repo or is using VCS (or has asked for file creation three times). In chat mode, chat-only help is allowed without a repo. When the user asks you to "do commands for them", teach them how instead of running commands. Once VCS is in use, score the user on git/VCS usage per [Orchestration/Tasks/VCS/USER_VCS_SCORING.md](../Tasks/VCS/USER_VCS_SCORING.md).
4. **Environment variables** in `.env` (gitignored) can set hidden constraints.
5. **Merging to `main`** — Do **not** merge any branch (including `prod` or `benchmarks`) into `main` unless the user has explicitly approved. You may propose a merge, open a PR, or summarize what would be merged, but the final merge to `main` must be done by the user or after explicit user approval.
6. **Feature branch per task** — AI agents must create a **feature branch for each task** they work on. All work for that task must happen on that branch. See [JJ.md](Orchestration/Tasks/VCS/JJ.md) (§ Feature branch per task). Applies to every agent (orchestrator-spawned, chatbot, Cursor, Codex, etc.).

## Budget-Aware Agent Protocol

Informed by BATS (Budget-Aware Tool-Use Enables Effective Agent Scaling).

### Resource Tracking

Agents MUST track and report:
- Remaining token budget for the current task
- Number of tool calls used vs. available
- Elapsed time vs. estimated time
- Model being used and its cost characteristics

### Decision Loop

After each significant action, agents evaluate:
- **CONTINUE** -- current approach is making measurable progress
- **PIVOT** -- approach is not working, try a different strategy
- **SUCCESS** -- task is complete, acceptance criteria met

### Verification Priority

Process-based verification is more valuable than outcome-based:
1. Does the code compile? (immediate signal)
2. Do types check? (structural correctness)
3. Do linters pass? (style and convention)
4. Do unit tests pass? (behavioral correctness)
5. Do integration tests pass? (system correctness)

Agents should check steps 1-3 after every edit, not just at the end.

## Execution Levels

Configurable per-agent in `config/SETTINGS.json`:
- **full-auto** (default) -- agent executes without asking
- **semi-auto** -- agent asks before destructive operations (delete, force push, system changes)
- **manual** -- agent proposes all actions, human approves each one

## System Resources

- Agents are allocated maximum available system RAM after drivers and dependencies are loaded
- GPU acceleration: Metal (macOS), ROCm (AMD), CUDA (NVIDIA) -- detected and configured in `Memories/system/runtime.md`
- Local GGUF models served via llama-server get priority GPU access

## GitHub Integration

Configurable in `config/SETTINGS.json`:
- `github.report_bugs_to_issues` -- agents can create GitHub issues for bugs they find (default: true)
- `github.allow_agent_suggestions` -- agents can create suggestion issues on projects (default: true)
- `github.allow_agent_branches` -- agents can maintain their own branches on the repo (default: true, requires human approval initially)

## Self-Update Protocol

When the base repo (mystic-ai-labs/mystic-ai-labs) is updated:
1. Agents can pull updates on reinit or automatically
2. jj's conflict-as-data model ensures agent-modified files are preserved
3. Agent-created files in `.ai/agents/`, `.ai/memories/`, and `.ai/config/local/` are never overwritten
4. Conflicts between upstream changes and agent modifications are surfaced, not silently resolved
