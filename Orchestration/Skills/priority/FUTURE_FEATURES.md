# Priority skill — prompts for future features

Use these prompts when extending or designing additional features for the priority skill.

- **Priority schema:** "Define a machine-readable schema for priority rules: role hierarchy, policy IDs, override conditions (e.g. 'safety > performance'), and how to resolve ties when two rules have equal rank."
- **Arbiter interface:** "Specify the interface for an arbiter agent or service: input (conflicting proposals, context), output (selected proposal, reason code), and hooks for logging and audit."
- **Dynamic priorities:** "Support context-dependent priorities (e.g. in emergency mode, different order). Define how mode or context is passed in and how it selects which priority table to use."
- **Escalation path:** "When the priority rule selects an outcome that is later rejected or overridden by a human, define an escalation path and how to feed that back into priority or policy updates."
- **Integration with other skills:** "Describe when to fall back from priority to negotiation or consensus (e.g. when no clear priority applies or when stakeholders require participation)."
