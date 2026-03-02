# AGI Research & Testing Toolkit

**Executive Summary**

A high-performance AI Research & Testing Toolkit inspired by the [LFM2 (Liquid Foundation Model)](https://arxiv.org/abs/2511.23404) architecture—prioritizing efficiency, hybrid MoE (Mixture of Experts), and edge-compatibility. This repository supports **Learning**, **Research**, and **Testing** workflows with a 2-month scope for initial delivery.

## Scope (2 Months)

| Phase   | Focus                          | Deliverables                          |
|--------|----------------------------------|----------------------------------------|
| **Learning**  | LFM2 hardware-in-the-loop methodology, MoE scaling, agent memory | Docs, playbooks, verified ref links |
| **Research**  | Ref-Agent link verification, academic synthesis, benchmarking APIs | PLAYBOOK, PM_DOC, PRD, RESEARCH_DOC |
| **Testing**   | Docker/venv dev environment, GitHub Pages deployment, license audit | Repo structure, CI-ready baseline |

Methodology is aligned with LFM2’s hardware-in-the-loop search: iterative, efficiency-focused, and suitable for edge and on-device deployment (e.g. [LFM2-8B MoE](https://www.liquid.ai/blog/lfm2-8b-a1b-an-efficient-on-device-mixture-of-experts)).

## Branch Structure

- **main** — Production/stable
- **research** — Primary working branch for docs and tools (current)
- **development** — Feature staging
- **training** — Model training scripts
- **benchmarking** — Evaluation suites

## Quick Start

1. Clone and checkout `research`: `git checkout research`
2. Create venv: `python -m venv venv` then `source venv/bin/activate` (or `venv\Scripts\activate` on Windows)
3. Run stack: `docker compose --profile full up` (see [PLAYBOOK.md](PLAYBOOK.md))

## Documentation

- [PLAYBOOK.md](PLAYBOOK.md) — Docker, venv, GitHub Pages deployment
- [PM_DOC.md](PM_DOC.md) — Critical path, onboarding, Apache 2.0 licensing
- [PRD.md](PRD.md) — Harnessed Agent Swarm goals (context scaling, multi-modal, guardrails)
- [RESEARCH_DOC.md](RESEARCH_DOC.md) — Academic review: Liquid FMs, MoE scaling, agent memory

## License

Apache 2.0. See [PM_DOC.md](PM_DOC.md) for licensing strategy.
