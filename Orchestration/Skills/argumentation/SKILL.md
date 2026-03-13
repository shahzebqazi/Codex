# Skill: argumentation

**Keyword:** `argumentation` (and phrases like "resolve by argumentation", "belief merging", "argument framework", "merge beliefs")

**Effect (deterministic):**

1. Apply **belief merging or argumentation frameworks** to resolve **information conflicts** in distributed AI systems: when agents hold contradictory data or beliefs, use defined merging rules or an argumentation semantics (arguments, attacks, acceptable conclusions) to produce a consistent or accepted set of conclusions.
2. Run the chosen method (e.g. belief merge operator, Dung-style semantics) and emit the **resolved beliefs or accepted arguments** (and optionally the reasoning trace).
3. Emit the outcome in the conversation or to the harness; do not write files unless explicitly requested.

**When to apply:** User or orchestrator requests conflict resolution via argumentation or belief merging; conflicts are about contradictory information or recommendations; a single voting step is insufficient and structured reasoning over arguments is desired.

**Context (distributed AI):** Suited to knowledge and recommendation conflicts where logical consistency and acceptability matter.
