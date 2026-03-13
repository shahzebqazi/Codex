---
name: "README rewrite — Aura public release"
overview: "An AI agent rewrites the project README for Aura (the .ai system) so it is clear, compelling, and ready for public release: correct branding, fixed copy, distinct audiences (humans vs AI agents), and a polished quick start."
todos:
  - id: readme-audit
    content: "Audit current README and user draft: fix incomplete sentences, typos (e.g. intuituve), and note what to keep vs replace"
    status: pending
  - id: readme-positioning
    content: "Rewrite hero and 'What is this?' for Aura: intuitive AI agent tool, alternative to MCP, deploy AI into any project; one clear value sentence"
    status: pending
  - id: readme-audiences
    content: "Separate and polish sections for Humans (quick start, concepts, structure, local model) and For AI Agents (entry point, how to begin); ensure both can succeed in under 2 minutes"
    status: pending
  - id: readme-structure
    content: "Keep or refine Key Concepts and .ai/ structure tree; add Prerequisites if needed (Docker, jj, GGUF model) for clone-to-running clarity"
    status: pending
  - id: readme-polish
    content: "Final pass: consistent voice, no internal jargon without explanation, License/links, and public-release tone (confident, minimal, no placeholders)"
    status: pending
isProject: false
---

# README rewrite — Aura public release

## Context

The project (Aura / the .ai system) is a declarative, markdown-first AI agent orchestration system — an alternative to protocol-based systems like MCP. The README must serve **public release**: first-time visitors (humans and AI agents) should immediately understand what Aura is, why it matters, and how to get started. A previous draft introduced the "Aura" framing and an "intuitive AI agent tool" line but had an incomplete sentence ("The"), a typo ("intuituve"), and minimal structure. The existing Codex README has good content (quick start, key concepts, structure, local model); the rewrite should preserve that substance while aligning with Aura branding and public-release quality.

## Scope

### In scope

- Single file: `README.md` at repo root (Codex).
- Positioning: Aura as an intuitive AI agent tool; deploy AI into any project; alternative to MCP; no protocol servers, just markdown + jj + local GGUF.
- Two audiences: **Humans** (quick start, concepts, structure, how to run) and **AI Agents** (entry point: `.ai/START_HERE.md`, how to begin).
- Copy fixes: complete all sentences, fix typos, remove placeholders.
- Public-release tone: clear, confident, minimal; no internal-only jargon without a one-line explanation.
- Optional: short Prerequisites line (e.g. Docker, jj, a GGUF model) if it improves clone-to-running clarity.

### Out of scope

- Changing `.ai/` directory layout or START_HERE.md / README.md (User guide) content (README only).
- Adding screenshots, logos, or long prose; keep the README scannable.
- Separate docs (e.g. CONTRIBUTING); link only if they already exist.

## Acceptance criteria

- README title/hero clearly names or implies **Aura** and states the one-line value (intuitive AI agent tool, deploy into any project, alternative to MCP).
- No incomplete sentences or typos.
- "Quick Start" works for a human (clone, read START_HERE then GUIDE) and "For AI Agents" tells an agent exactly where to point (`.ai/START_HERE.md`).
- Key concepts and `.ai/` structure remain (or are refined); local model serving (Docker/llama-server) is documented.
- Voice is consistent and suitable for public release; no internal slang unexplained.
- License or "See LICENSE" (or equivalent) present.

## Done when

- `README.md` has been rewritten and meets the acceptance criteria above.
- An AI agent (or human) can execute the todos in order and produce a single, shippable README.
