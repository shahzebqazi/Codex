# Run harness API server for Lua GUI.
# Usage: python -m harness [--port 15555]
import argparse
from .server import run_server

def main():
    p = argparse.ArgumentParser(description="Harness API server (Ollama backend)")
    p.add_argument("--host", default="127.0.0.1", help="Bind host")
    p.add_argument("--port", type=int, default=15555, help="Bind port")
    p.add_argument("--debug", action="store_true", help="Flask debug")
    args = p.parse_args()
    run_server(host=args.host, port=args.port, debug=args.debug)

if __name__ == "__main__":
    main()
