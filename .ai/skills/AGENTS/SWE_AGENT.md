# SWE-Agent Patterns

Config-driven coding agent. Radical simplicity.

Source: https://github.com/swe-agent/swe-agent

## YAML-as-Agent-Spec

- Agent behavior defined in YAML.
- Tools, prompts, constraints in config.
- No hardcoded logic. Swap config to change behavior.

## Agent-Computer Interface (ACI)

- Abstraction over tools. Agent sees high-level operations.
- Implementation details hidden. Portable across environments.

## Trajectory Logging

- Log full agent trajectory: actions, observations, decisions.
- Enables replay, debugging, analysis.
- Store in `.ai/trajectories/` or similar.

## Config-Driven Tool Injection

- Tools listed in config. Loaded at runtime.
- Add/remove tools without code changes.

## Radical Simplicity

- Mini-swe-agent: ~100 lines core.
- Minimal dependencies. Easy to audit, extend.
