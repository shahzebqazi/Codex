# Experimental AI Coding Branch — Instructions for AIs

<!-- AI: You are on the Experimental AI Coding Branch. Read this file first and follow it strictly. -->

This branch is the **Experimental AI Coding Branch** (`experimental-ai-coding-branch`). These instructions apply only on this branch and are intended to keep AI work isolated.

## Branch isolation rules

1. **Work only on this branch**
   - Do not push, merge, or open pull requests that target `main`, `desktop-app`, `Production`, or any other branch.
   - All commits, file edits, and experiments must stay on `experimental-ai-coding-branch` unless a human explicitly asks otherwise.

2. **Do not affect other branches**
   - Do not change shared configuration, CI workflows, or tooling in ways that would break or alter behavior on other branches (e.g. changing `.github/workflows` in a way that assumes this branch’s layout only).
   - Prefer adding new files or branch-specific config (e.g. under `Project/Experimental/` or a path used only on this branch) rather than modifying core entrypoints (e.g. `START_HERE.md`, `Project/README.md`) in ways that would conflict when merged.

3. **Scope of work**
   - Use `START_HERE.md` and the PRDs in `Project/Product Documents/PRDs/` (or `Documents/PRDs/`) as the source of tasks.
   - Headless runs and experiments (e.g. the 10-minute harness runner) are intended for this branch; review their output and any generated artifacts here before considering promotion.

4. **Review before merge**
   - Any human or process that merges this branch elsewhere must review changes first. AIs must not merge this branch into another branch.

5. **Identification**
   - When committing (e.g. via jj or git), include a short note that work is on the Experimental AI Coding Branch when it’s not obvious from context.

## Entry point

AIs on this branch should still read [START_HERE.md](../START_HERE.md) for system context, then apply these branch rules on top. Treat this file as an overlay: when in doubt, isolation and branch-only work take precedence.
