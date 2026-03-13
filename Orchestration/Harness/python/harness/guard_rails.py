# Guard rails: context cap, message cap, truncation.
from typing import Optional

from .config import (
    MAX_CONTEXT_MESSAGES,
    MAX_MEMORY_MESSAGES,
    MAX_RESPONSE_CHARS,
    INPUT_MAXLENGTH,
    TRUNCATE_SUFFIX,
)


def trim_messages_for_context(messages: list[dict]) -> list[dict]:
    """Return last MAX_CONTEXT_MESSAGES for API."""
    return messages[-MAX_CONTEXT_MESSAGES:] if len(messages) > MAX_CONTEXT_MESSAGES else messages


def trim_conversation(messages: list[dict]) -> list[dict]:
    """Return last MAX_MEMORY_MESSAGES for in-memory history."""
    return messages[-MAX_MEMORY_MESSAGES:] if len(messages) > MAX_MEMORY_MESSAGES else messages


def truncate_response(text: str) -> str:
    """Truncate assistant response and append suffix if over cap."""
    if len(text) <= MAX_RESPONSE_CHARS:
        return text
    return text[:MAX_RESPONSE_CHARS] + TRUNCATE_SUFFIX


def check_input_length(text: str) -> Optional[str]:
    """Return error message if input over cap, else None."""
    if len(text) > INPUT_MAXLENGTH:
        return f"Input too long (max {INPUT_MAXLENGTH} characters)."
    return None
