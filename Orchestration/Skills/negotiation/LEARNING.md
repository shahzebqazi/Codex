# Negotiation skill — prompts for learning when scaling/running

Use these prompts to capture or induce learning when the negotiation skill runs at scale or across many sessions.

- **Outcome logging:** "When a negotiation completes (agreement or timeout), log: participants, conflict type, number of rounds, final outcome, and whether the outcome was accepted by all. Use this log to analyze which protocols and concession strategies perform best."
- **Failure patterns:** "After each failed or timed-out negotiation, record the last N proposals per agent and the reason for failure. Use this to suggest protocol or parameter changes (e.g. more rounds, different concession rates)."
- **Preference learning:** "From historical negotiated outcomes and user or system feedback (accept/reject, edits), infer preferred trade-offs or fairness norms; update agent strategies or facilitator policies for future runs."
- **Scaling diagnostics:** "When running many concurrent negotiations, collect metrics: latency per round, drop-out rate, agreement rate by conflict type and group size. Produce a short diagnostic summary to tune parallelism and timeouts."
- **Transfer to new domains:** "Given a corpus of past negotiations (goals, resources, outcomes), generate a short summary of recurring patterns and recommended default parameters for new domains or agent types."
