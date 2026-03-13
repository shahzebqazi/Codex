# Protocol: Adaptive Requirements Elicitation

<!-- AI: Contains subprompts. Scan for task-specific instructions. -->
<!-- PREFERENCES (edit for your project): (none) -->

This protocol defines how agents elicit requirements before writing code, so outputs stay aligned with user constraints and architecture. **Everything in this system is a prompt;** this document is the canonical process prompt for requirements-first execution.

## Terminology

- **Tasks** — Primary term for capability units and work items. Tasks live in `Orchestration/Tasks/` (families: SWE, VCS, INFRA, DATA, TOOLS, PM, OS, etc.). See `Orchestration/Tasks/README.md`.
- **Sub-tasks** — Steps or sub-steps within a task. If a sub-task grows complex, the agent must **rebalance**: extract it into a new, separate task under `Orchestration/Tasks/`.
- **Skills** — Command keywords (e.g. summarize, generate) with deterministic effects; live in `Orchestration/Skills/`. Distinct from tasks.

## The Protocol (Four Phases)

### 1. ELICITATION

When the user states a request, **do not write code immediately.** Ask 1–3 targeted, specific questions to clarify:

- **Constraints** — Stack, runtime, security, compliance, performance.
- **Preferred languages/tools** — Frameworks, versions, style.
- **Edge cases** — Boundaries, failure modes, backward compatibility.
- **Architecture** — Where it lives, how it integrates, ownership.

Keep questions concise and actionable. If the request is already fully specified, confirm that and move to Confirmation.

### 2. CONFIRMATION

After the user answers (or confirms no further clarification is needed), state your understanding in 2–4 sentences:

- What will be built or changed.
- Key constraints and choices.
- Any assumptions you are making.

Invite a quick correction before proceeding. Only then move to Execution.

### 3. EXECUTION

Implement the required behavior:

- Code cleanly and efficiently.
- Comment the logic so intent and non-obvious decisions are clear.
- Follow project rules (see project/RULES.md) and existing patterns.

### 4. SKILL EXTRACTION (Task Extraction)

After delivering the work, produce a **Skill Payload** (task summary) so the system can persist reusable logic. Use this format:

```markdown
<skill_payload>
{
  "skill_name": "Brief name of the task/skill",
  "description": "What this does",
  "requirements_learned": "Key constraints or user preferences discovered",
  "code_pattern": "A brief, abstracted snippet of the core logic"
}
</skill_payload>
```

Save or append this payload to the project’s task/skill memory (e.g. a tasks index or the relevant task file) so future agents can reuse the learned constraints and patterns.

## Rebalancing Rule

- **Everything is a prompt.** Tasks and sub-tasks are prompts; when a sub-task becomes complex (many steps, multiple files, or distinct capability), the agent must **rebalance**:
  1. Create a **new, separate task** with a clear scope and entry point.
  2. Optionally add a task file under `Orchestration/Tasks/` so other agents can discover and load it.
  3. Reference the new task from the parent task instead of inlining the complex logic.

This keeps tasks small, discoverable, and reusable without overloading a single prompt.

## Where This Protocol Lives

- **Full protocol (this file):** `project/PROTOCOL_REQUIREMENTS_ELICITATION.md`
- **Master task for agents:** `Orchestration/Tasks/SWE/ADAPTIVE_ELICITATION.md` — load that task when acting as an Adaptive Requirements Elicitation Agent; it mirrors this protocol as tasks and sub-tasks and points here for detail.
