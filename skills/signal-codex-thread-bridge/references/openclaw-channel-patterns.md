# OpenClaw Channel Patterns

Use these patterns as inspiration for the Signal bridge design and for future multi-channel expansion.

## Gateway + Channels Architecture

- Use a single local gateway/control plane that manages sessions, channels, tools, and event routing.
- Treat each messaging app as a channel adapter that connects to the gateway.
- Keep channel adapters isolated so new channels can be added without changing core routing.

## Signal Channel Notes

- Signal integration can be handled by a dedicated Signal adapter backed by `signal-cli`.
- Run the Signal adapter in a daemon-style mode and stream message events into the gateway.
- Use allowlists for group access and treat DMs separately from group chat routing.
- Use deterministic routing for group chats by tagging the target agent in the message.

## CLI-Style Operational Hooks

- Provide a thin CLI or command surface to:
  - Check gateway status.
  - Login/pair the Signal device.
  - Send a message to a target group.

## Practical Takeaways For This Skill

- Keep the router generic: channel adapters emit the canonical envelope.
- Keep the gateway state minimal and immutable; store durable mappings separately.
- Favor explicit routing controls (agent tags, group allowlists) to reduce ambiguity.
