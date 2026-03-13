# Consensus skill — prompts for learning when scaling/running

Use these prompts to capture or induce learning when the consensus skill runs at scale or across many sessions.

- **Vote distribution logging:** "For each consensus run, log: question ID, votes per agent, chosen rule, outcome, and (if available) ground truth or ex-post feedback. Use to evaluate which rules and group compositions yield the best accuracy or satisfaction."
- **Disagreement analysis:** "When vote distribution is highly split or when outcome is later overridden, record the split and context. Use to identify systematic biases or missing information that could be addressed in future runs."
- **Rule tuning:** "From historical runs and feedback, recommend when to switch voting rules (e.g. majority vs supermajority) based on group size, conflict type, or consequence severity."
- **Scaling diagnostics:** "Under many concurrent consensus rounds, collect: latency, participation rate, and agreement rate. Produce a diagnostic summary to tune timeouts and quorum requirements."
- **Trust and weight updates:** "If using weighted votes, define how to update agent weights from correctness or alignment with final outcomes and user feedback."
