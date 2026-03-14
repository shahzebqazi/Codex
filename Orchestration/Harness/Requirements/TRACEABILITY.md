# Requirements Traceability

**Purpose:** Map PRD tasks and documentation sources to user stories, functional requirements (FR), and non-functional requirements (NFR).  
**User stories:** `Project/Product/UserStories/USER_STORIES.md`
**Functional requirements:** `Project/Product/Requirements/Functional/FUNCTIONAL_REQUIREMENTS.md`
**Non-functional requirements:** `Project/Product/Requirements/NFR/NON_FUNCTIONAL_REQUIREMENTS.md`

---

## System initiation (START_HERE as first input)

| Source | User story | Functional requirement | Notes |
|--------|------------|------------------------|------|
| Project requirement (user-specified) | US-INIT-001, US-INIT-002, US-INIT-003 | FR-001, FR-002, FR-003, FR-004, FR-005 | User must initiate with START_HERE.md (or equivalent) as first chat/LLM input; otherwise system does not initialize and prompts for creative/debug mode or guides to docs. |

## Edit mode vs debug mode

| Source | User story | Functional requirement | Notes |
|--------|------------|------------------------|------|
| Project requirement (user-specified) | US-MODE-001, US-MODE-002, US-MODE-003 | FR-024, FR-025, FR-026 | Edit mode: user not prevented from editing dotAi system. Debug mode: if user tries to edit project docs, alert and offer “new chat in edit mode” or “switch this chat to edit mode”; allow switching between debug and edit mode. |

---

## PRD → artifacts

| PRD / doc | User stories | FR | NFR |
|-----------|--------------|-----|-----|
| README, START_HERE, CONTRIBUTING | US-ONB-*, US-AGENT-*, US-SAFE-*, US-DOC-*, US-BASE-* | FR-020–FR-023, FR-040–FR-044, FR-080–FR-083, FR-100–FR-103, FR-120–FR-121, FR-140–FR-141 | NFR-001–NFR-003, NFR-020–NFR-022, NFR-040–NFR-043, NFR-060–NFR-062, NFR-100–NFR-102 |
| MVP_PRD | US-ONB-003, US-AGENT-*, US-BASE-* | FR-020–FR-023 | NFR-062 |
| FEATURES_PRD | US-CFG-*, US-SAFE-003 | FR-080–FR-083, FR-103 | NFR-023, NFR-082 |
| RULES | US-AGENT-*, US-SAFE-* | FR-041, FR-042, FR-120, FR-121 | NFR-043, NFR-102 |
| DOCUMENTATION_AUTOMATION | US-AGENT-005, US-DOC-002 | FR-044, FR-141 | — |
| RELEASE_READINESS_REPORT | — | — | NFR-004, NFR-080, NFR-081, NFR-120, NFR-121 |

---

*Update this index when adding or changing user stories, FR, or NFR.*
