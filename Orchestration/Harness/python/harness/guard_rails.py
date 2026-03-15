# Guard rails: context cap, message cap, truncation, swarm gate.
import re
from typing import Any, Optional

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


def swarm_gate(settings: dict[str, Any], workflow_def: dict[str, Any] | None = None) -> tuple[bool, str]:
    """Check if swarm dispatch is allowed. Returns (allowed, reason)."""
    swarm_cfg = settings.get("swarm", {})
    if not swarm_cfg.get("enabled", False):
        return False, "Swarm mode is not enabled. Set swarm.enabled=true in SETTINGS.json."
    if workflow_def:
        required = set(workflow_def.get("required_capabilities", []))
        available = set(swarm_cfg.get("capabilities", []))
        missing = required - available
        if missing:
            return False, f"Workflow requires capabilities not available: {missing}"
    return True, "ok"


SWARM_HALLUCINATION_PATTERNS = [
    r"Agent \d+ will",
    r"dispatching to (sub)?agent",
    r"delegating to .+ agent",
    r"swarm (plan|config|dispatch)",
    r"parallel agents? (will|should|can)",
]


def check_swarm_hallucination(text: str, swarm_enabled: bool) -> Optional[str]:
    """If swarm is disabled and response contains swarm-like patterns,
    return a warning suffix. Else return None."""
    if swarm_enabled:
        return None
    for pattern in SWARM_HALLUCINATION_PATTERNS:
        if re.search(pattern, text, re.IGNORECASE):
            return (
                "[Note: Swarm mode is not active. Multi-agent delegation language was detected "
                "but no agents were dispatched. Enable swarm mode in SETTINGS.json to use agent swarms.]"
            )
    return None
