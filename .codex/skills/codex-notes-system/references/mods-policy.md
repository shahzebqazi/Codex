# Mods Policy

Use this policy to resolve conflicts between different directives.

## Precedence Order

1. Hard rules
2. Project rules
3. User rules
4. Defaults

## Rules

- A value defined in a higher layer overrides the same key in lower layers.
- Do not keep conflicting statements within the same layer.
- Store the merged output in `mods.json` under `effective`.
- Changes to precedence require an update to `changelog.md`.

## Example

If defaults say `examples: always` but user rules say `examples: only_when_requested`,
then the effective value is `only_when_requested`.
