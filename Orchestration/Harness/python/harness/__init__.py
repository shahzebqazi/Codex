# Harness: Python core for Ollama + Lua GUI.
from .config import (
    DEFAULT_MODEL,
    INPUT_MAXLENGTH,
    MAX_CONTEXT_MESSAGES,
    MAX_MEMORY_MESSAGES,
    OLLAMA_BASE_URL,
)
from .ollama_client import (
    OllamaConnectionError,
    OllamaError,
    OllamaModelNotFoundError,
    chat,
    list_models,
)
from .conversation import Conversation, load_system_prompt
from .guard_rails import check_input_length, truncate_response
from .blocklist import load_blocklist, matches_blocklist

__all__ = [
    "DEFAULT_MODEL",
    "INPUT_MAXLENGTH",
    "MAX_CONTEXT_MESSAGES",
    "MAX_MEMORY_MESSAGES",
    "OLLAMA_BASE_URL",
    "OllamaConnectionError",
    "OllamaError",
    "OllamaModelNotFoundError",
    "chat",
    "list_models",
    "Conversation",
    "load_system_prompt",
    "check_input_length",
    "truncate_response",
    "load_blocklist",
    "matches_blocklist",
]
