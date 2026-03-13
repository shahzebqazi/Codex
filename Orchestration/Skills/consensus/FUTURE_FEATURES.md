# Consensus skill — prompts for future features

Use these prompts when extending or designing additional features for the consensus skill.

- **Voting rules:** "Define and document supported voting rules: majority, supermajority, unanimity, Borda, approval. For each, specify input format, aggregation logic, and tie-breaking."
- **Byzantine resistance:** "Specify how to run a BFT-style consensus (e.g. PBFT, Raft) among AI agents when a bounded number may be faulty or adversarial; include role assignment and message patterns."
- **Weighted votes:** "Allow agents to carry weights or reputation; define how weighted votes are aggregated and how weights are updated over time."
- **Partial agreement:** "Support outcomes where the system agrees on a subset of issues and records dissent on others; define the output schema for agreed vs disputed items."
- **Integration with negotiation:** "Describe how consensus can be used to ratify or reject an outcome produced by the negotiation skill (e.g. post-negotiation vote)."
