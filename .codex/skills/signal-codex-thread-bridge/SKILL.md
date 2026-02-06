---
name: signal-codex-thread-bridge
description: Build a full-duplex bridge between a Codex thread and a Signal chat, including message normalization, routing, and group-chat delegation among perpetual PRD-driven agents. Use when implementing Signal integration, agent dispatch from chat, or designing a connector layer for future messaging apps in Python (functional style).
---

# Signal Codex Thread Bridge

## Overview

Create a full-duplex Signal connector that binds a Signal group to a Codex thread and routes messages between human participants and PRD-driven agents in a group-chat style.

## Workflow

1. Collect inputs
- Signal account details (bot number or device), target group id, and initial allowlist.
- Codex thread id for the feature or PRD.
- Agent roster (names/ids) and default agent.
- Persistence target for mappings and dedupe state.

2. Define the canonical message envelope
- Include: `source`, `group_id`, `thread_id`, `sender`, `agent`, `text`, `timestamp`, `message_id`.
- Normalize inbound Signal messages into this envelope before any routing.
- Normalize outbound Codex events into the same envelope.

3. Design the router as pure functions
- Implement `route_inbound(envelope, state) -> (state, actions)`.
- Implement `route_outbound(envelope, state) -> (state, actions)`.
- Treat side effects as returned `actions` (send to Signal, send to Codex, log).
- Keep `state` immutable (return new values, do not mutate in place).

4. Implement full-duplex I/O loops
- Run separate async tasks for inbound Signal events and outbound Codex events.
- Use a bounded queue for each direction with backpressure.
- Dedupe by `message_id` + `source` (persist a small LRU window).
- Ensure idempotency: the router should be safe to re-run on retries.

5. Implement group-chat delegation
- Parse `@agent` mentions; route to that agent when present.
- When no agent is mentioned, route to the default agent or a dispatcher.
- Prefix outbound replies with `[agent]` so the group can follow who responded.
- Keep a light-weight mapping of `agent -> last thread` to support follow-ups.

6. Bind the thread and group
- Use a strict 1:1 mapping for now: one Signal group == one Codex thread.
- Store mapping in a small JSON file or sqlite table (group_id -> thread_id).
- Reject or prompt when a group is unmapped.

7. Keep the agents PRD-driven
- Load the current PRD and acceptance criteria before work begins.
- Update progress artifacts after each agent response.
- Follow the Ralph loop patterns in `references/ralph-loop-patterns.md`.

## Functional Python Style Guidelines

- Model data with `@dataclass(frozen=True)` or `NamedTuple`.
- Use pure functions for transformations; isolate I/O in thin adapters.
- Pass dependencies explicitly (no hidden globals).
- Prefer `map`, `filter`, comprehensions, and small composable functions.
- Keep side effects in one place: adapters and persistence only.

## Signal-Specific Notes (Current Scope)

- Treat Signal as the only active channel for now; design adapters so adding more channels later is straightforward.
- Use the Signal adapter to translate between Signal events and the canonical envelope.
- See `references/openclaw-channel-patterns.md` for patterns drawn from OpenClaw.

## Resources

- `scripts/router_sim.py` - Pure-function router example with a simulated group chat flow.
- `references/ralph-loop-patterns.md` - PRD-driven perpetual agent loop patterns.
- `references/openclaw-channel-patterns.md` - Gateway + channel patterns to reuse.
