# Skill: summarize

**Keyword:** `summarize` (and common variants, e.g. "summarize the chat", "summarize context")

**Effect (deterministic):**

1. Summarize the **current context window** of the chat (conversation so far, key decisions, open questions, and any relevant state).
2. Emit the summary **in the conversation** so the user can:
   - Copy-paste it elsewhere, or
   - Use it to start a new agent or chat with full context.
3. Do **not** write a file unless the user explicitly asks to save the summary to a path.

**When to apply:** User message clearly requests a summary of the chat/context/session (e.g. "summarize", "summarize this chat", "give me a context summary for a new agent").
