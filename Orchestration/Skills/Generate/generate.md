# Skill: generate

**Keyword:** `generate` (and phrases like "generate ...", "write me ..." when the user intends in-chat text only)

**Effect (deterministic):**

1. Produce the **requested text** (code, prose, list, etc.) **on screen** — i.e. in the conversation response.
2. Do **not** create, edit, or overwrite any file unless the user **explicitly** asks for a persisted artifact (e.g. "save to X", "write to file Y", "create a file").
3. If the user only asked for "generate X" or "write me X" without naming a file or path, output is in-chat only.

**When to apply:** User asks for generated content without specifying a file or save location. If they later say "save that" or "put that in file Z", then file writes are allowed for that follow-up.
