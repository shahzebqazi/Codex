# Priority skill — prompts for learning when scaling/running

Use these prompts to capture or induce learning when the priority skill runs at scale or across many sessions.

- **Decision logging:** "For each priority-based decision, log: conflict type, proposals, applied rule or arbiter, outcome, and (if available) ex-post feedback or override. Use to audit and to detect misconfigurations."
- **Override analysis:** "When a human or downstream system overrides the priority outcome, record the override and context. Use to identify rules that are frequently wrong or misaligned and suggest priority or policy updates."
- **Usage patterns:** "Aggregate which priority rules are invoked most often and for which conflict types. Use to simplify or consolidate rules and to surface missing policies."
- **Scaling diagnostics:** "Under high load, collect latency and correctness (if feedback exists) for priority resolution. Produce a short report to tune arbiter placement and rule evaluation order."
- **Policy refinement:** "From override and feedback data, generate candidate changes to priority tables or arbiter behavior (e.g. new rules, reordering) for human review."
