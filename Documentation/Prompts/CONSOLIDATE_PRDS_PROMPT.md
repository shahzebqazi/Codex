# Prompt: Consolidate All PRDs into One Structured Document

> **Model:** Claude Opus 4.6 (Max Mode)
> **Branch:** `development`
> **Workspace:** this repo (Codex / dotAi)

---

## Your task

You are a senior technical program manager. Read all 10 PRDs in `Documentation/PRDs/`, then produce **one structured, consolidated PRD** that a cross-functional team of **extreme programming (XP) engineers**, **graphics/brand artists**, and **infrastructure personnel** can execute from.

### What to read first

1. `START_HERE.md` — system overview, repo layout, conventions
2. `CONTRIBUTING.md` — branch flow, team roles
3. `Orchestration/Harness/Documents/BRANCHES.md` — branch structure
4. Every file in `Documentation/PRDs/` (listed below)

### Source PRDs (10 files, 113 todos total, 7 completed)

| PRD | Todos | Focus |
|-----|-------|-------|
| `MVP_PRD.md` | 24 (6 done) | Core system: scaffold, harness, jj, Docker, chatbot test, CI |
| `FEATURES_PRD.md` | 25 | Desktop app settings 1–56, personas, guard rails, agent governance |
| `REPO_CONSISTENCY_PRD.md` | 22 | Broken paths, stale refs, structural fixes across the repo |
| `CODE_REVIEW_HARNESS_AND_API_PRD.md` | 15 | Harness Python, SWE Node API, CI/CD, architecture gaps |
| `AI_SYSTEM_COMPATIBILITY_PRD.md` | 10 | Human verification of Ollama, llama-server, Anthropic, OpenAI, CLI, Desktop, API |
| `LAYERED_MEMORY_AND_KNOWLEDGE_BASE_PRD.md` | 7 | Memory layers, file/dir facts, anti-hallucination, KB design |
| `HOTKEYS_PRD.md` | 5 | Keyboard shortcuts and default mappings |
| `README_REWRITE_PRD.md` | 5 | Public-release README rewrite for "Aura" branding |
| `MYSTIC_PIXEL_ART_SWARM_PRD.md` | 0 (agent assignments) | Pixel art moon/stars, Cursor palette, brand guide HTML, asset gen |
| `SESSION_PRD_PROMPT.md` | 0 (template) | Meta-prompt for creating session PRDs |

### Target audience — three teams

**Team 1: XP Engineers** (software)
- Harness runtime (Python Flask + Ollama), SWE API (Node/Express), skill dispatcher, session management, mode enforcement, guard rails, agent hierarchies, sandboxing, memory system, jj integration, chatbot flow, settings persistence, CI/CD
- They pair-program, do TDD, work in short iterations, and need atomic user stories with clear acceptance criteria

**Team 2: Graphics & Brand Artists**
- Pixel art asset generator (Kotlin), brand guide HTML preview, color palette (Cursor brand research), twinkling star animation, typography specimens, moon renderer, icon/logo/banner/hero generation, README visual polish
- They need visual specs, asset dimensions, color hex values, animation parameters, and clear "done looks like" descriptions

**Team 3: Infrastructure**
- Docker compose, llama-server GGUF serving, CI workflows (GitHub Actions), deploy pipeline (Pages + blue-green), production branch strategy, Redis, Prometheus/Grafana, health endpoints, production-logs security, environment config
- They need runbooks, config references, port numbers, health check contracts, and deployment sequence diagrams

### Output format

Write the consolidated PRD as a single markdown file at:
`Documentation/PRDs/CONSOLIDATED_TEAM_PRD.md`

Use this structure:

```
---
name: "Consolidated Team PRD — dotAi / Mystic / Aura"
overview: "<one paragraph>"
todos:
  <all actionable items as YAML todos with id, content, status, plus a new field: team (xp|graphics|infra)>
isProject: true
---

# Consolidated Team PRD

## 1. Executive Summary
   - Vision, current state (7/113 done), what ships and when
   - Priority tiers: P0 (blocks everything), P1 (MVP gate), P2 (polish), P3 (strategic)

## 2. Architecture Overview
   - System diagram (Mermaid) showing all components and their owners
   - Component → team mapping table

## 3. XP Engineering Track
   ### 3.1 P0 — Critical Fixes
   ### 3.2 P1 — MVP Runtime
   ### 3.3 P2 — Features & Settings
   ### 3.4 P3 — Agent Governance & Memory
   (each item: id, description, acceptance criteria, source PRD, depends-on)

## 4. Graphics & Brand Track
   ### 4.1 Pixel Art Asset Generator
   ### 4.2 Brand Guide & HTML Preview
   ### 4.3 Color Palette & Typography
   ### 4.4 README Visual Polish
   (each item: visual spec, dimensions, hex values, animation params, done-looks-like)

## 5. Infrastructure Track
   ### 5.1 Docker & Local Model Serving
   ### 5.2 CI/CD Pipelines
   ### 5.3 Deploy & Production Strategy
   ### 5.4 Observability & Security
   (each item: config, ports, health contracts, runbook steps)

## 6. Cross-Team Dependencies
   - Dependency graph (Mermaid) showing blocking relationships
   - Shared contracts (API shapes, file formats, config keys)

## 7. Iteration Plan
   - Suggested 1-week iterations (XP style) with scope per team
   - Milestones: MVP gate, brand guide launch, production readiness

## 8. Deferred / Out of Scope
   - Items explicitly deferred with rationale

## 9. Appendix
   - Source PRD cross-reference table (consolidated id → original PRD + original id)
   - Glossary of project-specific terms
```

### Rules

1. **Read every PRD in full** before writing anything. Do not summarize from titles alone.
2. **Preserve every actionable item** — nothing gets lost. If a source todo is vague, sharpen it; if it's a duplicate across PRDs, merge and note the sources.
3. **De-duplicate ruthlessly** — many items appear in multiple PRDs (e.g. health endpoint, skill dispatcher, session management). Merge them into one item with all source PRD references.
4. **Add acceptance criteria** to every item that lacks them. XP engineers need testable criteria.
5. **Assign every item to exactly one team** (xp, graphics, or infra). Some items span teams — assign to the primary owner and note the dependency.
6. **Priority is king** — P0 items block everything. Be opinionated about priority based on the dependency graph and the MVP gate (Ollama works locally as backend).
7. **Do not invent new features.** Only consolidate and structure what already exists in the 10 PRDs.
8. **YAML frontmatter todos** must include every item. The `team` field is new — add it to every todo.
9. **SESSION_PRD_PROMPT is a template** — do not include its content as work items. Reference it as a process tool in the appendix.
10. **Mark completed items as completed** — carry forward the 7 completed todos from MVP_PRD with `status: completed`.
11. **Use the repo's conventions** — UPPERCASE.md filenames, Documentation/ paths, jj for VCS, Cursor plan.md YAML format.

### When you're done

1. Verify the consolidated PRD has the same or greater number of actionable items as the sum of all source PRDs (minus true duplicates). State the final count.
2. State how many items are assigned to each team.
3. State how many items are at each priority level.
4. Do NOT commit. Leave the file unstaged so the human can review before committing.
