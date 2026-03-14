# Documents

This directory contains **only this README**. It explains where project and user documents live.

## For users

All **project and user documents** (PRDs, requirements, user stories, use cases, domain model, UML, traceability, reports, plans, prompts, references) live under **Project/Product/**.

- **PRDs:** `Project/Product/PRDs/`
- **Requirements:** `Project/Product/Requirements/` (Functional, NFR, TRACEABILITY.md)
- **User stories:** `Project/Product/UserStories/`
- **Reports and reviews:** `Project/Product/Reports and Reviews/`
- **Plans:** `Project/Product/Plans/`
- **Papers:** `Project/Product/Papers/`
- **References:** `Project/Product/References/` (e.g. URLS.md)
- **Prompts:** `Project/Product/Prompts/` (create if missing)

Do not add subdirectories or files under `Documents/`; use `Project/Product/` instead.

## For AI agents

- **Authority:** See [SYSTEM_PROMPT.md](../SYSTEM_PROMPT.md) first.
- **Project and user documents:** All such artifacts live under **Project/Product/** (see paths above). When documentation automation or PRD work produces artifacts, create them under `Project/Product/` as specified in SYSTEM_PROMPT.
- **AI harness documentation:** See **Project/Product/AI/** for harness documentation and links to `Orchestration/Harness/`.
