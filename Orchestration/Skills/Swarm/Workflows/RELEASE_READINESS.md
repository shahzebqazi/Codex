---
id: release-readiness
name: "Release readiness"
trigger: ["release readiness", "ship check", "pre-release", "release audit"]
required_capabilities: ["subagent", "workflow"]
subagent_policy: independent_only
stages:
  - stage_id: audit
    role: "Release Auditor"
    task_type: planning
    model_key: reasoning
    context_budget: 50000
    output_budget: 6000
    depends_on: []
    inputs: ["CHANGELOG", "version tags", "PR history", "documentation/"]
    outputs: ["release checklist", "risk list", "blockers"]
    acceptance_criteria: "Checklist of release criteria; risks and blockers enumerated."
  - stage_id: security
    role: "Security Reviewer"
    task_type: review
    model_key: review
    context_budget: 40000
    output_budget: 3000
    depends_on: [audit]
    inputs: ["release checklist", "codebase hotspots", "dependency list"]
    outputs: ["security sign-off or findings"]
    acceptance_criteria: "Security review complete; no high/critical open issues or explicit waiver."
    subagent_type: code-reviewer
  - stage_id: docs
    role: "Docs Reviewer"
    task_type: docs
    model_key: default
    context_budget: 30000
    output_budget: 3000
    depends_on: [audit]
    inputs: ["release checklist", "README", "docs/", "CHANGELOG"]
    outputs: ["docs readiness note"]
    acceptance_criteria: "README and key docs updated; CHANGELOG reflects release scope."
  - stage_id: sign-off
    role: "Release Lead"
    task_type: planning
    model_key: reasoning
    depends_on: [security, docs]
    inputs: ["release checklist", "security sign-off", "docs readiness note", "blockers"]
    outputs: ["release sign-off document", "go / no-go"]
    acceptance_criteria: "Single sign-off: go or no-go with rationale and remaining follow-ups."
---

# Release readiness workflow

Pre-release audit: build a release checklist and risk list, run security and docs reviews, then produce a release sign-off (go/no-go).

**Inputs:** CHANGELOG, version/tags, PR history, documentation, dependency list.

**Outputs:** Release sign-off document with go/no-go and follow-ups.
