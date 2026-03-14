# Auction skill — prompts for future features

Use these prompts when extending or designing additional features for the auction skill.

- **Auction formats:** "Define supported formats: first-price, second-price (Vickrey), ascending, descending, combinatorial. For each, specify bid format, clearing rule, and output (allocation + prices)."
- **Resource schema:** "Specify a schema for resources being auctioned: discrete units, divisible capacity, slots, or tasks; include constraints (e.g. one agent per task) and how they affect clearing."
- **Budget and fairness:** "Add budget constraints per agent and optional fairness rules (e.g. max share per agent, reserve prices). Define how these are enforced in the clearing step."
- **Repeated auctions:** "Design a repeated-auction protocol for ongoing allocation (e.g. each round): state carried over, bid updates, and convergence or stability criteria."
- **Integration with other skills:** "Describe when to combine auction with priority (e.g. reserved allocation for high-priority agents) or with negotiation (e.g. post-auction side agreements)."
