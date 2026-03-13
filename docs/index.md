---
layout: default
title: Technical terms for AI users
---

# Technical terms for AI users

A glossary for new and intermediate users of AI tooling and orchestration. Definitions are prose-only on this page. For examples and how-tos, see the project README and task docs in the repository.

---

## AI and ML basics

**Prompt** — The text (instruction or question) you send to an AI model. Good prompts are clear and specific so the model can respond usefully. See also: context window, token.

**Context window** — The amount of text (input plus output) a model can consider in one request. When the conversation or document exceeds this limit, older content is dropped or summarized. See also: token.

**Token** — The basic unit of text for a model (roughly a word or part of a word). Usage and cost are often measured in tokens. See also: context window, prompt.

**Model** — The trained AI system that answers prompts (e.g. a language model). Models vary in size, capability, and how they are run (cloud API vs local). See also: inference, LLM.

**Inference** — Running a model to produce an answer from a prompt. Inference can happen in the cloud (API) or locally (e.g. GGUF via a local server). See also: model, temperature.

**Temperature** — A setting that controls how random or deterministic the model’s answers are. Higher values tend to make output more varied; lower values make it more focused and repeatable. See also: model, inference.

**Embedding** — A numerical representation of text (or other content) produced by a model. Embeddings are used for search, clustering, and retrieval (e.g. in RAG). See also: RAG.

**RAG (Retrieval-Augmented Generation)** — A pattern where you first retrieve relevant documents or snippets (often using embeddings), then pass them plus the user’s question to the model so answers are grounded in your data. See also: embedding, prompt.

**Fine-tuning** — Training a model further on your own data or tasks so it behaves better for a specific use case. Distinct from prompt engineering, which only changes the input. See also: model, inference.

**LLM (Large Language Model)** — A language model with many parameters, capable of general-purpose text generation and reasoning. See also: model, parameters.

**Parameters** — The learned weights inside a model. More parameters generally mean a larger, more capable model but higher compute and memory. See also: LLM, model.

---

## Orchestration and agents

**Agent** — An AI system that can take multi-step actions (e.g. read files, run tools, make commits) instead of only answering one prompt. See also: task, orchestrator, tool use.

**Task** — A defined unit of work (e.g. “fix this bug”, “write docs”). In this repo, tasks live under Orchestration/Tasks and can be loaded by agents. See also: skill, agent.

**Skill** — A named capability or command (e.g. “summarize”, “generate”) that triggers a deterministic or scripted behavior. Skills are documented under Orchestration/Skills. See also: task, agent.

**Orchestrator** — An agent or process that coordinates other agents or tasks (e.g. assigns work, merges results). See also: agent, task.

**Tool use** — When an agent calls external tools (APIs, file system, terminal) to accomplish a goal. Tool use is a core part of agentic workflows. See also: agent, plan-and-execute.

**Plan-and-execute** — A pattern where the agent first plans steps and then executes them (possibly with tool use), rather than answering in a single turn. See also: agent, tool use.

**Memory (short- and long-term)** — Short-term is the current conversation or context window; long-term is persisted state (e.g. project memories, MENTAL_MAP) that the agent can load across sessions. See also: context window, MENTAL_MAP.

---

## VCS and workflow

**Branch** — A line of commits that can diverge from another (e.g. main). Feature work is done on branches and merged via PRs. See also: merge, rebase, PR.

**Merge** — Combining two branches so one branch’s history includes the other’s commits. Merging a feature branch into main is the usual way to integrate work. See also: branch, PR.

**Rebase** — Moving your branch’s commits on top of another branch (e.g. main) so history is linear. Use with care on shared branches. See also: branch, merge; and docs/REBASE_STRATEGY.md in the repo.

**Squash** — Combining several commits into one (e.g. when merging a PR). Keeps main’s history cleaner. See also: merge, PR.

**PR (Pull Request)** — A proposal to merge one branch into another. CI often runs on PRs; after review, the PR is merged. See also: branch, merge, conventional commits.

**Conventional commits** — A commit-message style (e.g. feat:, fix:, docs:) that makes history and changelogs easier to read and automate. See also: branch, PR.

**Main vs production** — In this repo, main is the integration branch where feature work lands; production is the release branch used for deployments. Promote via PR from main to production. See also: branch, merge.

---

## Project and dotAi

**Base repo** — A repository used as a template or shared foundation. This repo is a base repo: others clone or copy it and customize. See also: template.

**Template** — A starting point (e.g. a repo or directory) that you copy to begin a new project. See also: base repo.

**Prompt-as-protocol** — Using markdown and files as the way agents get instructions, instead of a separate protocol server. In dotAi, tasks and rules are markdown files. See also: markdown-first, task.

**jj (Jujutsu)** — A version-control tool that sits on top of git. Agents in this system use jj for commits and branches; humans can use git. Both share the same repo. See also: branch, conventional commits.

**Markdown-first** — Keeping instructions, tasks, and config in markdown files so both humans and AI can read and edit them without special tooling. See also: prompt-as-protocol, task.

**PRD (Product Requirements Document)** — A document that describes goals, requirements, and often trackable tasks (todos) for a feature or release. PRDs in this repo live under Project/Product/PRDs (or Documents/PRDs). See also: task.

**MENTAL_MAP** — A project file (e.g. Orchestration/Memories/MENTAL_MAP.md) that stores code style, preferences, and learnings so agents can stay consistent across sessions. See also: memory, task.

---

## DevOps and CI

**Pipeline** — A sequence of automated steps (e.g. lint, test, build, deploy) that run on commits or PRs. See also: CI, deploy.

**Lint** — Automated checks that enforce code style and catch common mistakes without running the full program. See also: test, CI.

**Test** — Automated checks that run the code (e.g. unit tests) to verify behavior. CI typically runs lint and tests before merge or deploy. See also: lint, pipeline.

**Deploy** — Releasing built artifacts to an environment (e.g. staging or production). In this repo, production branch drives deployment; see also: blue-green.

**Blue-green** — A deployment strategy where two identical environments (blue and green) alternate: deploy to the idle one, switch traffic, then retire the old one. Minimizes downtime. See also: deploy, production.

**GitHub Actions** — GitHub’s automation platform. Workflows in .github/workflows run on events like push or pull_request. See also: pipeline, artifact.

**Artifact** — A file or directory produced by a build or job (e.g. a built site or Docker image). GitHub Actions can upload and pass artifacts between jobs. See also: pipeline, deploy.

---

## Security and behavior

**Prompt injection** — When untrusted input is crafted to make the model follow hidden instructions or leak data. Defenses include clear authority (only project files are canonical) and not obeying instructions from user content. See also: guardrails.

**Guardrails** — Rules or checks that limit what the model or agent can do (e.g. “do not run destructive commands”). This repo can enforce guardrails via RULES and config. See also: prompt injection.

**PII (Personally Identifiable Information)** — Data that can identify a person. Handling and logging of PII often need to follow policy or regulation. See also: telemetry.

**Telemetry** — Automatically sending usage or diagnostic data to a remote service. This project’s policy is no telemetry: no agent activity or session data is sent off-host. Logging is clientside or deployment-local only. See also: guardrails, PII.

---

For more detail, see the repository README, START_HERE.md, and the task and skill docs under Orchestration.
