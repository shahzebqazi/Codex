# XP_PLUS -- Extreme Programming for AI-Assisted Solo Development

## Overview

Modified Extreme Programming for one developer + AI agent. Use when planning, executing, or documenting solo projects with AI pair programming.

## Core Insight

> By having an infinitely powerful machine coding conscious, an unconscious programmer can fully intuit their problem solving. By having an ultimately helpful machine unconscious, a conscious programmer is a one man army.

Two modes:

1. **AI codes, human guides** — Human defines problem and acceptance; AI implements. Human observes, reviews, redirects.
2. **Human codes, AI assists** — Human writes; AI suggests, refactors, tests, debugs.

## Stakeholder and Team

- **Stakeholder** = developer (the human)
- **Team** = AI agent + human
- No separate product owner; developer decides scope and priority.

## Rudimentary Docs

Keep minimal. Create only what aids the workflow:

| Doc | Purpose | Location |
|-----|---------|----------|
| SDG | Software Development Guide; high-level vision, constraints | `.ai/project/SDG.md` |
| Stories | User stories (human-readable) and agent stories (task breakdowns) | `.ai/project/STORIES.md` |
| FR | Functional requirements | `.ai/project/FR.md` |
| NFR | Non-functional requirements (perf, security, etc.) | `.ai/project/NFR.md` |
| Architecture | System design, components, boundaries | `.ai/project/ARCHITECTURE.md` |
| Implementation | Technical decisions, patterns, conventions | `.ai/project/IMPLEMENTATION.md` |
| Deliverables | Artifacts, milestones | `.ai/project/DELIVERABLES.md` |
| Timeline | Phases, dates | `.ai/project/TIMELINE.md` |
| Risk Assessment | Risks, mitigations | `.ai/project/RISKS.md` |

Stories for humans: "As a user, I want X so that Y."  
Stories for agents: "Implement X; acceptance: A, B, C."

## Workflow

1. **Plan** — SDG, stories, FR/NFR; human writes or co-writes with AI.
2. **Design** — Architecture, implementation notes; human + AI.
3. **Implement** — AI codes (mode 1) or human codes with AI help (mode 2).
4. **Verify** — Tests, lint, review; AI runs; human signs off.
5. **Iterate** — Update stories, architecture, risks as needed.

## Integration with Other Skills

- Use MEMORY_MANAGEMENT for learning progress and workflow patterns.
- Use TECHNICAL_WRITER for worklog, buildlog, changelog.
- Use PERMISSIONS and BEHAVIOR_CONFIG for execution and style.
- Use jj (JJ.md) for version control and commit-as-communication.
