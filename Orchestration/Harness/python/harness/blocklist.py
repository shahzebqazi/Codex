# Blocklist: load from Orchestration/Memories, check user/assistant content.
from pathlib import Path
from typing import List, Optional

from .config import blocklist_path


def load_blocklist(path: Optional[Path] = None) -> List[str]:
    """Load blocklist patterns (one per line, strip, skip empty and #)."""
    p = path or blocklist_path()
    if not p.exists():
        return []
    lines = []
    for line in p.read_text(encoding="utf-8").splitlines():
        line = line.strip()
        if line and not line.startswith("#"):
            lines.append(line)
    return lines


def matches_blocklist(text: str, patterns: List[str]) -> List[str]:
    """Return list of patterns that appear in text (case-sensitive substrings)."""
    return [p for p in patterns if p in text]
