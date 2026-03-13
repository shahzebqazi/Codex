---
name: "Code Review — Harness, SWE API, CI/CD"
overview: "Compiled findings and suggestions from senior staff engineer code review of Orchestration/Harness, Orchestration/Tasks/SWE/api, .github/workflows, and integration points. Use for prioritization and release gating."
todos:
  - id: redis-connect
    content: "Fix Redis client: ensure connect() is awaited before first use in Orchestration/Tasks/SWE/api/src/lib/redis.js"
    status: pending
  - id: remove-duplicate-index
    content: "Remove duplicate Orchestration/Tasks/SWE/api/src/index.js or unify with server.js as single entrypoint"
    status: pending
  - id: harness-health
    content: "Add GET /health to Python harness (Orchestration/Harness/python/harness/server.py)"
    status: pending
  - id: production-logs-doc
    content: "Document that production-logs /ingest is unauthenticated and must be network-restricted"
    status: pending
  - id: deploy-workflow-doc
    content: "Document deploy workflow as manual/placeholder or add minimal real deploy steps"
    status: pending
  - id: harness-ci
    content: "Add Harness to CI: install deps, lint (e.g. ruff), and run tests for Orchestration/Harness"
    status: pending
  - id: api-tests-status-metrics
    content: "Add API tests for /api/v1/status, /metrics, and error middleware"
    status: pending
  - id: harness-config-env
    content: "Harness config: allow blocklist path and START_HERE path override via env"
    status: pending
  - id: readme-canonical-paths
    content: "README states Orchestration/ (root) is canonical; CI/deploy use Orchestration/ paths."
    status: pending
  - id: arch-no-skill-dispatch
    content: "ARCHITECTURE: Harness has no skill dispatch — user input goes straight to Ollama with no keyword interception. Skills are convention-only (system prompt tells LLM about them), making skill behavior probabilistic. Add a dispatcher in server.py."
    status: pending
  - id: arch-global-conversation
    content: "ARCHITECTURE: server.py uses a single global Conversation() — multiple clients share state. Replace with session-keyed dict[str, Conversation]."
    status: pending
  - id: arch-no-mode-enforcement
    content: "ARCHITECTURE: No mode field in config or guard rails. Chat mode vs agent mode is not distinguished at runtime. generate skill says 'no file writes' but nothing enforces it. Add mode to SETTINGS.json and policy checks in guard_rails.py."
    status: pending
  - id: arch-guard-rails-length-only
    content: "ARCHITECTURE: guard_rails.py only checks input length and truncates responses. Does not enforce skill constraints, mode constraints, or budget tracking (max_actions_before_pivot exists in SETTINGS.json but is never read)."
    status: pending
  - id: arch-empty-skill-dirs
    content: "ARCHITECTURE: Skills/Chat/, Skills/Check/, Skills/Debug/, Skills/Plan/, Skills/Refactor/ are empty directories — a dispatcher scanning Skills/ would match these and find nothing inside."
    status: pending
  - id: production-branch-strategy
    content: "Adopt and document production-branch strategy: one production branch, multiple build targets (desktop, web, installers, scripts); do not serve or build from multiple feature branches. See §9 in this PRD."
    status: pending
isProject: true
---

# Code Review — Harness, SWE API, CI/CD

**Scope:** Orchestration/Harness, Orchestration/Tasks/SWE/api, .github/workflows, and files affecting runtime, safety, release-readiness, and developer workflow.

**Review date:** 2026-03-12.

---

## 1. Executive Summary

- **Harness (Python)** is a focused Flask app with clear guard rails (input/response caps, blocklist) but **no tests or CI**; path resolution is file-based and can break depending on run location.
- **SWE API (Node)** has solid fail-closed premium gating and health/metrics, but **no auth middleware** (tier is documented as “set by auth” but never set); Redis client is used without awaiting `connect()`; **index.js duplicates server.js**.
- **CI** only covers the Node API (lint + test) and docker-compose validation; the **Python harness is not in any workflow**.
- **Deploy workflow** is a placeholder (no real deploy steps); branch policy is clear and correctly restricts production PRs.
- **Orchestrator-compose** runs only llama-server; it does **not** wire the Python harness or the Node API—single-command run does not match a “full stack” if harness+API are expected.
- **Production-logs** `/ingest` has no authentication; safe only on a trusted network and should be documented.
- **Duplicate tree (resolved):** Single `Orchestration/` at repo root is canonical; CI and deploy use `Orchestration/` paths. `Project/Orchestration/` has been removed.

---

## 2. Scorecard

| Criterion | Score | Rationale |
|-----------|-------|-----------|
| Architecture clarity and separation of concerns | 7 | Harness (Python/Ollama) and API (Node/Express) are separate; roles clear. Orchestration (orchestrator-compose, INFRA docker-compose) does not integrate the harness; two directory trees blur the “single system” picture. |
| Reliability and error handling | 6 | Harness: Ollama errors mapped to 503/404/500 and stream truncation. API: error middleware and production-logs. Redis client returned before `connect()` completes; no retries/backoff for Ollama or Redis. |
| Security and safety controls | 6 | Blocklist and input caps in harness are good. API: premium gate fail-closed. No auth middleware; production-logs `/ingest` unauthenticated; blocklist is substring-only (no regex escaping or rate limiting). |
| Test quality and coverage | 5 | API: health, app export, premium gate covered; no tests for `/api/v1/status`, `/metrics`, feature-flag or error middleware. Harness: **no tests** and no pytest/CI. |
| CI/CD quality and branch/release alignment | 6 | Single CI (lint → test, validate-infra) for main/production; only runs for **Project/** API. Branch policy enforces production PR sources. Deploy workflow is placeholder; paths assume Project/. |
| Observability/debuggability | 6 | API: Prometheus metrics, request logging, production-logs for 5xx. Harness: no metrics, no structured logging, no request IDs; Flask debug flag is a production risk if enabled. |
| Maintainability/readability | 7 | Small modules, clear names. Duplicate server.js/index.js; README references Project/ while repo has both Orchestration/ and Orchestration/. |
| Documentation quality and accuracy | 6 | HARNESS_SPEC and README describe harness and API well. README quick start uses orchestrator-compose (llama-server only); Harness README and CI use root Orchestration/ paths; CONTRIBUTING branch flow is accurate. |
| Production readiness | 5 | API has healthchecks, metrics, Dockerfile, blue-green compose. Deploy workflow does not perform real deployment. Harness has no production config (e.g. WSGI), no CI, no health endpoint; debug=True is a production risk. |

---

## 3. Top Risks (High → Low)

| Risk | Location | Notes |
|------|----------|--------|
| Harness not in CI | `Orchestration/Harness/`, `.github/workflows/ci.yml` | Regressions in guard rails, blocklist, or Ollama client are never caught. |
| Redis client used before connect | `Orchestration/Tasks/SWE/api/src/lib/redis.js` | `getClient()` returns client and calls `client.connect().catch(...)` without awaiting; first use may see “client not ready” or timeouts. |
| Deploy is a no-op | `.github/workflows/deploy-blue-green.yml` | Deploy job only echoes instructions; push to production does not change running systems. |
| Production-logs /ingest unauthenticated | `Orchestration/Tasks/SWE/api/services/production-logs/server.js` | Any process that can reach the service can POST logs; acceptable only on a closed network—must be documented. |
| Duplicate entrypoints | `Orchestration/Tasks/SWE/api/src/index.js`, `server.js` | Identical content; package.json points to server.js; index.js is dead code and can diverge. |
| Harness path resolution | `Orchestration/Harness/python/harness/config.py` | `_harness_root()` and `start_here_path()` depend on `__file__`; works from expected dirs but breaks if package is installed or run from elsewhere. |
| No auth middleware | `Orchestration/Tasks/SWE/api/src/app.js` | `req.userTier`/`req.userId` are never set; premium routes always 403 until an auth layer is added (documented but easy to miss). |
| Flask debug in harness | `Orchestration/Harness/python/harness/__main__.py`, `server.py` | `--debug` enables Flask debugger; dangerous if ever used in production. |
| No skill dispatch in harness | `Orchestration/Harness/python/harness/server.py` | User input goes straight to Ollama — no keyword interception. Skills (generate, summarize, swarm) are system-prompt convention only; LLM may ignore or misapply them. |
| Global conversation singleton | `Orchestration/Harness/python/harness/server.py` line 18 | Single `Conversation()` shared by all clients. Two concurrent sessions corrupt each other's state. |
| No mode enforcement | `Orchestration/Harness/python/harness/config.py`, `guard_rails.py` | No `mode` field exists. Chat mode vs agent mode not distinguished. `generate` skill says "no file writes" but guard rails only check length. |
| Guard rails are length-only | `Orchestration/Harness/python/harness/guard_rails.py` | `check_input_length` and `truncate_response` are the only guards. Budget tracking (`max_actions_before_pivot` in SETTINGS.json) is never consumed by the harness. |
| Empty skill directories | `Orchestration/Skills/Chat/`, `Check/`, `Debug/`, `Plan/`, `Refactor/` | Empty dirs that any dispatcher would match on and find nothing. Silent failure or error depending on implementation. |

---

## 4. Top Improvements (High Impact → Lower Effort)

1. **Add Harness to CI** (high impact, medium effort)  
   In `.github/workflows/ci.yml`: add a job that installs deps from `Orchestration/Harness/python/requirements.txt` (or `Orchestration/Harness/python/` if that is canonical), runs a linter (e.g. ruff), and pytest for the harness package. Ensures guard rails and blocklist are tested on every PR.

2. **Await Redis connection before use** (high impact, low effort)  
   In `Orchestration/Tasks/SWE/api/src/lib/redis.js`: use an async `getClient()` or an explicit `ensureConnected()` that awaits `client.connect()` before returning (or on first request), and handle connection errors so the app does not start with a broken client.

3. **Remove or repurpose duplicate index.js** (low effort)  
   Delete `Orchestration/Tasks/SWE/api/src/index.js` if the only entrypoint is `server.js`, or make `index.js` the single entrypoint and have it delegate to the same logic as `server.js` to avoid duplication.

4. **Implement or stub deploy in deploy workflow** (high impact, medium effort)  
   In `.github/workflows/deploy-blue-green.yml`: add concrete steps (e.g. push image to registry and update a deployment target, or call a known “deploy” script/action). If deploy is intentionally manual, document that and add a small script or composite action that does the real steps.

5. **Add a health endpoint to the harness** (medium impact, low effort)  
   In `Orchestration/Harness/python/harness/server.py`: add e.g. `GET /health` that returns 200 (and optionally Ollama reachability) so orchestration and load balancers can probe the harness.

6. **Document production-logs security** (low effort)  
   In `Orchestration/Tasks/SWE/api/services/production-logs/server.js` or a nearby README: state that `/ingest` must only be reachable on a trusted network and that no auth is performed; add a one-line comment in code.

7. **Clarify directory layout** (medium impact, low effort)  
   Root README states `Orchestration/` (root) is the source of truth; CI/deploy use `Orchestration/` paths. CONTRIBUTING notes branch flow.

8. **Add API tests for status, metrics, and errors** (medium impact, low effort)  
   In `Orchestration/Tasks/SWE/api/test/`: add tests for `GET /api/v1/status` (and optionally feature flags), `GET /metrics` (content-type and basic presence), and error middleware (e.g. 500 and production-logs behavior with `LOG_SERVICE_URL` unset/mocked).

9. **Harness: config from env** (medium impact, low effort)  
   In `Orchestration/Harness/python/harness/config.py`: allow override of blocklist path, `START_HERE` path, and optionally Ollama URL via environment variables so the same code works in different layouts and environments.

10. **Harness: production server** (strategic)  
    Document or add a WSGI entrypoint (e.g. gunicorn) and discourage `app.run(debug=True)` in production; consider a small “production” profile in README or HARNESS_SPEC.

---

## 5. Quick Wins (Next 24h)

- [ ] **Remove `Orchestration/Tasks/SWE/api/src/index.js`** or make it the single entrypoint and delete the duplicate logic in `server.js`.
- [ ] **In `redis.js`:** ensure Redis `connect()` is awaited (or connection guaranteed) before first use; keep NODE_ENV=test returning null.
- [ ] **Add `GET /health`** to the Python harness in `server.py` (e.g. 200 + optional `{"ollama": "up"|"down"}`).
- [ ] **In deploy workflow:** add a one-line comment that deploy is manual/placeholder and link to runbook or script, or add a minimal “echo + upload artifact” so the job does something traceable.
- [ ] **In production-logs server:** add a short comment that `/ingest` is unauthenticated and must be network-restricted.
- [ ] **In CI:** add a job that at least installs the Harness Python deps and runs `python -m py_compile` (or ruff) on `Orchestration/Harness/python/harness/*.py` so syntax and import errors are caught.

---

## 6. Release Gate Decision

**GO WITH CONDITIONS**

**Conditions before production deploy:**

1. Fix Redis client usage so connection is established (or explicitly deferred) before handling requests.
2. Resolve duplicate `index.js`/`server.js` (remove or unify).
3. Document that production-logs `/ingest` is unauthenticated and must be on a trusted network.

**Conditions before treating “release” as fully automated:**

1. Replace or extend the deploy workflow with real deploy steps (or document that releases are manual and how).
2. Add Harness to CI (lint + tests or at least install + compile).

**Optional but recommended for “production” quality:**

- Add harness health endpoint.
- Add tests for API `/api/v1/status`, `/metrics`, and error middleware.
- README and CI use root `Orchestration/` as canonical; single tree, no duplicate.

**Rationale:** The API is small and fail-closed where it matters; the harness is coherent and well-specified. The main blockers are reliability (Redis), clarity (duplicate files, deploy placeholder), and safety/ops (production-logs, harness not in CI). Addressing the conditions above makes the repo safe and clear for production use and future releases.

---

## 7. Key File References

| Area | Paths |
|------|--------|
| Harness | `Orchestration/Harness/` — README.md, HARNESS_SPEC.md, python/harness/*.py, lua_gui/*.lua |
| SWE API | `Orchestration/Tasks/SWE/api/` — src/app.js, server.js, middleware/, routes/, lib/, test/ |
| CI/CD | `.github/workflows/ci.yml`, `deploy-blue-green.yml`, `branch-policy.yml` |
| Infra | `Orchestration/Tasks/INFRA/docker-compose.yml`, nginx.conf, prometheus, grafana |
| Production logs | `Orchestration/Tasks/SWE/api/services/production-logs/server.js` |
| Orchestrator | `Orchestration/orchestrator-compose.yml` (llama-server only) |

---

## 8. Architecture Audit — Runtime Enforcement Gaps (2026-03-13)

**Review date:** 2026-03-13 (addendum to original review).

Architecture audit identified structural gaps between the directory-level design (Skills/, Tasks/, modes) and what the harness runtime actually enforces. These are not bugs in existing code but missing runtime wiring that makes the documented architecture probabilistic rather than deterministic.

### New Risks

| Risk | Location | Impact |
|------|----------|--------|
| No skill dispatch | `server.py` — input goes straight to Ollama | Skills (generate, summarize, swarm) rely entirely on the LLM "knowing" about them from the system prompt. Behavior is probabilistic, not deterministic. A dispatcher matching first-token against Skills/{keyword}/ directories would make it enforced. |
| Global conversation singleton | `server.py` line 18: `conversation = Conversation()` | All clients share one conversation. Lua terminal + IUP GUI + any HTTP client corrupt each other's context. Session-keyed `dict[str, Conversation]` needed. |
| No mode field | `config.py`, `SETTINGS.json` | No runtime distinction between chat (text-only), agent (artifacts), and swarm (fan-out). `generate` skill says "no file writes" but nothing blocks it. Mode must exist in config and be checked by guard rails. |
| Guard rails are length-only | `guard_rails.py` | Only `check_input_length` and `truncate_response`. Does not enforce mode constraints, skill constraints, or budget tracking (`max_actions_before_pivot` in SETTINGS.json is never read). |
| Empty skill directories | `Skills/Chat/`, `Check/`, `Debug/`, `Plan/`, `Refactor/` | Placeholder dirs with no content. Any directory-scanning dispatcher would match on these and fail silently or error. Must be populated or deleted. |
| No skills-tasks manifest | `Skills/` and `Tasks/` trees | Independent directory trees with no formal binding. Renaming `Tasks/SWE/` would break every swarm plan silently. A manifest.json would catch this at startup. |

### New Improvements

| # | Improvement | Impact | Effort |
|---|-----------|--------|--------|
| 11 | **Skill dispatcher** — keyword lookup before LLM call | High | Medium |
| 12 | **Session-keyed conversations** — per-client state isolation | High | Low |
| 13 | **Mode field in config + guard rails** — enforce chat/agent/swarm constraints | High | Medium |
| 14 | **Skills-tasks manifest** — validated binding, startup check | Medium | Low |
| 15 | **Budget enforcement** — read max_actions_before_pivot, count actions, emit PIVOT signal | Medium | Low |
| 16 | **Task completion feedback** — .status.json written on task completion for skill reporting | Medium | Medium |
| 17 | **Migrate summarize + generate to Python callables** — prove .md-to-executable pattern | Medium | Low |
| 18 | **Clean up empty skill dirs** — populate or delete Chat/, Check/, Debug/, Plan/, Refactor/ | Low | Low |

### Updated Scorecard Note

Architecture clarity score should be adjusted from **7 → 6** given the gap between documented architecture and runtime enforcement. The directory structure is well-designed, but the harness code does not wire the conventions it describes.

---

## 9. Production Branch Strategy — Recommended Approach (2026-03-13)

**Recommendation:** Use **one** `production` branch as the single source of truth for release. Build **all** deliverables (desktop installer, web app, packages, scripts) from that branch via a **matrix of build targets**. Do **not** configure production to “serve” or build from multiple feature branches (e.g. desktop-app, web-app).

### Do not: production “serving” different branches

If production is wired to build artifacts from different branches (e.g. “build desktop from `desktop-app`, web from `web-app`”):

- There is no single source of truth for what is in production; each artifact comes from a different history.
- Releases are hard to reason about (production desktop = branch A, production web = branch B).
- Hotfixes and versioning become messy (fix and tag multiple branches).
- This conflicts with the existing flow: feature → main → production.

### Do: one production branch, multiple build targets

- Keep a single **production** branch, updated from **main** (e.g. via PR).
- On push to **production** (or on release tags), run CI that builds **all** deliverables from the **same** commit:
  - Desktop installer (Electron, native, etc.)
  - Web app (static or backend + frontend)
  - Packages / scripts (CLI, npm package, etc.)
- Use a **matrix strategy** in the workflow (e.g. `target: [desktop, web, cli]`) so one pipeline produces multiple artifacts from one source.
- Feature branches (e.g. `desktop-app`, `web-app`) remain **feature branches** that merge into **main**; only **main → production** defines what gets built and released.

### When to use separate release branches

Use separate release branches (or repos) only if you have **separate products** with different release cadences or version numbers (e.g. desktop 2.x vs web 1.x). Then each product has its own release branch and CI. For a single product with multiple deliverables (desktop + web + scripts), one production branch with a build matrix is simpler and recommended.

### Implementation notes

- Extend `.github/workflows/deploy-blue-green.yml` (or add a dedicated `release.yml`) to run a matrix: each job builds one target (desktop, web, cli) from the same checkout of `production`.
- Tag releases from `production` (e.g. `v1.2.0`); the same tag can trigger all build jobs.
- Document this policy in CONTRIBUTING.md or `docs/REBASE_STRATEGY.md` so branch and release behavior stay consistent.

---

*This PRD compiles the code review findings and suggestions for product and engineering prioritization. Update todos in the frontmatter as items are completed.*
