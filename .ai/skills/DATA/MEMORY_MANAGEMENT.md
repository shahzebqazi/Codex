# MEMORY_MANAGEMENT -- Tiered Memory System for .ai/memories/

## Overview

Agents manage `.ai/memories/` as a tiered, persistent store. Memories inform context across sessions. Apply this skill when creating, updating, or querying memories.

## Memory Tiers

| Tier | Purpose | Path/Pattern | Update cadence |
|------|---------|--------------|----------------|
| User preferences | Editor, language, style choices | `preferences/` or `user/` | On user request |
| Project context | Domain, stack, conventions | `MENTAL_MAP.md`, `CONTEXT/` | As project evolves |
| Learning progress | Concepts mastered, gaps | `learning/` | After skill application |
| Workflow patterns | Repeated sequences, shortcuts | `workflows/` | When pattern stabilizes |
| Custom knowledge | Arbitrary domain facts | `knowledge/` | Ad hoc |

## Proficiency Scores

Store proficiency per concept/domain as `0.0`–`1.0`:

- `0.0` – No experience
- `0.3` – Beginner (used once, needs help)
- `0.6` – Intermediate (can apply with occasional hints)
- `1.0` – Mastery (can apply and teach)

Example in JSON:

```json
{ "react-hooks": 0.8, "graphql": 0.4 }
```

## Decay Indicators

Tag memories with freshness and relevance:

| Flag | Meaning | Action |
|------|---------|--------|
| `freshness` | Last verified date (ISO 8601) | Re-verify if older than threshold |
| `relevance` | How often used recently | Boost retrieval weight |
| `staleness` | Contradicts newer info | Mark for update or delete |

When loading memories, prefer: high relevance, recent freshness, not stale.

## Context Recovery on Session Resume

On session start:

1. Read `memories/MENTAL_MAP.md` for project overview.
2. Load user preferences from `memories/preferences/` or `user/`.
3. Scan `memories/` for entries with recent `freshness` or high `relevance`.
4. Check for `staleness` flags and avoid using unless explicitly corrected.
5. Reconcile with current project state (e.g. changed dependencies).

## When to Create / Update / Delete

- **Create**: New preference, domain fact, or workflow pattern the agent or user will reuse.
- **Update**: User changes preference; project context changes; proficiency increases; staleness detected.
- **Delete**: Factually wrong, obsolete, or explicitly revoked by user.

## Structured Format

Use a hybrid format for machine readability + AI prompts:

- **JSON** for data: proficiency scores, timestamps, flags, structured facts.
- **Markdown** for prose: rationale, examples, notes for AI prompts.

Example memory file:

```markdown
---
type: workflow
domain: testing
proficiency: 0.7
freshness: 2025-02-14
relevance: high
---

# Testing workflow for API routes

We run integration tests via `pytest` with `httpx`.
Always mock external services; use `responses` library.
```

JSON-only for simple data:

```json
{
  "type": "preference",
  "key": "comment_style",
  "value": "docblocks",
  "updated": "2025-02-14"
}
```

## File Layout

```
.ai/memories/
├── MENTAL_MAP.md          # Project overview, always read first
├── preferences/           # User preferences
├── context/               # Project-specific context
├── learning/              # Learning progress
├── workflows/             # Workflow patterns
├── knowledge/             # Custom domain knowledge
└── *.json                 # Structured data files
```
