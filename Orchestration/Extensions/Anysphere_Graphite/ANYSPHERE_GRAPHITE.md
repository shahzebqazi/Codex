# Anysphere & Graphite Compatibility

Compatibility stub for Anysphere (Cursor) and Graphite in dotAi.

## Purpose

- **Anysphere**: Cursor IDE and ecosystem; ensure dotAi rules, plans, and MCP usage align with Cursor workflows.
- **Graphite**: Stacked PRs, branch management; integrate with jj/git workflows and agent branches.

## Cursor (Anysphere)

- Primary doc in-tree: [Orchestration/Tasks/TOOLS/CURSOR.md](../../Orchestration/Tasks/TOOLS/CURSOR.md) (rules, plans, modes, MCP).
- Extensions role: document any Cursor-specific APIs, agent modes, or plugins that affect dotAi behavior.
- Compatibility: dotAi runs inside Cursor; rules in `.cursor/rules/` and plan.md format are already aligned.

## Graphite

- Stacked pull requests, `gt` CLI, merge queue behavior.
- Integration: when agents create branches (per JJ.md), document whether and how to use Graphite stacks (e.g. `gt stack submit`, PR grouping).
- Conventions: branch naming, stacking order, and how jj feature branches map to Graphite stacks.

## To Populate

- Graphite CLI commands and workflow steps.
- Any Anysphere/Cursor API or plugin integration points.
- Decision: Graphite as default for multi-PR work or optional.
