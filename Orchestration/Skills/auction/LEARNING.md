# Auction skill — prompts for learning when scaling/running

Use these prompts to capture or induce learning when the auction skill runs at scale or across many sessions.

- **Clearing logs:** "For each auction run, log: resource IDs, bids per agent, clearing allocation, clearing prices, and (if available) ex-post utilization or satisfaction. Use to evaluate efficiency and fairness."
- **Bid strategy analysis:** "From historical bids and outcomes, summarize which bid levels and strategies (e.g. shading) lead to wins and at what cost; produce a short guide for agent bid strategies."
- **Price and demand trends:** "Over time, aggregate clearing prices and demand by resource type. Use to detect scarcity spikes and to tune reserve prices or capacity."
- **Scaling diagnostics:** "Under many concurrent auctions, collect latency, participation rate, and clearing success. Produce a diagnostic to tune auction frequency and batch size."
- **Mechanism tuning:** "From efficiency and fairness metrics, suggest when to switch auction formats or parameters (e.g. second-price vs first-price, reserve levels) for different resource types or load."
