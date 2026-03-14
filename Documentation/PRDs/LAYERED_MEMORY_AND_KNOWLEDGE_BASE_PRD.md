---
name: "Layered Memory System and Knowledge Base — Research and Solution"
overview: "Research and define a layered memory system for the harness. One layer must hold facts about files and directories in the project; the harness must use it to eliminate misalignments and hallucinations about file knowledge. Theory: knowledge embeddings relate to facts and knowledge bases; a knowledge-base implementation can assist the harness."
todos:
  - id: research-layered-memory
    content: "Research layered memory architectures (short-term vs long-term, episodic vs semantic, scope separation) and document options suitable for the harness; align with existing Orchestration/Memories design (Manifests, Indexes, DB, Prolog KB) and Plans/2026-03-12-memory-system-and-ai-ready-features-design.md"
    status: pending
  - id: research-embeddings-and-kb
    content: "Research how knowledge embeddings relate to facts and knowledge bases; document when embeddings vs symbolic KB (e.g. Prolog, JSON index) are appropriate for harness use cases (file whereabouts, references, traceability)"
    status: pending
  - id: define-layers
    content: "Define the layers of the chosen memory system: at least one dedicated layer for facts about files and directories in the project (paths, existence, parent/child, optional metadata); document schema and update triggers"
    status: pending
  - id: file-dir-facts-layer
    content: "Specify the file/dir facts layer: canonical representation (e.g. workspace_structure.json, kb.pl directory/child_dir facts, or both); how the harness and agents read/write it; when it is built or refreshed (session start, on demand, after VCS/structure change)"
    status: pending
  - id: anti-hallucination-misalignment
    content: "Define harness behavior to eliminate misalignments and hallucinations about file knowledge: agents must consult the file/dir facts layer (or verify via tool) before asserting path existence or structure; document integration with FILE_STRUCTURE_VERIFICATION and WORKSPACE_STRUCTURE_INDEX"
    status: pending
  - id: kb-implementation
    content: "Design and document a knowledge-base implementation that assists the harness: queryable facts (file/dir whereabouts, references, traceability, optional embeddings index); location (e.g. Orchestration/Memories/kb.pl and/or file-based indexes); API or contract for harness and agents to query/update"
    status: pending
  - id: solution-doc
    content: "Produce a solution document (design doc or PRD addendum) that captures the chosen layered memory design, the file/dir facts layer, the anti-hallucination policy, and the KB implementation; get human review before implementation"
    status: pending
isProject: false
---

# Layered Memory System and Knowledge Base — Research and Solution

## Purpose

Research and determine a **solution for a layered memory system** for the harness. Requirements:

1. **Layered memory:** The system must have clearly defined layers (e.g. short-term vs long-term, scopes such as project/user/agent/system, or semantic vs episodic). The solution must be informed by research and aligned with existing memory design (see Orchestration/Harness/Plans/2026-03-12-memory-system-and-ai-ready-features-design.md and MENTAL_MAP.md).

2. **File/dir facts layer:** At least one layer must **hold facts about the files and directories** in the project (paths, existence, parent/child relationships, optionally key metadata). This layer is the single source of truth for “what exists where” so agents do not rely only on glob or documentation.

3. **Eliminate misalignment and hallucination:** The harness must **use this layer to eliminate misalignments and hallucinations** about knowledge of files in the system. Agents must consult the file/dir facts layer (or perform tool-based verification per FILE_STRUCTURE_VERIFICATION) before asserting that a path exists, does not exist, or has a given structure.

4. **Knowledge base and embeddings:** The **theory** is that **knowledge embeddings** are related to **facts and knowledge bases**. An **implementation of a knowledge base** to assist the harness could be useful. The PRD includes research on when embeddings vs symbolic KB (e.g. Prolog, JSON indexes) fit harness use cases, and a concrete KB design (queryable facts, file/dir whereabouts, references, traceability) that the harness and agents can use.

## Context

- **Existing memory design:** Orchestration/Harness/Plans/2026-03-12-memory-system-and-ai-ready-features-design.md describes four pillars (Manifests, Indexes/Redis, DB, Prolog KB) and scope separation (Project, User, Agent, Repo/System). MENTAL_MAP.md defines a Prolog KB (`kb.pl`) for references, dependencies, and traceability; WORKSPACE_STRUCTURE_INDEX.md describes a workspace structure index (file or KB) for file/dir whereabouts.
- **Anti-hallucination:** Orchestration/Memories/prompts/Constraints/FILE_STRUCTURE_VERIFICATION.md requires tool-based verification and, when present, use of a workspace structure index. This PRD elevates the file/dir facts into a **dedicated layer** of the layered memory system and ties harness behavior to it so misalignment and hallucination about file knowledge are eliminated by design.
- **Related PRDs:** FEATURES_PRD (hybrid memory pipeline, promotion to durable memory); MVP_PRD (memories bootstrap); CODE_REVIEW_HARNESS_AND_API_PRD (harness path resolution, config).

## Scope

### In scope

- Research on layered memory architectures and on knowledge embeddings vs symbolic KB.
- Definition of the layers of the harness memory system, with at least one layer for file/dir facts.
- Specification of the file/dir facts layer (schema, update policy, read/write contract).
- Harness behavior and agent instructions that enforce use of the file/dir facts layer (and tool verification) to prevent file-structure hallucinations and misalignments.
- Design of a knowledge-base implementation (facts, queries, optional embeddings) that assists the harness; location and API/contract for harness and agents.

### Out of scope

- Full implementation (this PRD drives research and solution design; implementation follows in tasks or a separate implementation PRD).
- Changes to Cursor or other IDEs beyond how the harness and project docs instruct agents to use the KB and file/dir layer.

## Acceptance criteria

- A **research summary** (or appendix) documents layered memory options and the role of embeddings vs symbolic KB for the harness.
- A **layered memory design** is defined, with at least one **file/dir facts layer** and clear placement relative to existing Manifests, Indexes, DB, and Prolog KB.
- **Harness and agent behavior** are specified so that file/dir existence and structure claims are grounded in the file/dir facts layer or tool verification; misalignment and hallucination about file knowledge are eliminated by process.
- A **knowledge-base implementation** (schema, location, query/update contract) is documented and ready for human review and subsequent implementation.
- The **solution document** is produced and linked from this PRD; todos in the frontmatter are updated as work completes.

## Related documents

- Orchestration/Harness/Plans/2026-03-12-memory-system-and-ai-ready-features-design.md
- Orchestration/Memories/MENTAL_MAP.md
- Orchestration/Memories/WORKSPACE_STRUCTURE_INDEX.md
- Orchestration/Memories/prompts/Constraints/FILE_STRUCTURE_VERIFICATION.md
- Orchestration/Harness/PRDs/FEATURES_PRD.md (hybrid memory, promotion to durable memory)
