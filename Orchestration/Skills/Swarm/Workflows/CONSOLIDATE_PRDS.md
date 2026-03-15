---
id: consolidate-prds
name: "Consolidate PRDs"
trigger: ["consolidate", "merge PRDs", "consolidated PRD"]
required_capabilities: ["subagent", "parallel_context", "workflow"]
subagent_policy: full
stages:
  - stage_id: coordinator
    role: Coordinator
    task_type: planning
    model_key: reasoning
    turns: "8-12"
    context_budget: 60000
    output_budget: 6000
    depends_on: []
    inputs: ["Documentation/PRDs/*"]
    outputs: ["consolidated structure", "delegation plan"]
    acceptance_criteria: "Single structured plan with team assignments and priority tiers; all PRD todos mapped."
  - stage_id: xp-review
    role: "XP Engineer Reviewer"
    task_type: review
    model_key: code
    turns: "5-8"
    context_budget: 50000
    output_budget: 4000
    depends_on: [coordinator]
    inputs: ["code PRDs", "harness PRDs", "MVP PRD", "repo-consistency PRD"]
    outputs: ["xp track review"]
    acceptance_criteria: "XP track items reviewed with acceptance criteria; dependencies noted."
    subagent_type: generalPurpose
  - stage_id: graphics-review
    role: "Graphics/Brand Reviewer"
    task_type: review
    model_key: default
    turns: "4-6"
    context_budget: 40000
    output_budget: 3000
    depends_on: [coordinator]
    inputs: ["pixel art swarm PRD", "features UI PRD", "README PRD"]
    outputs: ["graphics track review"]
    acceptance_criteria: "Graphics track items reviewed; visual specs and asset refs noted."
    subagent_type: generalPurpose
  - stage_id: infra-review
    role: "Infra Reviewer"
    task_type: review
    model_key: default
    turns: "4-6"
    context_budget: 40000
    output_budget: 3000
    depends_on: [coordinator]
    inputs: ["code review PRD (infra)", "MVP (docker/CI)", "compatibility PRD"]
    outputs: ["infra track review"]
    acceptance_criteria: "Infra track items reviewed; runbooks and config refs noted."
    subagent_type: generalPurpose
  - stage_id: final-writer
    role: "Final Writer"
    task_type: docs
    model_key: reasoning
    depends_on: [xp-review, graphics-review, infra-review]
    inputs: ["all review outputs", "coordinator structure"]
    outputs: ["Documentation/PRDs/CONSOLIDATED_TEAM_PRD.md"]
    acceptance_criteria: "Single consolidated PRD with all tracks, de-duplicated items, team field per todo, traceability appendix."
    subagent_type: generalPurpose
---

# Consolidate PRDs workflow

Reads all PRDs in `Documentation/PRDs/`, then produces one structured consolidated PRD that XP engineers, graphics/brand artists, and infrastructure personnel can execute from.

**Source prompt reference:** Documentation/Prompts/CONSOLIDATE_PRDS_PROMPT.md (canonical instructions for the consolidated output format and rules).

**Output:** `Documentation/PRDs/CONSOLIDATED_TEAM_PRD.md` with YAML frontmatter (name, overview, todos with team field), executive summary, architecture overview, per-track sections, cross-team dependencies, iteration plan, deferred items, and appendix.
