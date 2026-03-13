# Reviewing work on the Experimental AI Coding Branch

After the headless runner has been executed for 10 minutes (or any duration), use this checklist to review the work.

## 1. Locate the session log

- Session logs are in `Orchestration/Harness/headless_sessions/`.
- Filenames: `headless_session_YYYYMMDD-HHMMSS.jsonl`.
- Open the most recent file (or the one from the run you want to review).

## 2. Inspect the log and summary

- **Summary file:** Prefer `headless_session_YYYYMMDD-HHMMSS_summary.md` for a quick read; it contains the agent’s end-of-session summary of work done and remaining.
- **Session log:** Each line is a JSON object. Fields: `turn`, `role` (optional), `content` (optional), `summary` (optional, true on final turn), `error` (optional), `ts`.
- The model’s responses are in objects with `"role": "assistant"` and `"content": "..."`.
- Look for: which PRD tasks were mentioned, what concrete steps or code were suggested, and any errors (`"error"` field).

## 3. Compare to the PRD

- Open the PRD used for the run (default: `Project/Product Documents/PRDs/CODE_REVIEW_HARNESS_AND_API_PRD.md`).
- Check the `todos` in the frontmatter. See which tasks the model attempted and whether the suggestions align with the task descriptions.

## 4. Decide next steps

- **If the model only produced suggestions:** Treat them as proposals. A human or an AI with write access can apply changes in the repo (still on this branch).
- **If you want to re-run:** Adjust `--minutes`, `--prd`, or the model/endpoint and run `headless_run_10min.py` again. Logs are cumulative (new file per run).
- **If promoting work:** Review all changes on `experimental-ai-coding-branch`, then merge or cherry-pick into another branch only after human approval. See `Project/EXPERIMENTAL_BRANCH_AI_INSTRUCTIONS.md` for isolation rules.

## 5. Branch isolation

- All work from the headless run and any follow-up edits should remain on `experimental-ai-coding-branch` until explicitly merged. Do not push this branch to `main`, `desktop-app`, or `Production` without review.
