# Ollama API client: list models, chat (streaming).
import json
from typing import Iterator, List, Optional, Union

import requests

from .config import OLLAMA_API, OLLAMA_V1, DEFAULT_MODEL, MAX_RESPONSE_TOKENS


class OllamaError(Exception):
    pass


class OllamaConnectionError(OllamaError):
    pass


class OllamaModelNotFoundError(OllamaError):
    pass


def list_models() -> List[dict]:
    """Return list of installed models from GET /api/tags."""
    try:
        r = requests.get(f"{OLLAMA_API}/tags", timeout=10)
        r.raise_for_status()
    except requests.exceptions.ConnectionError as e:
        raise OllamaConnectionError("Ollama not running. Start with: ollama serve") from e
    data = r.json()
    return data.get("models", [])


def chat(
    messages: list,
    model: Optional[str] = None,
    stream: bool = True,
    max_tokens: int = MAX_RESPONSE_TOKENS,
) -> Union[Iterator[str], str]:
    """
    Send chat to Ollama. messages: [{"role":"user"|"assistant"|"system","content":"..."}].
    If stream=True, yields content chunks; else returns full response content.
    """
    model = model or DEFAULT_MODEL
    url = f"{OLLAMA_V1}/chat/completions"
    payload = {
        "model": model,
        "messages": messages,
        "stream": stream,
        "max_tokens": max_tokens,
    }
    try:
        r = requests.post(url, json=payload, stream=stream, timeout=60)
        if r.status_code == 404:
            raise OllamaModelNotFoundError(
                f"Model not found. Run: ollama pull {model}"
            )
        r.raise_for_status()
    except requests.exceptions.ConnectionError as e:
        raise OllamaConnectionError("Ollama not running. Start with: ollama serve") from e

    if not stream:
        data = r.json()
        choice = data.get("choices", [{}])[0]
        return choice.get("message", {}).get("content", "") or ""

    def gen() -> Iterator[str]:
        buf = ""
        for line in r.iter_lines(decode_unicode=True):
            if not line or not line.strip():
                continue
            if line.startswith("data: "):
                part = line[6:].strip()
                if part == "[DONE]":
                    break
                try:
                    chunk = json.loads(part)
                    delta = chunk.get("choices", [{}])[0].get("delta", {})
                    content = delta.get("content") or ""
                    if content:
                        buf += content
                        yield content
                except json.JSONDecodeError:
                    continue
        return None

    return gen()
