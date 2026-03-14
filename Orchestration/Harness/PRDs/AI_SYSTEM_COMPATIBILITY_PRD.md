---
name: "AI System Compatibility — Human Verification"
overview: "Human verification of dotAi compatibility with Anthropic, OpenAI, OpenCode, Ollama, LLM Studio, and interfaces (CLI, Desktop, API). Even when a provider or interface is documented as compatible, a human must verify compatibility before release or reliance."
todos:
  - id: verify-ollama
    content: "Human to verify compatibility with Ollama (Desktop app, base URL localhost:11434, chat completions, model list). Confirm streaming, model selector, and error handling in target environment."
    status: pending
  - id: verify-llama-server
    content: "Human to verify compatibility with llama-server / GGUF (default endpoint :8080, Docker, SETTINGS.json). Confirm OpenAI-compatible API and model load in target environment."
    status: pending
  - id: verify-openai-cloud
    content: "Human to verify compatibility with OpenAI cloud (if/when implemented). Check API key, base URL, and chat completions behavior; document any gaps vs docs."
    status: pending
  - id: verify-anthropic
    content: "Human to verify compatibility with Anthropic (Claude) if adapter or integration is added. Confirm request/response mapping and config (provider, model, API key)."
    status: pending
  - id: verify-opencode
    content: "Human to verify compatibility with OpenCode when integration exists (CLI, API, or conventions). Document integration points and any deviations from OpenCode expectations."
    status: pending
  - id: verify-llm-studio
    content: "Human to verify compatibility with LLM Studio (or LM Studio / equivalent). If OpenAI-compatible endpoint is used, confirm endpoint URL, model names, and behavior in target environment."
    status: pending
  - id: verify-cli
    content: "Human to verify CLI compatibility (Codex, Claude Code, or other runtimes that drive dotAi). Confirm entry point (e.g. START_HERE.md), config (SETTINGS.json), and model endpoint usage."
    status: pending
  - id: verify-desktop
    content: "Human to verify Desktop app compatibility (Electron + Ollama + LFM2). Confirm install, run, chat, model selector, and guard rails in target OS and environment."
    status: pending
  - id: verify-api
    content: "Human to verify API compatibility (OpenAI-shaped model_endpoint, /v1/chat/completions). Confirm config location, endpoint override, and behavior with at least one live backend (e.g. Ollama or llama-server)."
    status: pending
  - id: update-compatibility-review
    content: "After human verification of each item above, update Project/Product/Reports and Reviews/AI_SYSTEM_COMPATIBILITY_REVIEW.md with verification date, environment, and any caveats or fixes."
    status: pending
isProject: false
---

# AI System Compatibility — Human Verification

## Principle

**Even when a provider or interface is documented as compatible, a human must verify compatibility.** Automated or doc-based “compatible” claims are not sufficient for release or reliance. Each item in the todos above requires a human to run the relevant flow in a real environment and confirm behavior.

## Scope

- **Providers:** Anthropic (Claude), OpenAI (cloud), OpenCode, Ollama, llama-server (GGUF), LLM Studio (or equivalent).
- **Interfaces:** CLI (e.g. Codex, Claude Code), Desktop (Electron + Ollama), API (OpenAI-compatible `model_endpoint`).

## Reference

- Full compatibility analysis: [AI_SYSTEM_COMPATIBILITY_REVIEW.md](../Reports%20and%20Reviews/AI_SYSTEM_COMPATIBILITY_REVIEW.md)
- Config: `Orchestration/Memories/SETTINGS.json` (`model_endpoint`, `default_model`)
- Desktop spec: `Orchestration/Agents/AGENT_PROMPT.md`, `Project/Product/PRDs/FEATURES_PRD.md`

## Verification checklist (summary)

| Id | Item | Human action |
|----|------|--------------|
| verify-ollama | Ollama | Run Desktop app (or client) against local Ollama; confirm chat, streaming, model list, errors. |
| verify-llama-server | llama-server (GGUF) | Run `docker-compose up llama-server`, point agent/config at :8080; confirm completions. |
| verify-openai-cloud | OpenAI cloud | If implemented: set endpoint + key, run a completion; confirm behavior and docs. |
| verify-anthropic | Anthropic (Claude) | If implemented: run adapter against Claude API; confirm mapping and config. |
| verify-opencode | OpenCode | When integration exists: run OpenCode CLI/API against dotAi; document findings. |
| verify-llm-studio | LLM Studio | Point `model_endpoint` at LM Studio (or equivalent) server; confirm completions and model name. |
| verify-cli | CLI | Run dotAi via Codex/Claude Code (or other runtime); confirm START_HERE, config, endpoint. |
| verify-desktop | Desktop | Install and run Electron app; confirm Ollama connection, UI, guard rails. |
| verify-api | API | Use SETTINGS `model_endpoint` with one backend; confirm chat completions and overrides. |
| update-compatibility-review | Update review doc | After verifications, add dates, environment, and caveats to AI_SYSTEM_COMPATIBILITY_REVIEW.md. |

## Completion

When all todos are verified and the review doc is updated, mark each todo `status: completed` in this PRD and optionally add a short “Last verified” note (date, environment) at the top of [AI_SYSTEM_COMPATIBILITY_REVIEW.md](../AI_SYSTEM_COMPATIBILITY_REVIEW.md).
