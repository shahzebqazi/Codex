# Aura

**An intuitive AI agent tool.** Deploy AI into any project with markdown and local inference — no protocol servers, no cloud APIs. A declarative alternative to MCP.

## What is this?

Aura (the `.ai` system) is a markdown-first AI agent orchestration system. Everything in the `.ai/` directory is a prompt for AI: no JSON-RPC, no protocol servers — just markdown files, version control (jj), and local model inference (GGUF via llama-server). Clone a repo, point your agent at `.ai/START_HERE.md`, and go.

---

## For Humans

### Prerequisites

- **Docker** — for running llama-server (local GGUF inference)
- **jj** (Jujutsu) — optional but recommended; colocated with git for agent commits
- **A GGUF model** — place in `./models/` (see Local Model Serving below)

### Quick Start

1. Clone this repo.
2. Read `.ai/START_HERE.md` (entry point for agents) then `.ai/GUIDE.md` (full system).
3. To run a local model: `docker-compose up llama-server` — API at `http://localhost:8080`.

### Key Concepts

- **Everything is a prompt** — all `.ai/` files are markdown designed for AI to read.
- **Files are the protocol** — no servers; agents discover skills by scanning the filesystem.
- **jj commits are the communication bus** — agents coordinate via Jujutsu commit messages.
- **Local-first** — GGUF models via llama-server; no cloud APIs required.
- **No guardrails by default** — agents have full autonomy unless you set rules.

### Structure

```
.ai/
  START_HERE.md      — agent entry point
  GUIDE.md           — system documentation
  project/           — rules, system config, PRDs
  skills/            — agent capabilities (30+ skills)
  agents/            — per-agent state files
  memories/          — reinforcement and project knowledge
  config/            — settings (with gitignored local overrides)
  references/        — cataloged external links
  documentation/     — whitepaper and formal docs
  extensions/        — add-ons
```

### Local Model Serving

```bash
# Place a GGUF model in ./models/
# Start llama-server
docker-compose up llama-server
```

The server exposes an OpenAI-compatible API at `http://localhost:8080`.

---

## For AI Agents

**Entry point:** [.ai/START_HERE.md](.ai/START_HERE.md)

Open that file first. It links to GUIDE.md, your rules (project/RULES.md), system config (project/SYSTEM.md), and the skills directory. You will understand the system and can begin working in under two minutes.

---

## License

Open source. See LICENSE for details.
