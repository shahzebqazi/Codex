For AI readers: If you have not already, read START_HERE.md first. For the contributing guide in context, see Orchestration/Memories/MENTAL_MAP.md.

# Contributing to .ai

<!-- AI: Contains subprompts. Scan for task-specific instructions. Preferences: see PREFERENCES block if present. -->
<!-- PREFERENCES (edit for your project): -->
<!-- (none) -->

Thanks for your interest in the .ai system (this base repo). This guide explains how the repo works as a base and how humans and AI agents can contribute.

## Adding this base repo to your project

This repository is a **base repo**: a template and shared foundation you can add to any project. When you add it (e.g. by cloning, copying the `Project/` tree, or using it as a template):

- Your project gets the full dotAi system: markdown-first agent orchestration, tasks (under `Orchestration/Tasks/`), rules, and local GGUF inference.
- AI agents that work in your repo read `Project/START_HERE.md` and use the same conventions (jj, tasks, config).
- You can customize per project via local overrides and `Orchestration/Constraints/RULES.md`; PRDs live under `Documents/PRDs/`.

No protocol servers or cloud APIs are required—just the `Project/` directory and (optionally) Docker for local models.

## How AI agents use GitHub issues

When this base repo is used in a project that hosts its code on GitHub, AI agents can integrate with GitHub Issues so that findings and suggestions are tracked in one place.

- **Bug reports** — Agents can open issues for bugs they find (config: `github.report_bugs_to_issues` in `Orchestration/Memories/SETTINGS.json`, default on).
- **Suggestions** — Agents can open issues for feature or improvement suggestions (config: `github.allow_agent_suggestions`, default on).
- **Branches** — Agents can use branches for their work; human approval can be required initially (config: `github.allow_agent_branches`).

So: add this base repo to your project, point your AI agent at `Project/START_HERE.md`, and the agent can both do work in the repo and report bugs or ideas via GitHub issues. Details and overrides live in `Orchestration/Constraints/RULES.md` and `Orchestration/Memories/SETTINGS.json`.

## How to contribute (humans)

- **Code and docs** — Open a pull request from a branch. Prefer small, focused PRs.
- **Bugs and ideas** — Open a [GitHub issue](https://github.com/shahzebqazi/Codex/issues). Use the issue templates if present (e.g. bug report, feature suggestion).
- **Base repo changes** — Keep backward compatibility in mind; this repo is a template and foundation for other projects. See the README (Base repo guidelines section) for design principles.

### Branch and release flow

- **Feature work** — Branch from `main` using `feature/*` (or `docs/*`, `chore/*`) and open PRs into `main`.
- **Release promotion** — Promote through PRs from `main` to `Production`.
- **Hotfixes** — Branch from `Production` using `hotfix/*`, merge into `Production`, then back-merge into `main`.
- **Development branch** — `development` is transitional and retained temporarily for in-flight work only; avoid creating new work on it.

## How to contribute (AI agents)

- Read `Project/START_HERE.md` first.
- Follow `Orchestration/Constraints/RULES.md` and respect `Orchestration/Memories/SETTINGS.json` (including GitHub integration settings).
- For bugs or suggestions, create GitHub issues when allowed by config; use the repository’s issue templates when available.
- Prefer feature branches that target `main`; use `main -> Production` PRs for release promotions.

## License

Contributions are made under the same license as the project. See [LICENSE](LICENSE) for details.
