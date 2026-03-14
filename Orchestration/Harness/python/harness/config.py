# Harness config: Ollama, guard rails, paths.
from pathlib import Path

# Ollama
OLLAMA_BASE_URL = "http://localhost:11434"
OLLAMA_V1 = f"{OLLAMA_BASE_URL}/v1"
OLLAMA_API = f"{OLLAMA_BASE_URL}/api"
DEFAULT_MODEL = "lfm2"

# Guard rails (match AGENT_PROMPT)
MAX_CONTEXT_MESSAGES = 20   # last N messages sent to Ollama
MAX_MEMORY_MESSAGES = 50    # in-memory conversation cap
MAX_RESPONSE_CHARS = 16_384
MAX_RESPONSE_TOKENS = 4096
INPUT_MAXLENGTH = 32_768
TRUNCATE_SUFFIX = "\n[Response truncated to avoid high memory use.]"

# Paths (relative to repo / harness root)
def _harness_root() -> Path:
    """Orchestration/Harness."""
    return Path(__file__).resolve().parent.parent.parent

def memories_dir() -> Path:
    """Orchestration/Memories."""
    return _harness_root().parent / "Memories"

def blocklist_path() -> Path:
    return memories_dir() / "blocklist.txt"

def system_prompt_path() -> Path:
    """Orchestration/Harness/SYSTEM_PROMPT.md (convention / system prompt)."""
    return _harness_root() / "SYSTEM_PROMPT.md"
