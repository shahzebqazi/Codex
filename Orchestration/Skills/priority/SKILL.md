# Skill: priority

**Keyword:** `priority` (and phrases like "resolve by priority", "authority decides", "priority order", "arbiter")

**Effect (deterministic):**

1. Apply **priority or authority rules** to resolve conflicts: use predefined priorities, roles, or policies so that when agents disagree, a designated arbiter or priority order determines the winning proposal or action (e.g. safety overrides convenience, specific agent role overrides others).
2. **Detect conflicts** that are eligible for priority resolution (e.g. same resource, incompatible goals), then apply the rule and emit the **authoritative outcome**.
3. Emit the outcome in the conversation or to the harness; do not write files unless explicitly requested.

**When to apply:** User or orchestrator requests conflict resolution by authority or priority; need for predictable, low-latency decisions; negotiation or consensus is undesirable or has deadlocked; safety or policy requires a single designated decider.

**Context (distributed AI):** Suited to hierarchical systems and safety-critical overrides where a clear authority or ordering is required.
