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
Skills = command keywords (Orchestration/Skills/): user-typed triggers with deterministic effects (e.g. summarize, generate). Not capability units.
Tasks = families of actions (Orchestration/Tasks/): SWE, VCS, INFRA, DATA, TOOLS, PM, OS, etc. Eventually implemented as tool calls, scripts, or bundled programs; .md is interim.
[Agents populate this section as they learn about the project]
[Patterns, gotchas, conventions, integration points]
```

## Prolog knowledge base (facts)

Use a **Prolog knowledge base** to store and query relational facts (e.g. which files reference which, task dependencies, traceability). This keeps the mental map and task system consistent and queryable.

### Location and format

- **KB file:** `Orchestration/Memories/kb.pl` (or project-configured path under Memories). Create the file if missing.
- **Facts:** One predicate per relation. Prefer meaningful names: `references(FromFile, ToFile)`, `depends_on(Task, Task)`, `implements(Artifact, Requirement)`.
- **Rules:** Add rules in the same file (or an included file) for transitive closure and common queries, e.g. `refs_from_trans/2`, `refs_to_trans/2`, so agents can ask "what does X reference?" or "what references X?" (directly or transitively).

### When to update the KB

- After discovering or creating **file-to-file references** (imports, links, citations): add or update `references(From, To)` (or project convention).
- When **task/PRD/artifact relationships** are established: add facts for dependencies, implementation, and traceability.
- When **user or project preferences** are learned and should be queryable: add facts (e.g. `prefers(Scope, Key, Value)`) and keep MENTAL_MAP.md and DEFAULTS.md as the human-readable summary; the KB is the machine-queryable store.

### Usage

- **Query:** Use the KB to answer "what references X?", "what does X depend on?", "which artifacts implement requirement R?" without re-scanning the repo each time. Regenerate or incrementally update the KB when files or structure change.
- **Workspace structure (optional):** To avoid agents missing dirs in subdirs, the KB can also store directory whereabouts: e.g. `directory(Path)`, `child_dir(Parent, Child)`. See [WORKSPACE_STRUCTURE_INDEX.md](WORKSPACE_STRUCTURE_INDEX.md) for the full technique (file-based index or KB). When present, query the index/KB for "does dir X exist?" or "where is X?" before relying only on glob.
- **Tooling:** If Prolog is available (e.g. SWI-Prolog), agents can run queries via subprocess or a small script. Otherwise, keep the `.pl` file as the canonical fact store and document how downstream tools (or future harness features) will consume it.
- **Convention:** Document the predicate names and intended semantics in a short comment at the top of `kb.pl` or in this section so all agents use the same schema.

## Optimization History

```
[Agents record what approaches worked and what didn't]
[Format: date | agent | task | approach | outcome | learning]
```
