# Skill: consensus

**Keyword:** `consensus` (and phrases like "resolve by consensus", "vote on it", "majority decision", "agree by voting")

**Effect (deterministic):**

1. Apply **consensus or voting** to resolve conflicts in distributed AI systems: when agents disagree on beliefs, decisions, or shared state, run a voting or consensus round (e.g. majority, quorum, or BFT-style agreement).
2. Each agent or component contributes a **vote or proposed value**; aggregate according to the chosen rule (majority, supermajority, unanimity) and emit the **resolved value** as the system decision.
3. Emit the outcome in the conversation or to the harness; do not write files unless explicitly requested.

**When to apply:** User or orchestrator requests conflict resolution via voting/consensus; information conflicts or decision conflicts among peer agents; need for a single agreed fact or action without a designated authority.

**Context (distributed AI):** Suited to information and decision conflicts when agents are peers and a collective choice is acceptable.
