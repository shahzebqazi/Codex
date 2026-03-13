# Skill: auction

**Keyword:** `auction` (and phrases like "resolve by auction", "bid for resources", "market allocation", "allocate by bid")

**Effect (deterministic):**

1. Apply **auction or market-based mechanisms** to resolve **resource contention** in distributed AI systems: agents submit bids or prices for scarce resources (e.g. compute, bandwidth, tasks); the mechanism clears the market and assigns resources to winning bids.
2. Run the chosen auction format (e.g. first-price, second-price, combinatorial) and emit the **allocation** (who gets what) and optionally clearing prices.
3. Emit the outcome in the conversation or to the harness; do not write files unless explicitly requested.

**When to apply:** User or orchestrator requests conflict resolution via auction or market; conflicts are primarily about who gets which resources; efficient or fair allocation at scale is desired.

**Context (distributed AI):** Suited to resource contention when agents can express willingness-to-pay or preferences via bids.
