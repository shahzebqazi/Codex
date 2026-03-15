# Workflow registry

This directory holds **workflow definitions** for workflow-based swarms. Each file is a markdown document with YAML frontmatter conforming to [WORKFLOW_SCHEMA.md](../WORKFLOW_SCHEMA.md).

## Registered workflows

| File | id | trigger keywords | Stages |
|------|-----|------------------|--------|
| [CONSOLIDATE_PRDS.md](CONSOLIDATE_PRDS.md) | consolidate-prds | consolidate, merge PRDs, consolidated PRD | 5 (coordinator → 3 reviewers → final writer) |
| [CODE_REVIEW_PIPELINE.md](CODE_REVIEW_PIPELINE.md) | code-review-pipeline | code review pipeline, review PR, multi-reviewer | 4 (decompose → code review → infra review → merge) |
| [RELEASE_READINESS.md](RELEASE_READINESS.md) | release-readiness | release readiness, ship check, pre-release | 4 (audit → security → docs → sign-off) |

## Adding a workflow

1. Create a new markdown file in this directory (e.g. `MY_WORKFLOW.md`).
2. Add YAML frontmatter with required fields: **id**, **name**, **stages**. Optional: **trigger**, **required_capabilities**, **subagent_policy**, **concurrency**.
3. Define **stages** with at least: stage_id, role, task_type. Add depends_on, model_key, context_budget, output_budget, inputs, outputs, acceptance_criteria, subagent_type as needed.
4. Register the workflow in the table above in this README.
5. Ensure **depends_on** references only existing stage_ids and the graph is acyclic.

## Resolution

When the user invokes the swarm skill with a workflow keyword, the agent matches the user’s message against each workflow’s **trigger** list. The first matching workflow is used, or the user can specify **workflow &lt;id&gt;** to pick by id.
