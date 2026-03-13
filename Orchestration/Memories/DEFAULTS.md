# DEFAULTS -- Default Reinforcement Learning Preferences

These are the system-wide defaults for agent behavior. Project-specific overrides go in `MENTAL_MAP.md`. User-specific private overrides go in `config/local/MEMORIES.md`. Runtime and model config live under `Memories/system/` (runtime.md, model_serving.md).

## Precedence

1. `config/local/RULES.md` (highest -- hidden, user-specific)
2. `project/RULES.md` (project-level)
3. `MENTAL_MAP.md` (project-learned)
4. This file (system defaults)
5. Runtime/model: `Memories/system/`

## Response Defaults

```json
{
  "response_style": "concise",
  "examples": "only_when_requested",
  "emoji": "never",
  "comment_style": "descriptive",
  "spacing": "minimal",
  "paradigm": "functional",
  "testing_required": true,
  "version_control": "active"
}
```

## Learning Defaults

```json
{
  "training_mode": "active",
  "learning_rate": 0.1,
  "memory_decay_check_interval": "session_start",
  "proficiency_tracking": true,
  "feedback_integration": "immediate"
}
```

## Budget Defaults

```json
{
  "max_actions_before_pivot": 5,
  "max_token_budget_per_task": null,
  "verification_after_every_edit": true,
  "dense_reward_tracking": true
}
```

## Memory Decay Rules

- Memories older than 30 days without access: flag as stale
- Memories contradicted by recent evidence: flag for review
- Memories with proficiency > 0.9: reduce update frequency
- Memories with proficiency < 0.3: increase update frequency
