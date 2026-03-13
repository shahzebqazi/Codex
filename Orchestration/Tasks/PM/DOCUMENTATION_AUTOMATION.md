# DOCUMENTATION_AUTOMATION -- Product-Lifecycle Doc Generation

<!-- AI: Contains subprompts. When a PRD task is completed and artifacts are missing, produce product-lifecycle documentation as specified here. -->

## Role

You are responsible for **automated project documentation**. Whenever a task from a PRD is marked completed and the corresponding product-doc artifacts do not already exist, you create or update them using standard product-development lifecycle techniques.

## Trigger

- **When:** A todo in any `Project/Product/PRDs/*.md` is set to `status: completed`.
- **Condition:** The completed task does not already have corresponding entries in the project docs (user stories, use cases, etc.) for that PRD/task scope.
- **Who:** The agent that completed the PRD task, or the next agent that picks up work on that PRD or codebase, must run this protocol.

## Artifacts to Create or Update

For each completed PRD task (or coherent group of tasks), ensure the following exist under `Project/Product/`. Create only what is missing; do not overwrite existing substantive content.

| Artifact | Purpose | Path / format |
|----------|---------|----------------|
| **User stories** | Who, what, why in one-line form | `Project/Product/UserStories/<PRD_SLUG>.md` — list of "As a … I want … So that …" |
| **Use cases** | Actor–system interactions, flows | `Project/Product/UseCases/<PRD_SLUG>.md` or per-feature files — name, actor, preconditions, flow, postconditions |
| **UML** | Use case diagrams, class/sequence where useful | `Project/Product/UML/<PRD_SLUG>_*.md` or `.puml` — Mermaid or PlantUML in markdown |
| **Domain model** | Key concepts, relationships, glossary | `Project/Product/DomainModel/<PRD_SLUG>.md` — entities, relations, terms |
| **Functional requirements** | Shall-statements, traceable to PRD | `Project/Product/Requirements/Functional/<PRD_SLUG>.md` — FR-001, FR-002, … |
| **NFR** | Non-functional: performance, security, etc. | `Project/Product/Requirements/NFR/<PRD_SLUG>.md` — NFR-001, NFR-002, … |
| **Traceability** | PRD task id → user story / use case / FR | In each doc: "Source: PRD \<name\>, task \<id\>" and optional `Project/Product/Requirements/TRACEABILITY.md` index |

Other lifecycle artifacts (e.g. acceptance criteria summary, risk log, glossary) may be added under `Project/Product/` as needed; keep structure flat and discoverable.

## Protocol (Steps)

1. **Identify completed PRD tasks** — Scan PRDs in `Project/Product/PRDs/` for `status: completed` todos that do not yet have a doc-artifact reference or that lack corresponding files in `Project/Product/UserStories/`, `Project/Product/UseCases/`, etc.
2. **Check existing docs** — For that PRD/task scope, look under `Project/Product/` for existing user stories, use cases, UML, domain model, functional requirements, NFR. If present and up to date, skip creation; otherwise add or update.
3. **Create missing artifacts** — For each missing type, add one file (or section) under the path above. Use the PRD slug (e.g. `MVP`, `FEATURES`) and task id in filenames or headings so traceability is clear.
4. **Link back to PRD** — In each new or updated doc, reference the source PRD and task id(s). Optionally maintain `Project/Product/Requirements/TRACEABILITY.md` mapping task id → user story / use case / FR.
5. **Commit** — Commit new or updated docs via jj with a message that references the PRD and task (e.g. `docs(PRD): add user stories & use cases for MVP task scaffold`).

## Conventions

- **Paths:** All product-lifecycle artifacts live under `Project/Product/`. Create subdirs (UserStories, UseCases, UML, DomainModel, Requirements/Functional, Requirements/NFR) as needed.
- **Format:** Markdown first. For UML, embed Mermaid or PlantUML in markdown; optional separate `.puml` files if tools require it.
- **Naming:** Use PRD slug and optional task id in filenames, e.g. `MVP.md`, `FEATURES_auth.md`, `MVP_use_case_diagram.md`.
- **No duplicate work:** If an artifact already exists and clearly covers the completed task, only update it if the implementation added new behavior or constraints; otherwise leave it as is.

## Integration

- **Orchestrator / any agent:** After marking a PRD todo completed, run this protocol (or ensure the next agent run does) so project docs stay in sync with delivered work.
- **ADAPTIVE_ELICITATION:** When using requirements-first elicitation, the resulting requirements can be merged into `Project/Product/Requirements/` and linked from user stories/use cases created here.
- **TECHNICAL_WRITER:** Logging (worklog, buildlog) remains separate; this protocol covers product/domain/requirements documentation only.

## Full Protocol Location

- **This file:** `Orchestration/Tasks/PM/DOCUMENTATION_AUTOMATION.md`.
- **PRDs:** `Project/Product/PRDs/`.
- **Output root:** `Project/Product/`.
