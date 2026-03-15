# Workflow definition schema — YAML + Markdown

Workflow definitions are markdown files with YAML frontmatter. They declare pipeline stages, agent roles, token budgets, and routing so the swarm generator can produce executable swarm plans and configs.

## File format

```yaml
---
id: <unique-workflow-id>
name: "<human-readable name>"
trigger: ["keyword1", "keyword2", ...]
required_capabilities: ["subagent", "parallel_context", "workflow"]
subagent_policy: none | independent_only | per_prd | full
concurrency: [["stage_a", "stage_b"], ["stage_c"]]
stages: [...]
---
```

Optional body: Markdown description, runbook, or references.

## Top-level fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| **id** | string | yes | Unique workflow identifier (e.g. `consolidate-prds`, `release-review`, `code-review-pipeline`). Used in `workflow_ref` in swarm config. |
| **name** | string | yes | Human-readable name for display and confirmation prompts. |
| **trigger** | string[] | no | Keywords or phrases that invoke this workflow when the user says them. Used by swarm routing. |
| **required_capabilities** | string[] | no | Capabilities the harness must have to run this workflow. Checked against `SETTINGS.json → swarm.capabilities`. Examples: `subagent`, `parallel_context`, `workflow`. |
| **subagent_policy** | string | no | When to use subagents: `none`, `independent_only`, `per_prd`, `full`. See [SUBAGENTS.md](SUBAGENTS.md). Default: `independent_only`. |
| **concurrency** | array of string[] | no | Which stage_ids can run in parallel. Each inner array is one parallel wave. Omit for generator to infer from `depends_on`. |
| **stages** | stage[] | yes | Ordered pipeline stages. Each stage maps to one or more tasks in the emitted swarm config. |

## Stage object

Each element of `stages` has:

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| **stage_id** | string | yes | Unique within the workflow (e.g. `coordinator`, `xp-review`, `final-writer`). Used in `depends_on`. |
| **role** | string | yes | Agent role name for display and prompts (e.g. "Coordinator", "XP Engineer Reviewer"). |
| **task_type** | string | yes | From [MODELS_AND_EXPERTS.md](MODELS_AND_EXPERTS.md): `planning`, `code`, `review`, `refactor`, `docs`, `research`. |
| **model_key** | string | no | Model routing key: `default`, `local`, `reasoning`, `code`, `review`, or project-defined. |
| **context_budget** | integer | no | Input token budget for this stage (e.g. 60000). Used for allocation and caps. |
| **output_budget** | integer | no | Output token budget (e.g. 6000). |
| **turns** | string or integer | no | Estimated conversation turns (e.g. `8-12` or `5`). Informational. |
| **depends_on** | string[] | no | stage_ids that must complete before this stage. Empty or omit for no dependencies. |
| **inputs** | string[] | no | What this stage reads: paths, PRD refs, or "output of stage X". |
| **outputs** | string[] | no | What this stage produces: file paths, artifact names, or descriptions. |
| **acceptance_criteria** | string | no | One-line criteria for stage completion. |
| **subagent_type** | string | no | For Cursor/MCP: `generalPurpose`, `explore`, `shell`, `code-reviewer`, `other`. See [SUBAGENTS.md](SUBAGENTS.md). |

## Example (minimal)

```yaml
---
id: quick-review
name: "Quick review"
trigger: ["quick review", "fast review"]
subagent_policy: none
stages:
  - stage_id: reviewer
    role: Reviewer
    task_type: review
    model_key: review
    depends_on: []
    inputs: ["changed files"]
    outputs: ["review summary"]
    acceptance_criteria: "Review summary with approve/request-changes and inline suggestions."
---
```

## Example (full)

See [Workflows/CONSOLIDATE_PRDS.md](Workflows/CONSOLIDATE_PRDS.md) for a full workflow with multiple stages, token budgets, and dependencies.

## Validation

- **stage_id** must be unique within `stages`.
- **depends_on** must reference only existing stage_ids. The dependency graph must be acyclic.
- **task_type** should be one of: planning, code, review, refactor, docs, research.
- **subagent_policy** must be one of: none, independent_only, per_prd, full.

When emitting swarm config, the generator maps each stage to one or more tasks in [swarm_config.schema.json](swarm_config.schema.json) (or [workflow_config.schema.json](workflow_config.schema.json)), preserving role, model_key, depends_on, acceptance_criteria, and optional context_budget, output_budget, inputs, outputs.
