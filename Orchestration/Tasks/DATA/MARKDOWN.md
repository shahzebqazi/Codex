# Markdown Conventions

Standard Markdown conventions for the dotAi system.

## File Naming

- All AI docs: `UPPERCASE.md`
- Examples: `TODO.md`, `AGENTS.md`, `SKILL.md`, `PLAN.md`

## Structure

- **Headers** — Use for sections. Clear hierarchy (H1 → H2 → H3).
- **Bullet points** — For lists, options, steps.
- **Code blocks** — For technical content. Specify language when relevant.
- **Tables** — For structured data, comparisons, config.

## Linking

- Links between `.ai/` files: use relative paths.
- Example: `[See TODO](DATA/TODO.md)` or `[Plan](.ai/PLAN.md)`

## Consistency

- Prefer consistency over stylistic variation.
- AI agents parse these files; predictable structure improves reliability.
