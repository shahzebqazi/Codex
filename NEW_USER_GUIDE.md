# New user guide

A short guide to the ideas and terms you’ll see in this repo and in AI-assisted development: **AI**, **LLM**, **agents**, **scaffolds**, **guardrails**, **harnesses**, **MCP**, and how they fit together.

---

## AI (Artificial Intelligence)

**AI** is software that can do tasks we usually associate with intelligence: understanding language, reasoning, planning, or creating content. In this repo, “AI” usually means systems built around **large language models (LLMs)** that you or an **agent** talk to via text (prompts, chat, or files). The [.ai](README.md) system is a way to deploy that kind of AI into a project using markdown and (optionally) local models—no protocol servers or cloud APIs required.

---

## LLM (Large Language Model)

An **LLM** is a model trained on huge amounts of text so it can predict and generate language. You give it a **prompt** (instructions plus context); it returns text (answers, code, summaries). Examples: OpenAI’s GPT series, Anthropic’s Claude, open models (Llama, Mistral, etc.). Many can run **locally** (e.g. via GGUF) for privacy or offline use. In Codex, the full project and local GGUF setup live on the **`desktop-app`** (nightly) branch; the `main` branch is docs and process only.

---

## Agents

An **agent** is an AI system that doesn’t just answer one question—it can **take multiple steps**: read files, run tools, search, edit code, and loop until a goal is met. In practice, “agent” often means an LLM plus **tools** (e.g. run a command, read a file, call an API) and **instructions** (rules, skills, project context). Codex is built so that when you point an agent at your repo (e.g. at `Project/START_HERE.md` on the **`desktop-app`** nightly branch), it follows the same conventions: skills, rules, and the AI operation directory.

---

## Scaffolds

A **scaffold** is a reusable structure for a task: a prompt template, a workflow, or a small “skeleton” of steps (e.g. “intake → classify → summarize → commit”). Iterating on scaffolds means you refine prompts, components, and workflows over time and capture the good versions. On the **`desktop-app`** (nightly) branch you’ll find project structure and intake workflow for classifying scaffolds and related artifacts (components, prompts, workflows, patterns, templates).

---

## Guardrails

**Guardrails** are rules and checks that keep AI behavior within safe or acceptable bounds. They can be:

- **Input:** what topics or formats are allowed, content filters, prompt rules.
- **Output:** no harmful or off-topic content, format checks, fact-checking hooks.
- **Process:** “don’t push without approval,” “only use these tools,” “stay in this directory.”

In this project, [research policy](CONTRIBUTING.md#research-policy) and contribution rules (e.g. “get explicit approval before pushing research”) act as guardrails for how agents and humans should behave.

---

## Harnesses

A **harness** is a framework or environment that **wraps** and **controls** how agents (or models) run: same inputs, same tools, same evaluation. Harnesses are used for **testing**, **benchmarking**, and **evaluation**—e.g. running an agent on a fixed set of tasks and comparing outputs or metrics. In research (e.g. [agi-research](https://github.com/shahzebqazi/agi-research)), “harnessed” agent setups often refer to controlled experiments: context scaling, multi-modal inputs, or guardrails tested in a repeatable way.

---

## MCP (Model Context Protocol)

**MCP** is a protocol that lets an AI application (e.g. Cursor, Claude Desktop) talk to external **tools and data sources** (APIs, databases, file systems, custom servers). Instead of hard-coding every integration, the app discovers “MCP servers” and calls them so the model gets more context and can use more capabilities. **Cursor** and other editors can use MCP to give agents access to Stripe, Figma, browsers, etc. Cursor-specific plugins, tools, and MCP integrations for this maintainer live in the [Cursor config repo](CONTRIBUTING.md#cursor-config-repo), not in Codex.

---

## How this repo fits in

| Concept   | In Codex / .ai |
|----------|------------------|
| **AI**   | The .ai system: markdown-first orchestration, optional local inference. |
| **LLM**  | Used by agents; optional local GGUF on the **`desktop-app`** (nightly) branch. |
| **Agents** | Designed to be pointed at `Project/START_HERE.md` on **`desktop-app`** and to follow CONTRIBUTING and project rules. |
| **Scaffolds** | Intake workflow in [CONTRIBUTING](CONTRIBUTING.md#ai-intake-agent-workflow); full project on **`desktop-app`**. |
| **Guardrails** | [Research policy](CONTRIBUTING.md#research-policy), approval-before-push, and contribution conventions. |
| **Harnesses** | Research and benchmarking live in [agi-research](https://github.com/shahzebqazi/agi-research). |
| **MCP**  | Use the [Cursor config repo](CONTRIBUTING.md#cursor-config-repo) for Cursor-specific tools and MCP integrations. |

---

## Where to go next

- **[README](README.md)** — What Codex is, how to clone and use template/plugin mode.
- **[CONTRIBUTING](CONTRIBUTING.md)** — Branch model, AI intake workflow, how to contribute.
- **[Research policy](CONTRIBUTING.md#research-policy)** — Where research lives (agi-research) and approval before push.
- **[Cursor config repo](CONTRIBUTING.md#cursor-config-repo)** — Cursor-specific config, plugins, and MCP.
- **`desktop-app` (nightly) branch** — Full project, Orchestration, Documents, START_HERE; use this branch for the desktop app.
- **agi-research** — [github.com/shahzebqazi/agi-research](https://github.com/shahzebqazi/agi-research) for learning, research, and testing (LFM2, MoE, benchmarking, etc.).
