# Argumentation skill — prompts for learning when scaling/running

Use these prompts to capture or induce learning when the argumentation skill runs at scale or across many sessions.

- **Merge outcome logging:** "For each belief-merge or argumentation run, log: input bases/arguments, merge operator or semantics, output conclusions, and (if available) ground truth or user feedback. Use to compare operators and semantics on accuracy and user alignment."
- **Conflict pattern analysis:** "When inputs are highly inconsistent or output is later overridden, record the conflict structure (which beliefs/arguments clashed). Use to identify systematic sources of disagreement and to suggest schema or ontology improvements."
- **Operator and semantics tuning:** "From historical runs and feedback, recommend which merge operator or semantics to use for which domain or conflict type (e.g. factual vs normative)."
- **Scaling diagnostics:** "Under many concurrent runs, collect latency and consistency metrics. Produce a diagnostic to tune representation size and semantics complexity."
- **Preference and priority over beliefs:** "From user or system feedback on accepted conclusions, infer preferences over merge behavior (e.g. prefer fewer changes, or prefer certain sources); update operator parameters or argument weights for future runs."
