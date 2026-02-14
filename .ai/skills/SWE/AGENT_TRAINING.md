# Agent Training

Skill for active agent learning in the dotAi system.

## Training Modes

- **active** — Learn from every interaction. Update memories/skills automatically on each turn.
- **passive** — Learn only when explicitly told (e.g., "remember this", "add to skills").
- **disabled** — No learning. Read-only mode for memory and skills.

## Skill Proficiency

- Track proficiency per domain (e.g., python, typescript, go, security).
- Represent as levels or scores in `.ai/memories/PROFICIENCY.json`.
- Adjust behavior: higher proficiency → less hand-holding, fewer confirmations.

## Learning Rate

- Configure in `SETTINGS.json`: `learning_rate` (0.0–1.0).
- Higher = more aggressive updates; lower = more conservative.
- Apply to both memory and skill updates.

## Feedback Integration

1. **Explicit feedback** — User says "good/bad", "remember this", "forget that".
2. **Implicit signals** — Corrections, reverted edits, repeated questions.
3. **Confidence** — Low-confidence outputs are candidates for learning.

## Memories vs Skills

| Update memories when | Update skills when |
|----------------------|--------------------|
| User preferences, project context | Reusable patterns, procedures |
| Facts about this session/project | Cross-project capabilities |
| One-off corrections | Generalizable techniques |
| Names, paths, choices | How-to workflows, conventions |

**Rule**: Memories are ephemeral/project-scoped. Skills are durable and reusable across projects.
