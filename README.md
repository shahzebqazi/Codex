# dotAi

A declarative, markdown-first AI agent orchestration system. An alternative to protocol-based systems like MCP.

## What is this?

Everything in the `.ai/` directory is a prompt for AI. There are no protocol servers, no JSON-RPC -- just markdown files, a version control system (jj), and local model inference (GGUF via llama-server).

## Quick Start

1. Clone this repo
2. Read `.ai/START_HERE.md` -- that's the entry point for any AI agent
3. Read `.ai/GUIDE.md` -- explains the full system

## For AI Agents

Point your AI coding agent at `.ai/START_HERE.md`. It will understand the system and begin working.

## For Humans

Read `.ai/GUIDE.md` for the complete system explanation, directory layout, and conventions.

## Local Model Serving

```bash
# Place a GGUF model in ./models/
# Start llama-server
docker-compose up llama-server
```

The server exposes an OpenAI-compatible API at `http://localhost:8080`.

## Key Concepts

- **Everything is a prompt** -- all `.ai/` files are markdown designed for AI to read
- **Files are the protocol** -- no servers needed, agents discover skills by scanning the filesystem
- **jj commits are the group chat** -- AI agents communicate via brief Jujutsu commit messages
- **Local-first** -- runs on GGUF models via llama-server, no cloud APIs required
- **No guardrails by default** -- agents have full autonomy unless you set rules

## Structure

```
.ai/
  START_HERE.md      -- agent entry point
  GUIDE.md           -- system documentation
  project/           -- rules, system config, PRDs
  skills/            -- agent capabilities (30+ skills)
  agents/            -- per-agent state files
  memories/          -- RL data and project knowledge
  config/            -- settings (with gitignored local overrides)
  references/        -- 80+ cataloged external links
  documentation/     -- whitepaper and formal docs
  extensions/        -- add-ons
```

## License

Open source. See LICENSE for details.
