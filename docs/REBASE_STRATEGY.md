# Rebase Strategy

This document describes how to rebase feature branches and keep history aligned with `main` and `production`. Use it for one-time cleanup or when bringing long-lived branches up to date.

## Branch model

- **main** — Integration branch; all feature work lands here via PR.
- **production** — Release branch; promoted from `main` via PR. Deployments and blue-green releases run from `production`.
- **Feature branches** — `feature/*`, `docs/*`, `chore/*` (and agents: `agent-name/task-id`). Always branch from `main`.

## Recommended approach: Rebase feature branches onto main

Use this when you want to keep `main` as the source of truth and tidy existing feature branches before merging.

### One-time steps (per feature branch)

1. **Decide the target main.** Usually `origin/main`. Ensure local main is up to date:
   ```bash
   git fetch origin
   git checkout main
   git pull origin main
   ```

2. **Rebase the feature branch onto main.** From the branch you want to update (e.g. `desktop-app`, `docs/documentation-reorg`):
   ```bash
   git checkout <branch-name>
   git fetch origin main
   git rebase origin/main
   ```
   Resolve any conflicts, then continue with `git rebase --continue` until done.

3. **Force-push if the branch was already pushed** (coordinate with anyone else using the branch):
   ```bash
   git push --force-with-lease origin <branch-name>
   ```

4. **Open a PR** from the rebased branch into `main` (or merge after review).

5. **After all feature branches are merged**, update `production` from `main` per the normal release flow (PR from `main` to `production`).

## Alternative: Squash on main (optional)

If you want a single “fresh start” commit on `main` (e.g. after a large reorganization):

1. Create a backup branch: `git branch main-backup main`
2. Find the root commit or the commit you want to squash back to.
3. Soft reset: `git reset --soft <root-or-commit>`
4. Create one new commit with the desired message (e.g. `chore: align repo with branch and CI/CD strategy`).
5. Force-push to `main` only if the repo is single-user or all collaborators agree. All feature branches would then need to be rebased onto the new `main`.

Use this sparingly; prefer “rebase feature branches onto main” for ongoing work.

## Summary

| Goal                         | Action                                                |
|-----------------------------|--------------------------------------------------------|
| Update one feature branch   | `git checkout <branch> && git fetch origin main && git rebase origin/main` |
| Clean history over time     | Use squash-merge for PRs into `main` when merging      |
| One-time squash of main     | Backup main, soft reset, single commit, then rebase all feature branches |

Future rebases should follow the same approach: branch from `main`, rebase onto latest `main` before opening or updating a PR.
