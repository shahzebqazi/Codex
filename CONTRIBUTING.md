# Contributing to .ai

<!-- AI: Contains subprompts. Scan for task-specific instructions. Preferences: see PREFERENCES block if present. -->
<!-- PREFERENCES (edit for your project): -->
<!-- (none) -->

Thanks for your interest in the .ai system (this base repo). This guide explains how the repo works as a base and how humans and AI agents can contribute.

## Branch model and workflow

- **main** — Docs and process only (README, CONTRIBUTING, LICENSE, docs). Default branch for landing and documentation.
- **development** — Nightly builds and active development. Feature and contributor branches merge here.
- **Production** — Releases. Promoted from `development` when stable; do not commit directly to Production.

Flow: open a feature or contributor branch from `development`, do work, then merge (or open a PR) into `development`. Releases are promoted from `development` to `Production`.

## Interactive rebase before merging into development

Before merging your feature or contributor branch into `development`:

1. **Rebase onto development** — `git fetch origin development && git rebase origin/development` (or equivalent).
2. **Use interactive rebase to clean history** — `git rebase -i origin/development` (or squash onto a single commit if preferred). During the rebase:
   - **Squash** fixup and WIP commits into logical steps.
   - **Reword** commit messages so they are intention-rich and useful for future AI-assisted evolution (what changed and why).
   - **Keep** distinct logical steps as separate commits where it helps readability.
3. **Branch protection** — Do not push directly to `Production`. All changes reach Production via `development` after review/promotion.

## Adding this base repo to your project

This repository is a **base repo**: a template and shared foundation you can add to any project. When you add it (e.g. by cloning, copying the `.ai/` tree, or using it as a template):

- Your project gets the full **.ai** system: markdown-first agent orchestration, skills, rules, and local GGUF inference.
- AI agents that work in your repo read the START_HERE (or equivalent) in your chosen AI operation directory and use the same conventions (skills, config).
- You can customize per project via `config/local/`, `project/RULES.md`, and your own PRDs under `project/`.

No protocol servers or cloud APIs are required—just the AI operation directory and (optionally) Docker for local models. See [README — AI operation directory](README.md#ai-operation-directory) for supported directory names and how to specify a custom path.

### AI directory compatibility

Supported names for the AI operation root (single directory per project, or use a user-specified path override):

| Name        | Notes                    |
|------------|--------------------------|
| `Project/Ai/` | Project-scoped          |
| `.ai/`     | Default in this repo     |
| `dotai`    | No leading dot           |
| `.AI/`     | Uppercase                |
| `AI`       | Uppercase, no slash      |
| `ai`       | Lowercase                |
| Custom path| Override via config/env  |

**Precedence when multiple exist:** use explicit user-specified path if set; otherwise tools may auto-detect using a deterministic order (e.g. `.ai/` then `Project/Ai/` then `dotai` then `.AI/` then `AI` then `ai`). Document the chosen order in your tool’s docs so behavior is predictable.

## AI intake agent workflow

When an AI agent is helping you capture iterations and research:

1. **Intake loop** — The agent prompts you to place iteration artifacts (prompt files, scaffold snippets, component docs, findings notes) into a **recognized operation directory** (any of the names above, e.g. `.ai/`, `Project/Ai/`, or your custom path).
2. **Expected artifact types** — Prompt files (e.g. `.md`), scaffold snippets, component documentation, and short findings notes. The agent may suggest subdirectories (e.g. `inbox/`, `prompts/`, `findings/`) for organization.
3. **Repeatable cycle** — Collect artifacts into that directory → classify and normalize names → summarize (e.g. in a taxonomy or index) → commit with intention-rich messages. The agent should ask you to add files to the chosen directory when it needs more input or when compiling research.

## Prompt and commit conventions

- **Files as prompts** — Any file in the repo can serve as a prompt surface; agents may read markdown, code, and config to infer intent and next steps.
- **Comments for AI** — Code and config comments may include AI guidance or subprompts (e.g. `<!-- AI: ... -->` in markdown, or inline instructions in code). Use them to steer agents without changing runtime behavior.
- **Commit messages** — Write intention-rich messages (what changed and why) so future AI-assisted work can reason about history. Prefer this over vague one-liners.

## How AI agents use GitHub issues

When this base repo is used in a project that hosts its code on GitHub, AI agents can integrate with GitHub Issues so that findings and suggestions are tracked in one place.

- **Bug reports** — Agents can open issues for bugs they find (config: `github.report_bugs_to_issues` in `.ai/config/SETTINGS.json`, default on).
- **Suggestions** — Agents can open issues for feature or improvement suggestions (config: `github.allow_agent_suggestions`, default on).
- **Branches** — Agents can use branches for their work; human approval can be required initially (config: `github.allow_agent_branches`).

So: add this base repo to your project, point your AI agent at `.ai/START_HERE.md`, and the agent can both do work in the repo and report bugs or ideas via GitHub issues. Details and overrides live in `.ai/project/RULES.md` and `.ai/config/SETTINGS.json`.

## How to contribute (humans)

- **Code and docs** — Open a pull request from a branch. Prefer small, focused PRs.
- **Bugs and ideas** — Open a [GitHub issue](https://github.com/shahzebqazi/Codex/issues). Use the issue templates if present (e.g. bug report, feature suggestion).
- **Base repo changes** — Keep backward compatibility in mind; this repo is a template and foundation for other projects. See `.ai/project/BASE_REPO_GUIDELINES.md` for design principles.

## How to contribute (AI agents)

- Read `.ai/START_HERE.md` first.
- Follow `.ai/project/RULES.md` and respect `config/SETTINGS.json` (including GitHub integration settings).
- For bugs or suggestions, create GitHub issues when allowed by config; use the repository’s issue templates when available.

## License

Contributions are made under the same license as the project. See [LICENSE](LICENSE) for details.
