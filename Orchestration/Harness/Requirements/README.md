# Requirements

This directory holds **functional requirements (FR)** and **non-functional requirements (NFR)** for the dotAi system, plus traceability to PRDs and user stories.

| Artifact | Path | Description |
|----------|------|-------------|
| **Functional requirements** | [Functional/FUNCTIONAL_REQUIREMENTS.md](Functional/FUNCTIONAL_REQUIREMENTS.md) | Shall-statements (FR-001, FR-002, …), including **system initiation**: first user input must be START_HERE.md or equivalent. |
| **Non-functional requirements** | [NFR/NON_FUNCTIONAL_REQUIREMENTS.md](NFR/NON_FUNCTIONAL_REQUIREMENTS.md) | Security, privacy, performance, usability, operability, compliance (NFR-001, …). |
| **Traceability** | [TRACEABILITY.md](TRACEABILITY.md) | Mapping from PRDs and docs to user stories, FR, and NFR. |

**User stories** live in `Project/Product/UserStories/USER_STORIES.md` (As a … I want … So that …).

**Convention:** When adding or changing requirements, update TRACEABILITY.md and reference the source PRD or task id in the requirement doc.
