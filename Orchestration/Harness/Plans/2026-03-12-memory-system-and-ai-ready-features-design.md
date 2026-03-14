# Design: Memory System, AI-Ready Features, and Harness Learning

**Date:** 2026-03-12  
**Status:** Draft for approval  
**Related:** FEATURES_PRD.md, MEMORY_MANAGEMENT.md, HARNESS_SPEC.md, continual-learning (Cursor)

---

## 1. Goals

- **Every feature has the potential to be AI:** Each harness feature (chat, model choice, system prompt, persona, etc.) can consume learned prefs and, later, local inference for suggestions and adaptation.
- **Developer experience learned and logged locally:** Interaction patterns and outcomes are recorded under Memories; learning is compressive (compact, delete, merge), not only additive.
- **Memories become a structured system:** Manifests (imported/referenced data), indexes + Redis (operations and cache), DB (long-term storage), Prolog KB (optimizations).
- **Clear scope separation:** Project vs. User vs. Agent vs. Repo/System memories.
- **MVP integration with other AIs:** Cursor Memory support (read/write AGENTS.md-style preferences and workspace facts; optional sync with Cursor’s transcript/index).

---

## 2. Memory Architecture (Target State)

### 2.1 Four Pillars

| Pillar | Purpose | Implementation notes |
|--------|---------|----------------------|
| **Manifests** | Imported/referenced data: external docs, URLs, other AI exports | Files under `Memories/manifests/` (or per-scope); manifest index lists source, checksum, last sync. |
| **Indexes / Redis** | Operations and cache: fast lookup, session state, hot data | Redis (or SQLite + in-memory) for: session cache, event tail, derived prefs cache; file-based indexes (e.g. JSON) where Redis is optional. |
| **DB** | Long-term storage: event log, derived prefs history, learning state | SQLite (or single DB file) under Memories; append/compact event log; tables for prefs, compaction runs. |
| **Prolog KB** | Optimizations: rules, routing, “which model for this task” | Prolog facts/rules derived from DB + MENTAL_MAP; harness or a small service queries KB for suggestions. |

All persistence lives under **Orchestration/Memories** (or per-scope subdirs). No separate “harness state” root; Redis/DB paths are configurable and default under Memories.

### 2.2 Scope Separation

| Scope | Purpose | Location (under Memories) | Examples |
|-------|---------|---------------------------|----------|
| **Project** | Domain, stack, conventions, project-specific learning | `Memories/` (existing MENTAL_MAP, context, workflows) | MENTAL_MAP.md, project preferences, task-model success table |
| **User** | User-specific preferences and patterns (portable) | `Memories/user/` or `config/local/` (gitignored) | Preferred model, streaming on/off, UI density, Cursor-learned prefs |
| **Agent** | Per-agent state and learning (e.g. per persona/skillset) | `Memories/agents/<agent_id>/` or `Orchestration/Agents/` | Agent-specific prefs, last model used, session stats |
| **Repo/System** | Repo-wide or system defaults, not user-specific | `Memories/system/` or repo-level `Memories/` | DEFAULTS.md, blocklist, system prompts, Prolog KB |

Precedence (who overrides whom) aligns with existing DEFAULTS.md: user > project > system. Agent scope is orthogonal (per-agent overrides for that agent only).

---

## 3. Learning: Compressive and Deletive

Learning is **not only additive**. The system will:

- **Compact:** Merge similar events (e.g. “model X chosen” N times → single prefs entry with count); summarize long event tails into aggregates.
- **Compress:** Replace raw event sequences with derived summaries (e.g. “preferred model”, “typical session length”); keep a bounded event window or sampled archive.
- **Delete:** Drop low-value or stale data per policy: age, low relevance, superseded by newer learning, or explicit user revocation.

Policies (configurable, stored in Memories/system/ or DEFAULTS):

- Event retention: e.g. last N days of raw events, or size cap; rest compacted into DB summaries.
- Staleness: reuse MEMORY_MANAGEMENT freshness/relevance/staleness; mark and prune stale entries.
- User/agent requests: “forget this” or “reset learning” clears or truncates the corresponding scope.

Compaction can run on a schedule (e.g. idle), on session end, or on-demand via API/settings.

---

## 4. Harness Integration (MVP)

- **Event log (DB):** Harness writes interaction and outcome events to the DB (or append-only file that the DB ingests). Events are scoped (user, project, agent, system).
- **Derived prefs:** A process (harness thread, cron, or on-demand) reads the event log and updates derived prefs (e.g. USER_PREFS.json or LEARNING.md) in the appropriate scope; Redis/cache holds hot prefs for the API.
- **Features:** Each feature that has a “default” or “suggestion” (model, streaming, persona, system prompt) reads from prefs (and later Prolog KB). No behavior change until we add explicit “suggest” endpoints or UI; the data path is in place so every feature can become AI-driven.

---

## 5. Cursor Memory Support (MVP)

- **Cursor “memory”** = AGENTS.md (Learned User Preferences + Learned Workspace Facts) and optionally the continual-learning index (`.cursor/hooks/state/continual-learning-index.json`) and transcript root (`~/.cursor/projects/<workspace>/agent-transcripts/`).
- **MVP behavior:**
  - **Read:** Harness (or a small adapter) can read Cursor’s AGENTS.md when present (workspace or project root). Map “Learned User Preferences” and “Learned Workspace Facts” into our user/project prefs so harness defaults and future Prolog KB can use them.
  - **Write:** Optionally write back to AGENTS.md (or a Cursor-compatible file) from our derived user/project prefs so Cursor’s agents see harness-learned preferences and facts. Format: same sections and plain bullets as continual-learning skill.
- **Sync direction:** Prefer one source of truth. MVP: either (a) Cursor is source → we import into Memories/user and Memories/project, or (b) Harness is source → we export to AGENTS.md. Document choice (e.g. “Cursor as source for MVP”) and add reverse sync later if needed.
- **No duplication of transcript processing:** We do not re-implement Cursor’s continual-learning pipeline; we consume its output (AGENTS.md) and optionally write back. If we later add our own transcript ingestion, it can feed the same DB/prefs and then export to AGENTS.md.

---

## 6. File and Data Layout (MVP-Oriented)

```
Orchestration/Memories/
├── MENTAL_MAP.md          # existing (project)
├── DEFAULTS.md            # existing (system)
├── system/                # runtime.md, model_serving.md (was SYSTEM.md)
├── prompts/               # CONTEXT_REFRESH.md (agent instructions)
├── blocklist.txt          # existing (system)
├── user/                  # user scope (gitignored where appropriate)
│   ├── prefs.json         # derived user prefs
│   └── events.db          # or shared events DB with scope column
├── agents/                # agent scope
│   └── <id>/
│       └── prefs.json
├── system/
│   ├── event_schema.json  # event types and fields
│   └── compaction_policy.json
├── manifests/             # manifest index + imported/referenced data
│   └── index.json
├── db/                    # long-term store (MVP: SQLite)
│   └── memories.db        # events, prefs_history, compaction_runs
└── kb/                    # Prolog KB (future)
    └── optimizations.pl
```

Redis (optional for MVP): if used, connection/config points to a local instance; keys namespaced by scope (e.g. `memories:user:prefs`, `memories:project:cache`).

---

## 7. Event Schema (Minimal for MVP)

Events are scoped (project | user | agent | repo/system) and have at least:

- `ts` (ISO 8601), `scope`, `type`, `payload` (JSON).
- Types: e.g. `model_selected`, `new_chat`, `message_sent`, `system_prompt_set`, `chat_exported`, `session_ended`, `cursor_prefs_imported`.

Compaction consumes these to produce/update prefs (e.g. “preferred_model”, “streaming_default”, “typical_session_turns”).

---

## 8. Out of Scope for This Design

- Prolog KB implementation details and exact rule set.
- Redis vs. SQLite-only for MVP (decision left to implementation plan).
- Full Cursor transcript parsing; only AGENTS.md (and optional index) in MVP.
- Other AI integrations (e.g. OpenClaw, Ralph) beyond Cursor Memory.

---

## 9. Success Criteria (MVP)

- All harness user persistence lives under Memories (project/user/agent/system).
- Event log exists and is written by harness for key actions; at least one derived prefs file is updated from it (compaction).
- At least one harness feature (e.g. default model or streaming) reads from derived prefs.
- Cursor Memory: read AGENTS.md into prefs when present; optional write-back of learned prefs to AGENTS.md.
- Compaction policy exists and can delete/compact old events; no requirement to run Prolog or Redis in MVP.

---

## 10. Next Step

On approval: invoke **writing-plans** skill to produce a phased implementation plan (events + DB, scopes, compaction, Cursor read/write, Prolog/Redis later).
