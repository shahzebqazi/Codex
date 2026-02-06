---
name: ralph-loop-parallel-agents
description: Orchestrate multiple coding agents in parallel using a Ralph-style loop driven by a single PRD. Use when you need to extract requirements, detect ambiguity, define interfaces, decompose work into dependency-aware tasks, coordinate agents across branches, and maintain traceability with progress logs and acceptance criteria.
---

# Ralph Loop Parallel Agents

## Overview

Coordinate parallel, PRD-driven coding agents using a repeatable Ralph loop and a shared artifact set so each agent works independently without drifting from the PRD.

## Quick Start

1. Identify the PRD and store it in a stable location (prefer `.ai/PRD.md` if present).
2. Run `scripts/prd_to_requirements.py` to seed a requirements pool JSON.
3. Create the core artifacts (see `references/artifacts.md`).
4. Define interface contracts before assigning parallel tasks.
5. Spawn per-agent branches and run the Ralph loop for each assigned task.

## Workflow

1. Intake PRD
- Read the PRD end-to-end.
- Produce a requirements pool with acceptance criteria.
- Capture unresolved ambiguity as explicit questions.

2. Design Interfaces
- Define shared interfaces, data contracts, and file boundaries.
- Publish contracts before any agent starts implementation.

3. Decompose Into Parallel Tasks
- Build a dependency graph and label tasks with owners.
- Keep each task small enough to finish in one iteration.
- Require a single PRD item per iteration to reduce context drift.

4. Run the Ralph Loop
- For each task, run a fresh agent iteration.
- The iteration should: implement, run quality checks, update artifacts, and commit.
- Persist memory only in artifacts and git history, not in the agent context.

5. Validate And Merge
- Run tests/typechecks and enforce acceptance criteria.
- Merge only when interfaces and tests pass.
- Update progress/decision logs and mark PRD items complete.

## Core Artifacts (Required)

- `prd.json` - Requirement pool with acceptance criteria and `passes` status.
- `task_graph.yaml` - Dependency-aware task list with owners.
- `progress.txt` - Append-only learnings and context.
- `decision_log.md` - Important decisions and rationale.
- `interface_contracts.md` - Public contracts used across parallel tasks.

Templates live in `references/artifacts.md`.

## Parallel Coordination Protocol

- Use one branch per agent (`codex/<agent>/<task>`).
- Require agents to read `AGENTS.md` and append discoveries.
- Use deterministic status updates in `progress.txt`:
  - `[date] [agent] [task-id] started`
  - `[date] [agent] [task-id] complete`
  - `[date] [agent] [task-id] blocked: reason`
- Mark `passes: true` only after tests and review.

## Ralph Loop Guidelines

- Each iteration starts with a clean context and ends with artifact updates.
- Use tests/typechecks as mandatory feedback loops.
- Split tasks that exceed a single context window.
- Stop only when all PRD items are `passes: true`.
- Follow `references/ralph-loop-notes.md` for concrete patterns.

## OpenClaw-Inspired Control Plane (Optional)

If you need a long-lived coordinator or multi-channel control surface:
- Use a gateway/control-plane pattern to route events and manage sessions.
- Keep routing rules and agent workspaces isolated.
- See `references/openclaw-gateway-notes.md` for patterns to borrow.

## Resources

- `scripts/prd_to_requirements.py` - Seed a requirements pool from PRD markdown.
- `references/artifacts.md` - Templates for requirement pool, task graph, and logs.
- `references/ralph-loop-notes.md` - Ralph loop mechanics and task sizing guidance.
- `references/openclaw-gateway-notes.md` - Control-plane patterns for coordination.
