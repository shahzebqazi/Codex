# API Audit — MVP & P0 (Showstopper) Checklist

## Current Endpoints (Implemented)

| Method | Path | Auth | Tier | Purpose |
|--------|------|------|------|---------|
| GET | `/health` | No | — | LB and orchestrator health; latency/error tracking |
| GET | `/metrics` | No | — | Prometheus scrape (latency, request count, errors) |
| GET | `/api/v1/status` | No | — | App status + feature flags |
| GET | `/api/v1/premium/feature` | No | Pro/Enterprise | Example tier-gated route (Stripe middleware) |

## P0 — Required for MVP (Showstopper if Missing)

| Item | Status | Notes |
|------|--------|-------|
| Health endpoint | Done | `/health` returns 200/503, Redis check |
| Error handling | Done | Centralized middleware → production-logs |
| Feature flags | Done | Redis/env; toggles without redeploy |
| Tier-gated routes | Done | `tierGate(['pro','enterprise'])` middleware |
| Encryption at rest | Schema + doc | `compliance/init-db-encryption.sql` + `encrypt-at-rest.md` |
| Auth for protected routes | Pending | Add auth middleware; set `req.userId`, `req.userTier` from JWT/Stripe |
| User CRUD (if MVP needs it) | Pending | Add POST/GET /api/v1/users with encrypted PII |
| Stripe webhook | Pending | Add POST /api/v1/webhooks/stripe to sync tier from subscription |

## Recommended Next Steps

1. Implement auth middleware (JWT or session); set `req.userTier` from DB or Stripe Customer.
2. Add Stripe webhook handler to update user tier on subscription change.
3. Add user-facing CRUD only if MVP scope requires it; use encrypt helper for PII.
