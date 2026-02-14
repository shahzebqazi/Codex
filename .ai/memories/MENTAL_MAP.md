# MENTAL_MAP -- Project Knowledge and RL Driver

This file is the central knowledge base for AI agents working on this project. Agents read it at session start and update it as they learn. It drives reinforcement learning by tracking what works and what doesn't.

## Project Code Style

```
language: [detected or configured]
paradigm: functional preferred
naming: snake_case for files, camelCase for JS/TS, snake_case for Python/Rust
comments: descriptive ("why" not "what"), avoid redundant comments
spacing: minimal empty lines
indentation: [project default]
line_length: [project default]
```

## LSP and Linters

```
lsp: [auto-detect from project]
linter: [auto-detect from project]
formatter: [auto-detect from project]
test_framework: [auto-detect from project]
build_tool: [auto-detect from project]
```

## Agent Performance Tracking

### Dense Reward History

Track intermediate signals per agent per task type:
```
compilation_rate: [% of edits that compile on first try]
lint_pass_rate: [% of edits that pass linting]
test_pass_rate: [% of test runs that pass]
context_efficiency: [useful tokens / total tokens]
```

### Model Performance by Task Type

| Task Type | Best Model | Success Rate | Avg Tokens |
|---|---|---|---|
| [populated by agents as they learn] | | | |

### Adaptive Compute Allocation

Based on Scaling Test-Time Compute findings:
- Simple tasks (rename, fix typo): use smallest available model, minimal verification
- Medium tasks (add function, fix bug): use default model, standard lint/test loop
- Hard tasks (architecture, subtle bugs): use largest model, search + verification loops

### Pivot Thresholds

From MRT (Meta Reinforcement Fine-Tuning):
- If no measurable progress after N actions, consider PIVOT
- Track cumulative regret per agent: if rising, reassign
- Default N: 5 actions without progress signal

## User Preferences

Extracted from dev branch memories:
```
response_style: concise
examples: only_when_requested
emoji: never
technical_level: advanced
planning: plan_before_code
development: incremental
testing: test_driven
config_format: json_over_scripts
vcs: active_git_and_jj
shell: zsh
ide: cursor
```

## Project-Specific Knowledge

```
[Agents populate this section as they learn about the project]
[Patterns, gotchas, conventions, integration points]
```

## Optimization History

```
[Agents record what approaches worked and what didn't]
[Format: date | agent | task | approach | outcome | learning]
```
