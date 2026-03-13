# USER_VCS_SCORING -- Scoring the User on Git and VCS Usage

## Purpose

Once the user has a repo or is using VCS, agents **start scoring** the user's git/VCS habits. Use scores to give brief, constructive feedback and encourage good practices. Do not nag or overwhelm.

## When to Score

- **Only** when a git or jj repo exists in the workspace (or the user is clearly using VCS).
- **Only** when the user or the agent performs VCS-related actions (commit, branch, push, merge, etc.).
- Scoring is **informational** — it guides feedback, not permissions.

## What to Score

| Criterion | Good (positive signal) | Poor (feedback opportunity) |
|-----------|------------------------|-----------------------------|
| **Commit frequency** | Regular, logical commits | Long gaps; huge single commits |
| **Commit message quality** | Clear, conventional (feat:, fix:, etc.) | Vague or empty messages |
| **Branch usage** | Feature branches; clean main | Work directly on main; orphan work |
| **Merge hygiene** | Intentional merges; no force to main | Force push to main; unreviewed merges |
| **Conflict handling** | Resolves conflicts; documents decisions | Leaves conflicts; silent overwrites |

Agents may extend this list (e.g. tag usage, PR descriptions) in project memory.

## Where to Store Scores

- **Preferred:** `Orchestration/Memories/user_vcs_score.json` (create if missing; gitignored optional so user can track progress).
- **Alternative:** A **User VCS** subsection in `Orchestration/Memories/MENTAL_MAP.md` under "User Preferences".

**Suggested JSON shape:**

```json
{
  "last_updated": "ISO8601",
  "summary_score": 0,
  "criteria": {
    "commit_frequency": { "score": 0, "note": "" },
    "commit_messages": { "score": 0, "note": "" },
    "branch_usage": { "score": 0, "note": "" },
    "merge_hygiene": { "score": 0, "note": "" },
    "conflict_handling": { "score": 0, "note": "" }
  },
  "observations": []
}
```

Use a simple 0–5 or 0–10 scale per criterion; `summary_score` can be an average or weighted sum.

## How to Give Feedback

- **Brief:** One or two sentences when relevant (e.g. after a commit or merge).
- **Constructive:** Suggest what to do better, not only what was wrong.
- **Occasional:** Do not comment on every single action; aggregate when summarizing.
- **Optional:** If the user asks "how am I doing with git?", use the stored score and observations to give a short summary.

## Relation to Other Docs

- **VCS gate:** See [Orchestration/Constraints/VCS_AND_FILE_GATE.md](../../Constraints/VCS_AND_FILE_GATE.md) — no substantive work or file creation until repo exists or user has asked three times; chat mode exception.
- **Git conventions:** [GIT.md](GIT.md) — human commit conventions and branch strategy.
- **jj for agents:** [JJ.md](JJ.md) — how agents use jj; scoring here applies to **user** git/jj usage.
