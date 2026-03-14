# Branch and Documentation Refactor — Implementation Plan

> **For Claude:** Use superpowers:executing-plans to implement this plan task-by-task if executing in a separate session.

**Goal:** Merge documentation branches into desktop or main, reduce branch/doc inconsistencies, and refactor into a clear branching structure plus a single doc tree (Mystic PRDs, docs for devs, docs for users, dependencies).

**Architecture:** Treat **desktop-app** (Orchestration), **assets** (Asset generators), and **docs/documentation-reorg** (Mockups) as the canonical sources for their domains. Consolidate into one primary development branch (desktop-app or main), keep **assets** as a content-only branch that the Pages workflow fetches, and retire or merge **docs/documentation-reorg** and **docs/documentation-reorg-project**. Unify all product/process docs under one root (**Documents/**) with clear subdirs for PRDs (Mystic), dev docs, user docs, and dependency/reference docs.

**Tech Stack:** Git, GitHub Actions (deploy-mockups-pages), existing Markdown/PRD layout.

---

## Current state (summary)

| Branch | Canonical content (your mapping) | Notes |
|--------|----------------------------------|--------|
| **desktop-app** | Orchestration | Tasks, Harness, Agents, Constraints, Memories |
| **assets** | Asset generators + Brand Guide output | `Assets/` Kotlin toolchain, `Assets/output/preview/` |
| **docs/documentation-reorg** | Mockups | Mockups app, handoffs, Pages deploy trigger |
| **main** | Stable trunk | CI/Pages on push; deploy workflow fetches `Assets/output/` from **assets** |
| **docs/documentation-reorg-project** | (Remote only) | Alternate/follow-up reorg; not in workflow |

**Doc naming bug:** Repo has **Documentation/** on disk (PRDs, Requirements, References, UserStories, Plans, Reports, Papers). START_HERE and other files reference **Documents/** (PRDs, UserStories, Requirements, etc.). These should be one canonical name and path.

---

## Target state

### Branching

1. **main** — Default; stable, human-approved. After refactor: contains Orchestration, Mockups app, workflow, and **pointers** to where docs live (unified Documents).
2. **desktop-app** — Either merged into main (if it becomes the primary line) or kept in sync with main for GUI/desktop-specific work; one of these is the “dev” branch.
3. **assets** — Kept as-is: asset generators + `Assets/output/`. No code reorg here; deploy workflow continues to `git fetch origin assets` for `Assets/output/`.
4. **docs/documentation-reorg** — Merged into main (or desktop-app). All Mockups and doc-structure changes brought in; branch deleted or deprecated.
5. **docs/documentation-reorg-project** — Decide: merge useful bits into main then delete, or delete if redundant.
6. **docs/template-plugin-positioning** — Decide: merge or delete (optional follow-up).

### Doc structure (single root: Documents/)

Use **Documents/** as the single canonical root (rename **Documentation/** → **Documents/** and fix all references). Subdirs:

| Path | Purpose | Contents (examples) |
|------|---------|----------------------|
| **Documents/PRDs/** | Mystic / product PRDs | MVP_PRD, FEATURES_PRD, HOTKEYS_PRD, etc.; Mystic-specific PRDs only |
| **Documents/ForDevs/** | Developer-facing | Architecture, API audit, harness spec refs, CONTRIBUTING, build/deploy, branch policy |
| **Documents/ForUsers/** | User-facing | User stories, whitepaper, release notes, “how to use” |
| **Documents/Requirements/** | Shall-statements, traceability | Functional, NFR, TRACEABILITY.md (unchanged structure) |
| **Documents/References/** | External links, ralph, URLs | URLS.md, ralph/ (unchanged) |
| **Documents/Plans/** | Implementation/design plans | Date-named plans (this file) |
| **Documents/Reports/** | Reviews, audits | AI_SYSTEM_COMPATIBILITY_REVIEW, RELEASE_READINESS, API_AUDIT_MVP |
| **Documents/Prompts/** | Authored prompts | (create if missing; START_HERE says canonical here) |
| **Documents/UML/** | Use case / class diagrams | (create when automation produces them) |
| **Documents/DomainModel/** | Glossary, concepts | (create when automation produces them) |

**Dependencies:** Either a single **Documents/Dependencies.md** (or **Documents/ForDevs/Dependencies.md**) listing key stacks (Node, Kotlin, Vite, Ollama, etc.) and where they’re used, or keep references in ForDevs and in READMEs (Mockups, Assets, Orchestration).

---

## Merge strategy (order)

1. **Audit** — On a temp branch or locally, diff docs/documentation-reorg vs main (and vs desktop-app) for: `Mockups/`, `Documentation/`, `Orchestration/` (if any), `.github/workflows/deploy-mockups-pages.yml`. List files that exist only on docs/documentation-reorg or have different content.
2. **Resolve canonical** — For each conflict or difference, decide: keep main’s, keep docs/documentation-reorg’s, or merge manually. Document decisions in a short checklist.
3. **Merge docs/documentation-reorg into main** (or into desktop-app first, then desktop-app → main). Prefer one PR: “Merge docs/documentation-reorg into main” with the checklist completed.
4. **Optional:** Merge **docs/documentation-reorg-project** into main if it has unique changes; otherwise delete.
5. **Rename Documentation → Documents** on the chosen trunk (main or desktop-app) and add the ForDevs/ForUsers split (move files per table above).
6. **Update all references** from `Documentation/` and `Documents/` to the single `Documents/` layout (START_HERE, README, CONTRIBUTING, DOCUMENTATION_AUTOMATION, TRACEABILITY, etc.).
7. **Update BRANCHES.md** and any CI/workflow comments to describe the new branch policy and that docs live under Documents/.

---

## Bite-sized tasks

### Phase 1: Audit and decide

**Task 1.1: List branch differences**

- **Files:** None created; use git.
- **Step 1:** From repo root, run:
  ```bash
  git fetch origin main docs/documentation-reorg desktop-app assets 2>/dev/null || true
  git diff origin/main origin/docs/documentation-reorg --stat
  ```
- **Step 2:** Save output to `Documentation/Plans/2026-03-14-branch-diff-reorg-vs-main.txt` (or a temp file).
- **Step 3:** Run:
  ```bash
  git diff origin/main origin/desktop-app --stat
  ```
- **Step 4:** Save to `Documentation/Plans/2026-03-14-branch-diff-desktop-vs-main.txt`.
- **Commit:** `docs(plans): add branch diff artifacts for refactor`

**Task 1.2: Create merge checklist**

- **Create:** `Documentation/Plans/2026-03-14-merge-checklist.md`
- **Content:** Table of paths that differ (from Task 1.1). Columns: Path, Keep (main | reorg | merge), Notes. Fill “Keep” and “Notes” after reviewing diffs.
- **Commit:** `docs(plans): add merge checklist for docs reorg branch`

---

### Phase 2: Merge docs/documentation-reorg

**Task 2.1: Merge docs/documentation-reorg into main**

- **Modify:** Repo state via git (no file edits in repo tree yet).
- **Step 1:** `git checkout main && git pull origin main`
- **Step 2:** `git merge origin/docs/documentation-reorg -m "Merge docs/documentation-reorg into main (branch consolidation)"`
- **Step 3:** Resolve conflicts using the merge checklist; prefer reorg version for Mockups and Documentation layout if that was the canonical.
- **Step 4:** Run any existing CI (e.g. `branch-policy.yml`) and fix if needed.
- **Commit:** Resolve and `git add` then `git commit` (or `jj` equivalent).
- **Step 5:** Push to `origin main` (or open PR from a branch that has the merge).

**Task 2.2: Update deploy workflow trigger (if needed)**

- **Modify:** `.github/workflows/deploy-mockups-pages.yml`
- **Step 1:** Ensure `on.push.branches` includes `main` (it already does). If you merged into desktop-app first, add `desktop-app` to the list until desktop-app is merged to main.
- **Commit:** `ci: ensure Pages deploy runs on main after reorg merge`

---

### Phase 3: Rename Documentation → Documents and add ForDevs/ForUsers

**Task 3.1: Rename folder**

- **Rename:** `Documentation/` → `Documents/`
- **Step 1:** `git mv Documentation Documents` (or `mv Documentation Documents && git add -A`)
- **Step 2:** Verify no broken links yet (links will be fixed in Task 3.3).
- **Commit:** `refactor(docs): rename Documentation to Documents`

**Task 3.2: Create ForDevs and ForUsers and move files**

- **Create:** `Documents/ForDevs/`, `Documents/ForUsers/`
- **Move (examples):**
  - Into **ForDevs:** Reports and Reviews (e.g. API_AUDIT_MVP, AI_SYSTEM_COMPATIBILITY_REVIEW, RELEASE_READINESS_REPORT), UX_WIREFRAME_AGENT_PROMPT (dev-facing), and a new **Documents/ForDevs/Dependencies.md** (optional) listing Node, Kotlin, Vite, Ollama, etc.
  - Into **ForUsers:** UserStories, Papers (WHITEPAPER), and any user-facing release notes or “how to use” content.
- **Files to create:** `Documents/ForDevs/README.md` (one-line: “Developer-facing docs: architecture, APIs, harness, dependencies”), `Documents/ForUsers/README.md` (one-line: “User-facing docs: stories, whitepaper, guides”).
- **Commit:** `refactor(docs): add ForDevs and ForUsers and move existing docs`

**Task 3.3: Update all references to Documentation/ and Documents/**

- **Modify (exact paths):**
  - `START_HERE.md` — Replace every `Documents/` link so it points to the new layout (Documents/PRDs/, Documents/ForDevs/, Documents/ForUsers/, Documents/Requirements/, Documents/References/, Documents/Plans/, Documents/Prompts/, Documents/UML/, Documents/DomainModel/). Fix any lingering “Documentation/” if present.
  - `README.md` — Update PRDs and references path to `Documents/`.
  - `CONTRIBUTING.md` — Update `Documents/PRDs/` reference.
  - `Orchestration/Tasks/PM/DOCUMENTATION_AUTOMATION.md` — Replace all `Documents/` paths to use the new subdirs (UserStories, UseCases, UML, DomainModel, Requirements, etc. under Documents/).
  - `Documentation/Requirements/TRACEABILITY.md` → **Documents/Requirements/TRACEABILITY.md** — Fix self-references and links to UserStories/Requirements (paths under Documents/).
  - `Documentation/Requirements/README.md` → **Documents/Requirements/README.md** — Update links to Documents/UserStories, etc.
  - `Documentation/Requirements/Functional/FUNCTIONAL_REQUIREMENTS.md` — Update `Documents/` paths.
  - `Documentation/Requirements/NFR/NON_FUNCTIONAL_REQUIREMENTS.md` — Update `Documents/` paths.
  - `Documentation/UserStories/USER_STORIES.md` — Update traceability link to Documents/Requirements/TRACEABILITY.md.
  - `Documentation/Reports and Reviews/*.md` — After move to Documents/ForDevs/ or Documents/Reports/, update internal links.
  - `Orchestration/Harness/README.md` — Update `Documents/PRDs/` (and Project/Documents/PRDs/ if present) to `Documents/PRDs/`.
  - `Orchestration/Harness/HARNESS_SPEC.md` — Update Documents/PRDs reference.
  - `Documentation/UX_WIREFRAME_AGENT_PROMPT.md` (now under Documents/ForDevs/ or Documents/) — Update Project/Documents/ paths to Documents/.
  - `Extensions/Huggingface/HUGGINGFACE.md` — Update Documents/References/URLS.md (should already be Documents/ after rename).
- **Step 2:** Grep for `Documentation/` and `Documents/` across the repo and fix any remaining references.
- **Commit:** `docs: fix all references to Documents/ layout and ForDevs/ForUsers`

**Task 3.4: Update BRANCHES.md and doc index**

- **Modify:** `Orchestration/Harness/Documents/BRANCHES.md`
- **Content:** Update “Feature and topic branches” to reflect: main (or main + desktop-app) as primary dev line; assets for asset generators only; docs/documentation-reorg merged and deprecated; docs/documentation-reorg-project merged or removed. Add one line: “All product and process docs live under **Documents/** (PRDs, ForDevs, ForUsers, Requirements, References, Plans).”
- **Optional:** Add **Documents/README.md** (or update if present) as an index: short table linking to PRDs, ForDevs, ForUsers, Requirements, References, Plans, Reports.
- **Commit:** `docs: update BRANCHES.md and Documents index for refactor`

---

### Phase 4: Mystic PRDs and dependencies

**Task 4.1: Consolidate Mystic PRDs under Documents/PRDs/**

- **Modify:** Ensure all Mystic/product PRDs live only under `Documents/PRDs/` (they already do after rename). Remove or archive any duplicate PRDs that might exist on other branches after merge.
- **Files:** `Documents/PRDs/README.md` or a short index listing: MVP_PRD, FEATURES_PRD, HOTKEYS_PRD, AI_SYSTEM_COMPATIBILITY_PRD, SESSION_PRD_PROMPT, README_REWRITE_PRD, etc. Add one-sentence purpose for each.
- **Commit:** `docs(PRDs): index Mystic PRDs under Documents/PRDs`

**Task 4.2: Add or update Dependencies doc**

- **Create or modify:** `Documents/ForDevs/Dependencies.md`
- **Content:** List key stacks (e.g. Node 20, Kotlin/Gradle for Assets, Vite for Mockups, Ollama for harness, Python for harness, Lua for GUI), where each is used (Mockups, Assets, Orchestration), and link to package files (package.json, build.gradle.kts, etc.).
- **Commit:** `docs(ForDevs): add Dependencies overview`

---

### Phase 5: Cleanup and verification

**Task 5.1: Delete or deprecate merged branches (remote)**

- **Action:** After merge is on main and verified, delete or deprecate `docs/documentation-reorg` (and optionally `docs/documentation-reorg-project`) on remote. Prefer “delete” only after confirmation that no unique work is lost.
- **Command (example):** `git push origin --delete docs/documentation-reorg` (run only when safe).

**Task 5.2: Verification**

- **Step 1:** Open START_HERE.md and click every Documents/ link; ensure targets exist.
- **Step 2:** Run a full-text grep for `Documentation/` (old path); expect zero hits except in this plan or in history.
- **Step 3:** Trigger or run deploy workflow (push to main or workflow_dispatch); confirm Pages build succeeds and Brand Guide still loads from assets branch.
- **Commit:** None; verification only. If fixes needed, add a small commit.

---

## Summary

| Phase | What |
|-------|------|
| 1 | Audit branch diffs; create merge checklist |
| 2 | Merge docs/documentation-reorg into main; adjust workflow if needed |
| 3 | Rename Documentation → Documents; add ForDevs/ForUsers; move files; fix all references; update BRANCHES.md |
| 4 | Index Mystic PRDs; add Dependencies doc |
| 5 | Delete/deprecate merged branches; verify links and deploy |

**Execution options:** Subagent-driven (this session, task-by-task) or parallel session with executing-plans in a dedicated worktree.
