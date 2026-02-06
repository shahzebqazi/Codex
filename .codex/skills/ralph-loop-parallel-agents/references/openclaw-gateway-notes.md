# OpenClaw Gateway Notes

This file summarizes patterns from the OpenClaw repo that are useful for coordinating parallel agents.

## Gateway-Control Plane Pattern

- Use a local-first gateway to manage sessions, channels, and tool execution.
- Keep channel adapters isolated so routing logic stays centralized and testable.
- Provide a multi-channel inbox that can fan out tasks to multiple agents.

## Operational Practices

- Run the gateway as a daemon or background service for always-on coordination.
- Expose a small CLI to start/stop the gateway and check status.
- Keep routing rules explicit to avoid ambiguous agent assignment.

## Applicability To This Skill

- Treat the gateway as the coordination layer for agent workstreams.
- Use the gateway to dispatch tasks and collect status updates.
- Keep agent workspaces and branches isolated while sharing the same artifact set.
