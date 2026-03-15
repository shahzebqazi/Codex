---
id: code-review-pipeline
name: "Code review pipeline"
trigger: ["code review pipeline", "review PR", "multi-reviewer review"]
required_capabilities: ["subagent", "workflow"]
subagent_policy: independent_only
stages:
  - stage_id: decompose
    role: "Review Coordinator"
    task_type: planning
    model_key: reasoning
    context_budget: 40000
    output_budget: 4000
    depends_on: []
    inputs: ["PR diff", "PR description", "changed files list"]
    outputs: ["review checklist", "assignments by area"]
    acceptance_criteria: "Checklist of areas to review; assignment of areas to reviewer roles."
  - stage_id: code-review
    role: "Code Reviewer"
    task_type: review
    model_key: code
    context_budget: 50000
    output_budget: 5000
    depends_on: [decompose]
    inputs: ["PR diff", "review checklist (code areas)"]
    outputs: ["code review report"]
    acceptance_criteria: "Structured review: correctness, style, tests, edge cases; inline suggestions where applicable."
    subagent_type: code-reviewer
  - stage_id: infra-review
    role: "Infra / CI Reviewer"
    task_type: review
    model_key: default
    context_budget: 30000
    output_budget: 2000
    depends_on: [decompose]
    inputs: ["PR diff (CI/config)", "review checklist (infra areas)"]
    outputs: ["infra review report"]
    acceptance_criteria: "CI, config, and deployment impact noted; security and env concerns flagged."
    subagent_type: generalPurpose
  - stage_id: merge-report
    role: "Merge Reporter"
    task_type: docs
    model_key: default
    depends_on: [code-review, infra-review]
    inputs: ["code review report", "infra review report", "review checklist"]
    outputs: ["merged review summary", "approve / request-changes recommendation"]
    acceptance_criteria: "Single summary; clear approve or request-changes with rationale."
---

# Code review pipeline

Multi-stage code review: decompose PR into review areas, run code and infra reviews (optionally in parallel), then produce a merged review summary and recommendation.

**Inputs:** PR diff, description, and list of changed files (from context or paths).

**Outputs:** Merged review summary and approve/request-changes recommendation.
