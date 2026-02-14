# Daily Logging Skill

Structured journal format for project context and long-term memory.

## Title Format

```
[DD-MM-YYYY]
```

Example: `[14-02-2025]`

## Sections

### Body
- Free-form narrative. What happened. Key events and decisions.

### Log
- **Notes** — Ad-hoc observations.
- **Highlights** — Wins, milestones.
- **Challenges** — Blockers, difficulties.
- **Learnings** — Insights to retain.

## Example

```markdown
# [14-02-2025]

## Body
Shipped auth refactor. Migrated 3 services to new JWT flow.

## Log
- **Highlights**: Zero downtime deploy.
- **Challenges**: Redis key migration took longer than expected.
- **Learnings**: Use batch scan for large keyspace migrations.
```

## Storage

- Path: `.ai/memories/journal/` or project `.ai/memories/`.
- One file per day or per project-session.
- Agents read journals for long-term context across sessions.
