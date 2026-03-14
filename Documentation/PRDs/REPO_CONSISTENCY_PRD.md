---
name: "Repo Consistency Remediation — Critical Structural Fixes"
overview: "Multiple agents made structural changes (directory moves, submodule removal, file deletions) without updating all references. This PRD captures every broken path, duplicate, and inconsistency found during a full-repo audit, and defines the fixes needed to restore a consistent, navigable project."
todos:
  - id: decide-product-location
    content: "DECISION REQUIRED: Keep Product/ at repo root (current state) or re-create Project/Product/ to match docs. All path fixes depend on this decision."
    status: pending
  - id: decide-constraints-location
    content: "DECISION REQUIRED: Keep constraints in Orchestration/Memories/ (current state) or restore Orchestration/Constraints/ as its own directory. All constraint-path fixes depend on this decision."
    status: pending
  - id: fix-system-prompt-paths
    content: "CRITICAL: Fix all broken paths in Orchestration/Harness/SYSTEM_PROMPT.md — Constraints/ references, Project/Product/ references, Extensions casing (HuggingFace→Huggingface, OpenAI→Openai)"
    status: pending
  - id: fix-readme-paths
    content: "CRITICAL: Fix root README.md — references to Project/, Orchestration/Constraints/RULES.md, START_HERE.md, and directory layout diagram"
    status: pending
  - id: fix-documents-readme
    content: "CRITICAL: Fix Documents/README.md (and Orchestration/Harness/Documents/README.md) — all Project/Product/ references point to wrong location; SYSTEM_PROMPT.md link is wrong"
    status: pending
  - id: fix-memories-constraints-links
    content: "CRITICAL: Fix broken relative links in Orchestration/Memories/Constraints.md — VCS_AND_FILE_GATE.md href, JJ.md href"
    status: pending
  - id: fix-vcs-gate-links
    content: "CRITICAL: Fix broken relative link in Orchestration/Memories/prompts/Constraints/VCS_AND_FILE_GATE.md — USER_VCS_SCORING.md href"
    status: pending
  - id: fix-harness-readme-paths
    content: "Fix Orchestration/Harness/README.md — Orchestration/Constraints/RULES.md reference, Project/Product/PRDs/ reference"
    status: pending
  - id: fix-chatbot-paths
    content: "Fix Orchestration/Tasks/SWE/CHATBOT.md — Orchestration/Constraints/VCS_AND_FILE_GATE.md reference"
    status: pending
  - id: fix-user-vcs-scoring-paths
    content: "Fix Orchestration/Tasks/VCS/USER_VCS_SCORING.md — Orchestration/Constraints/VCS_AND_FILE_GATE.md reference"
    status: pending
  - id: fix-contributing-paths
    content: "Fix CONTRIBUTING.md — Orchestration/Constraints/ references and shahzebqazi/Codex GitHub link"
    status: pending
  - id: fix-tasks-readme-paths
    content: "Fix Orchestration/Tasks/README.md — Project/SYSTEM_PROMPT.md reference"
    status: pending
  - id: fix-adaptive-elicitation-link
    content: "Fix Orchestration/Tasks/SWE/ADAPTIVE_ELICITATION.md — broken link to project/PROTOCOL_REQUIREMENTS_ELICITATION.md (should be ../PM/PROTOCOL_REQUIREMENTS_ELICITATION.md)"
    status: pending
  - id: fix-swarm-paths
    content: "Fix Orchestration/Skills/Swarm/GENERATOR_PROTOCOL.md and Swarm/README.md — Documents/PRDs/, Documents/Plans/ references; START_HERE reference"
    status: pending
  - id: fix-defaults-paths
    content: "Fix Orchestration/Memories/DEFAULTS.md — config/local/RULES.md, config/local/MEMORIES.md, config/SETTINGS.json references"
    status: pending
  - id: fix-extensions-casing
    content: "Fix Extensions/README.md — HuggingFace→Huggingface link casing (breaks on Linux)"
    status: pending
  - id: fix-harness-scripts
    content: "Fix Orchestration/Harness scripts — install.py creates Project/ (gone), headless_run_10min.py references START_HERE.md (deleted), inconsistent naming (run_headless_* vs headless_run_*)"
    status: pending
  - id: fix-mvp-prd-paths
    content: "Fix MVP_PRD.md — .ai/ prefix references, START_HERE.md references, project/RULES.md path"
    status: pending
  - id: remove-duplicate-ux-wireframe
    content: "Remove duplicate: Product/UX_WIREFRAME_AGENT_PROMPT.md is identical to Product/Prompts/UX_WIREFRAME_AGENT_PROMPT.md — keep only the Prompts/ copy"
    status: pending
  - id: fix-product-internal-paths
    content: "Fix internal path references inside Product/ files — many still reference Project/Product/, Project/Orchestration/, Project/Extensions/"
    status: pending
  - id: delete-start-here-references
    content: "Remove or replace all remaining references to START_HERE.md (deleted file) across the repo"
    status: pending
  - id: verify-ci-paths
    content: "Verify .github/workflows/ paths still resolve after fixes (currently valid)"
    status: pending
isProject: true
---

# Repo Consistency Remediation — Critical Structural Fixes

## Context

Multiple AI agents working on this repository performed structural changes that were partially completed:

1. **`Project/` submodule removed** — `Project/` was a git submodule pointing to commit `879124d9`. It was removed, and product docs were moved to `Product/` at the repo root. But references throughout the repo still use `Project/Product/...`.

2. **`Orchestration/Constraints/` directory removed** — Rules and VCS gate files were moved into `Orchestration/Memories/` and `Orchestration/Memories/prompts/Constraints/`. But the SYSTEM_PROMPT, task files, and other docs still link to `Orchestration/Constraints/RULES.md` and `Orchestration/Constraints/VCS_AND_FILE_GATE.md`.

3. **`START_HERE.md` deleted** — The root entry-point file was deleted without updating the many files (docs, PRDs, harness scripts) that reference it.

4. **`Mockups/` deleted** — Clean removal; no remaining references.

5. **`Documents/` -> `Product/` migration** — Content-complete (all 19 files migrated), but the target prefix in both the migrated files and the rest of the repo is wrong (`Project/Product/` instead of `Product/`).

6. **Extension directory casing** — SYSTEM_PROMPT and README reference `Extensions/HuggingFace/` and `Extensions/OpenAI/`, but actual directories are `Extensions/Huggingface/` and `Extensions/Openai/`. Works on macOS, breaks on Linux.

**Result:** The repo has ~40 broken internal links, inconsistent path conventions, duplicate files, and harness scripts that reference deleted files.

---

## Audit Summary

### Broken path categories

| Category | Count | Severity |
|----------|-------|----------|
| `Project/Product/...` references (Project/ gone) | ~20+ | Critical |
| `Orchestration/Constraints/...` references (dir gone) | 7 | Critical |
| `START_HERE.md` references (file deleted) | 5+ | Critical |
| Broken relative links within Memories/ | 3 | Critical |
| Extension casing mismatches | 2 | Major |
| `config/` path references (dir never existed) | 3 | Moderate |
| Harness script stale paths | 3 | Moderate |
| Duplicate files | 1 | Minor |

### Files with broken references (by priority)

**P0 — Core system files (agent entry points)**

| File | Broken references |
|------|-------------------|
| `Orchestration/Harness/SYSTEM_PROMPT.md` | `../Constraints/RULES.md`, `../Constraints/VCS_AND_FILE_GATE.md`, `../../Project/Product/*` (throughout), `../../Extensions/HuggingFace/`, `../../Extensions/OpenAI/` |
| `README.md` | `Project/`, `Orchestration/Constraints/RULES.md`, `START_HERE.md`, directory layout diagram |
| `Orchestration/Memories/Constraints.md` | `(VCS_AND_FILE_GATE.md)` → wrong relative path, `(Orchestration/Tasks/VCS/JJ.md)` → wrong relative path |
| `Orchestration/Memories/prompts/Constraints/VCS_AND_FILE_GATE.md` | `(../Tasks/VCS/USER_VCS_SCORING.md)` → wrong relative path |

**P1 — Task and skill files**

| File | Broken references |
|------|-------------------|
| `Orchestration/Tasks/SWE/CHATBOT.md` | `../../Constraints/VCS_AND_FILE_GATE.md` |
| `Orchestration/Tasks/VCS/USER_VCS_SCORING.md` | `../../Constraints/VCS_AND_FILE_GATE.md` |
| `Orchestration/Tasks/README.md` | `Project/SYSTEM_PROMPT.md` |
| `Orchestration/Tasks/SWE/ADAPTIVE_ELICITATION.md` | `../../project/PROTOCOL_REQUIREMENTS_ELICITATION.md` |
| `Orchestration/Skills/Swarm/GENERATOR_PROTOCOL.md` | `Documents/PRDs/`, `Documents/Plans/` |
| `Orchestration/Skills/Swarm/README.md` | `Documents/PRDs/`, `START_HERE` |

**P2 — Supporting files**

| File | Broken references |
|------|-------------------|
| `Orchestration/Harness/README.md` | `Orchestration/Constraints/RULES.md`, `Project/Product/PRDs/` |
| `CONTRIBUTING.md` | `Orchestration/Constraints/` references, `shahzebqazi/Codex` GitHub URL |
| `Orchestration/Memories/DEFAULTS.md` | `config/local/RULES.md`, `config/local/MEMORIES.md`, `config/SETTINGS.json` |
| `Extensions/README.md` | `HuggingFace/HUGGINGFACE.md` (casing) |
| `Documents/README.md` | `Project/Product/` paths, `../SYSTEM_PROMPT.md` |
| `Orchestration/Harness/Documents/README.md` | `Project/Product/` paths, `../SYSTEM_PROMPT.md` |

**P3 — Harness scripts**

| File | Issue |
|------|-------|
| `Orchestration/Harness/install.py` | Creates `Project/`, expects `Project/SYSTEM_PROMPT.md` |
| `Orchestration/Harness/headless_run_10min.py` | Hardcodes `START_HERE.md` path |
| `Orchestration/Harness/run_headless_*.py` vs `headless_run_10min.py` | Inconsistent naming convention |

**P4 — Product files with stale internal paths**

| File | Issue |
|------|-------|
| `Product/AI/README.md` | References `Project/Product/` |
| `Product/Requirements/README.md` | References `Project/Product/UserStories/` |
| `Product/Requirements/TRACEABILITY.md` | References `Project/Product/Requirements/` |
| `Product/UX_WIREFRAME_AGENT_PROMPT.md` | References `Project/README.md`, `Project/SYSTEM_PROMPT.md`, `Project/Product/PRDs/` |
| `Product/Prompts/UX_WIREFRAME_AGENT_PROMPT.md` | Same as above (duplicate file) |
| `Product/Reports and Reviews/*.md` | Reference `Project/Product/`, `Project/Extensions/` |
| `Product/Requirements/Functional/FUNCTIONAL_REQUIREMENTS.md` | References `Project/Product/` |
| `Product/PRDs/CODE_REVIEW_HARNESS_AND_API_PRD.md` | References `Project/Orchestration/`, `Project/` |

### Duplicates

| File A | File B | Action |
|--------|--------|--------|
| `Product/UX_WIREFRAME_AGENT_PROMPT.md` | `Product/Prompts/UX_WIREFRAME_AGENT_PROMPT.md` | Keep `Prompts/` copy, delete root copy |

### Missing directories referenced in docs

| Referenced path | Actual location |
|----------------|----------------|
| `Orchestration/Constraints/` | `Orchestration/Memories/Constraints.md` + `Orchestration/Memories/prompts/Constraints/` |
| `Project/Product/` | `Product/` |
| `config/` | Does not exist; settings at `Orchestration/Memories/SETTINGS.json` |
| `START_HERE.md` | Deleted; entry point is now `Orchestration/Harness/SYSTEM_PROMPT.md` |

---

## Decisions Required

### Decision 1: Product docs location

**Option A — Keep `Product/` at repo root (minimal change)**
- Update all `Project/Product/` references to `Product/`
- Simpler structure, fewer nesting levels
- Requires updating ~20+ files

**Option B — Re-create `Project/Product/` (match existing docs)**
- Move `Product/` under a new `Project/` directory
- Existing references become correct without edits
- Adds a nesting level; `Project/` was previously a submodule (may cause confusion)

### Decision 2: Constraints location

**Option A — Keep in `Orchestration/Memories/` (current state)**
- Update all `Orchestration/Constraints/` references to `Orchestration/Memories/Constraints.md` and `Orchestration/Memories/prompts/Constraints/VCS_AND_FILE_GATE.md`
- Constraints are co-located with other memory/config files

**Option B — Restore `Orchestration/Constraints/` directory**
- Move `Orchestration/Memories/Constraints.md` to `Orchestration/Constraints/RULES.md`
- Move `Orchestration/Memories/prompts/Constraints/VCS_AND_FILE_GATE.md` to `Orchestration/Constraints/VCS_AND_FILE_GATE.md`
- Existing references become correct without edits
- Cleaner separation: constraints are not memories

### Decision 3: START_HERE.md

**Option A — Restore START_HERE.md as a thin redirect**
- Create a new `START_HERE.md` that points to `Orchestration/Harness/SYSTEM_PROMPT.md`
- Fixes all references with minimal disruption

**Option B — Remove all START_HERE.md references**
- Update every reference to point to `Orchestration/Harness/SYSTEM_PROMPT.md` directly
- Cleaner, but more edits

---

## Execution Plan

Once decisions are made, the fix order is:

1. **Structural moves** (if Option B chosen for any decision)
2. **P0 fixes** — SYSTEM_PROMPT.md, README.md, Memories/ links
3. **P1 fixes** — Task and skill files
4. **P2 fixes** — Supporting files, CONTRIBUTING, DEFAULTS
5. **P3 fixes** — Harness scripts (install.py, headless scripts)
6. **P4 fixes** — Product/ internal paths
7. **Duplicate removal** — Delete `Product/UX_WIREFRAME_AGENT_PROMPT.md`
8. **Extension casing** — Rename dirs or fix references
9. **Verification** — `grep -r` for any remaining broken paths
10. **CI check** — Confirm workflows still resolve

Estimated scope: ~30 files need edits; ~3 files need deletion or move.

---

## Success Criteria

1. Every internal markdown link resolves to an existing file
2. No references to `Project/` (unless `Project/` is restored)
3. No references to `Orchestration/Constraints/` (unless restored)
4. No references to `START_HERE.md` (unless restored)
5. Extension paths match actual directory casing
6. No duplicate content files
7. CI workflows pass
8. An agent reading SYSTEM_PROMPT.md can follow every link to an existing file
