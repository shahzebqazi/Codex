# Branch list and descriptions

Quick reference for what each branch is for. Generated from branch names and recent commits.

---

## Primary / trunk

| Branch | Description |
|--------|-------------|
| **main** | Stable, human-approved code. Default branch. CI/Pages deploy from here. |
| **production** | Release/production line. CI runs on PRs to this and main. |

---

## Feature and topic branches

| Branch | Description |
|--------|-------------|
| **desktop-app** | Desktop app and GUI features (e.g. Mockups, desktop-specific UI). Keep in sync with main for non-GUI code. |
| **benchmarks** | Benchmarking-related work. |
| **assets** | Asset and static content (e.g. Pages, koi-pond Mockups). |
| **docs/documentation-reorg** | Documentation restructure and doc-only changes. |
| **docs/documentation-reorg-project** | (Remote only.) Alternate or follow-up docs reorg. |
| **docs/template-plugin-positioning** | (Remote only.) Template and plugin positioning for docs. |
| **experimental-ai-coding-branch** | Experimental AI coding and workflow changes (e.g. CI paths, layout). |

---

## AGI-research (from former agi-research repo)

These branches were brought in from the deleted `agi-research` repo. They are research/toolkit-focused and diverge from main.

| Branch | Description |
|--------|-------------|
| **agi-research** | Main line of the former agi-research repo (research merge). |
| **agi-research-benchmarking** | AI Research Toolkit: benchmarking and related prompts (`.ai/prompts`). |
| **agi-research-training** | AI Research Toolkit: training and related prompts (`.ai/prompts`). |
| **agi-research-research** | Research docs and copy: humanized copy, links, research/training/benchmark-only workflow (no PR workflow). |
| **agi-research-libra-ai** | Libra AI / Library-Librarian content and exploration. |
| **agi-research-type-system** | Type system and web framework exploration. |

---

## Remotes

- **origin** — GitHub (e.g. `shahzebqazi/Codex`). Most branches are pushed here.
- **agi-research** — Legacy remote pointing at the old agi-research repo URL (repo deleted). Safe to remove: `git remote remove agi-research`.

---

## Conventions (from [GIT.md](../../Tasks/VCS/GIT.md))

- `main` = stable; `production` = release.
- Human feature branches: `feature/name`, `docs/name`, `chore/name`, `hotfix/name`.
- Agent work: `agent-name/task-id` (often managed by jj).
