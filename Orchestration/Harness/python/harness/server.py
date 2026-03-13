# HTTP API for Lua GUI: models, chat (streaming), new_chat, system prompt.
import json
from flask import Flask, request, Response, jsonify

from .ollama_client import (
    list_models,
    chat as ollama_chat,
    OllamaError,
    OllamaConnectionError,
    OllamaModelNotFoundError,
)
from .conversation import Conversation
from .guard_rails import check_input_length, truncate_response
from .blocklist import load_blocklist, matches_blocklist
from .config import DEFAULT_MODEL

app = Flask(__name__)
conversation = Conversation()


@app.route("/api/models", methods=["GET"])
def api_models():
    """List installed Ollama models."""
    try:
        models = list_models()
        names = [m.get("name", "").split(":")[0] for m in models if m.get("name")]
        return jsonify({"models": names, "default": DEFAULT_MODEL})
    except OllamaError as e:
        return jsonify({"error": str(e)}), 503


@app.route("/api/system_prompt", methods=["GET"])
def api_get_system_prompt():
    return jsonify({"system_prompt": conversation.system_prompt})


@app.route("/api/system_prompt", methods=["POST"])
def api_set_system_prompt():
    data = request.get_json() or {}
    conversation.set_system_prompt(data.get("system_prompt", ""))
    return jsonify({"ok": True})


@app.route("/api/new_chat", methods=["POST"])
def api_new_chat():
    conversation.clear()
    return jsonify({"ok": True})


@app.route("/api/chat", methods=["POST"])
def api_chat():
    """Send user message; stream assistant reply or return full text."""
    data = request.get_json() or {}
    user_content = (data.get("content") or "").strip()
    model = data.get("model") or None
    if not model:
        model = DEFAULT_MODEL
    stream = data.get("stream", True)

    err = check_input_length(user_content)
    if err:
        return jsonify({"error": err}), 400

    blocklist = load_blocklist()
    if blocklist and matches_blocklist(user_content, blocklist):
        return jsonify({
            "error": "Message matched blocklist; not sent.",
            "blocked": True,
        }), 400

    conversation.add_user(user_content)
    messages = conversation.messages_for_api()

    try:
        if stream:
            def generate():
                full = []
                for chunk in ollama_chat(messages, model=model, stream=True):
                    full.append(chunk)
                    yield f"data: {json.dumps({'content': chunk})}\n\n"
                text = truncate_response("".join(full))
                conversation.add_assistant(text)
                yield f"data: {json.dumps({'done': True, 'full': text})}\n\n"

            return Response(
                generate(),
                mimetype="text/event-stream",
                headers={"Cache-Control": "no-cache", "X-Accel-Buffering": "no"},
            )
        else:
            text = ollama_chat(messages, model=model, stream=False)
            text = truncate_response(text)
            conversation.add_assistant(text)
            return jsonify({"content": text})
    except OllamaConnectionError as e:
        return jsonify({"error": str(e)}), 503
    except OllamaModelNotFoundError as e:
        return jsonify({"error": str(e)}), 404
    except OllamaError as e:
        return jsonify({"error": str(e)}), 500


@app.route("/api/history", methods=["GET"])
def api_history():
    return jsonify({"messages": conversation.history()})


def run_server(host: str = "127.0.0.1", port: int = 15555, debug: bool = False):
    app.run(host=host, port=port, debug=debug, threaded=True)
