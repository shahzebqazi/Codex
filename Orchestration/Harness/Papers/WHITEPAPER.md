# WHITEPAPER -- dotAi: Declarative Markdown-Based Agent Orchestration

**Status:** DRAFT -- Outline and research notes for human authorship.

**Thesis:** Declarative markdown-based agent orchestration (via a `.ai/` directory and jj commits) is more efficient and composable than protocol-based systems like MCP.

---

## Abstract

_[~200 words. State the problem, proposed solution, and key claims.]_

**Problem framing notes:**
- Fragmented ecosystem: 10+ autonomous coding agent frameworks, each with proprietary protocols
- MCP attempts standardization but requires dedicated protocol servers
- Every framework has independently converged on file-based agent instructions (AGENTS.md, CLAUDE.md, SKILL.md)

**Solution framing notes:**
- dotAi makes the implicit pattern explicit: files ARE the protocol
- VCS commits (jj) ARE the communication bus
- No servers, no JSON-RPC, no runtime negotiation

---

## 1. Introduction

**Suggested structure:**
- Explosion of autonomous coding agents (2024-2026)
- The fragmentation problem
- MCP as partial solution and its limitations
- The convergence pattern: every system uses file-based instructions
- dotAi's proposition: take the convergence to its logical conclusion

**Key citations to weave in:**
- SWE-agent [1] -- first to show single-agent + ACI can resolve real GitHub issues
- MetaGPT [7] -- multi-agent SOP produces complete software from one-line requirement
- OpenHands [5] -- 67k+ stars, full-stack agent platform
- Axon [9] -- K8s-native declarative agent orchestration

---

## 2. Related Work

### 2.1 Single-Agent Loop Systems
| System | Key Pattern | Citation |
|---|---|---|
| SWE-agent | YAML-as-agent-spec, ACI, trajectory logging, mini-swe-agent (~100 lines) | [1] |
| Aider | Declarative edit formats, tree-sitter repo map, self-healing lint/test loop | [4] |
| Ralph | Fresh-context-per-iteration, PRD-driven, file-based memory (progress.txt, prd.json) | [11] |

### 2.2 Multi-Agent SOP Systems
| System | Key Pattern | Citation |
|---|---|---|
| MetaGPT | SOP-as-code, role decomposition (PM/Architect/Engineer), typed inter-agent artifacts | [7] |

### 2.3 Platform and SDK Systems
| System | Key Pattern | Citation |
|---|---|---|
| OpenHands | AGENTS.md convention, skills/ directory, SDK-first, Docker sandboxes | [5] |
| Axon | K8s CRDs (Task, Workspace, AgentConfig, TaskSpawner), event-driven lifecycle | [9] |

### 2.4 Gateway and Control-Plane Systems
| System | Key Pattern | Citation |
|---|---|---|
| OpenClaw | Single control plane, markdown-first prompts (AGENTS.md, SOUL.md, TOOLS.md), Docker per session | [8] |

### 2.5 Protocol-Based Systems (MCP)
| System | Key Pattern | Citation |
|---|---|---|
| Codex CLI | AGENTS.md in VCS, MCP integration, skills as extensions | [2] |
| Claude Code | CLAUDE.md scoped instructions, slash commands as markdown workflows, plugins | [3] |

**Argument to make:** All systems converge on file-based agent instructions. MCP adds a protocol layer on top. dotAi removes the protocol layer entirely.

---

## 3. The dotAi System

### 3.1 Core Principles
1. Everything is a prompt
2. Files are the protocol (no servers)
3. VCS commits are the communication bus
4. Declarative over imperative
5. Local-first (GGUF via llama-server)

### 3.2 Architecture
_[Describe the `.ai/` directory structure. Diagram the agent lifecycle: START_HERE.md -> skill loading -> task execution -> jj commit -> memory update.]_

### 3.3 Jujutsu as Communication Layer
**Key properties to argue:**
- Working-copy-as-commit eliminates "forgot to save" failure mode
- Conflicts-as-data enables safe parallel merging (no blocking)
- Automatic rebase propagates shared changes to all agent branches
- Operation log provides complete audit trail
- Revsets enable programmatic orchestration queries

### 3.4 The XP+ Methodology
**The Carl Jung insight:**
> "By having an infinitely powerful machine coding conscious, an unconscious programmer can fully intuit their problem solving. By having an ultimately helpful and effective machine unconscious, a conscious programmer is a one man army."

_[Develop this into a formal methodology section. Two modes: AI-codes/human-observes, AI-assists/human-codes. Stakeholder = developer. Team = human + AI agents.]_

---

## 4. Theoretical Advantages

### 4.1 Composability
**Argument:** Markdown composes via concatenation. Protocol systems require adapters and serialization.

### 4.2 Auditability
**Argument:** Every agent action = VCS commit. No separate logging infrastructure needed.

### 4.3 Parallelism
**Argument:** jj's conflict-as-data vs. protocol systems' explicit locking/ordering.

### 4.4 Efficiency
**RL research to cite:**

| Paper | Key Finding | How it supports dotAi |
|---|---|---|
| Scaling Test-Time Compute [15] | Smaller model + optimal compute beats 14x larger model | Local GGUF models can be competitive |
| BATS [16] | Budget-aware agents achieve same accuracy at 10x less cost | CONTINUE/PIVOT/SUCCESS protocol |
| DeepSeek-R1 [17] | Pure RL on verifiable tasks produces emergent reasoning | Code is uniquely suited (tests = verifiable rewards) |
| Argos [18] | Ensemble of reward signals beats single pass/fail | Dense intermediate verification (compile, lint, typecheck) |
| MRT [19] | Dense progress rewards give 2-3x performance, 1.5x token efficiency | Token efficiency critical for local deployment |
| VISTA-Gym [20] | Standardized tool interfaces + trajectory logging | Unified skill files + jj commit logs |

---

## 5. Comparison with MCP

| Dimension | MCP | dotAi |
|---|---|---|
| Communication | JSON-RPC protocol servers | VCS commits (jj) |
| Configuration | Runtime config, env vars | Markdown files in `.ai/` |
| Tool exposure | Server-hosted tool endpoints | Skill files on disk |
| Discovery | Protocol negotiation at runtime | File system scan |
| Composability | Adapter code per tool server | File concatenation |
| Auditability | Requires separate logging | Built into VCS history |
| Offline support | Requires running servers | Files always available |
| Parallelism | Explicit concurrency control | jj conflict-as-data |
| Infrastructure | Protocol server per tool set | Single directory + VCS |
| Learning curve | Protocol spec + SDK | Markdown + VCS basics |

**Key tradeoff to acknowledge:** MCP supports real-time dynamic tool discovery. dotAi requires tools declared as files before use. For autonomous coding (stable tool sets), this favors dotAi.

---

## 6. Verification and Proof of Concept

_[Describe the MVP implementation. What it demonstrates. How to reproduce.]_

**MVP facts:**
- 40+ files in `.ai/` directory
- Single START_HERE.md bootstraps a chatbot agent
- Local GGUF model (Kimi K2.5) via docker-compose + llama-server
- Cursor plan.md-compatible PRDs
- jj for agent VCS with group-chat commit messages
- Self-update from base repo without losing local modifications

---

## 7. Limitations and Future Work

- Single-agent MVP; multi-agent orchestration designed but not implemented
- Heuristic RL in MENTAL_MAP.md; formal RL training requires compute
- jj is young; Git-only fallback needed for broader adoption
- No formal SWE-bench evaluation yet
- Future: nvim, emacs, Codex, Claude Code as agent runtimes

---

## 8. Conclusion

_[2-3 paragraphs. Restate thesis with evidence. Note the convergence trend. Call to action.]_

---

## References

[1] Yang, J., et al. (2024). SWE-agent: Agent-Computer Interfaces Enable Automated Software Engineering. *NeurIPS 2024*. https://github.com/swe-agent/swe-agent

[2] OpenAI. (2025). Codex CLI. https://github.com/openai/codex

[3] Anthropic. (2025). Claude Code. https://github.com/anthropics/claude-code

[4] Gauthier, P. (2024). Aider: AI Pair Programming in Your Terminal. https://github.com/Aider-AI/aider

[5] Wang, X., et al. (2024). OpenHands: An Open Platform for AI Software Developers as Generalist Agents. https://github.com/OpenDevin

[6] Alibaba Cloud. (2025). Qwen Code: Terminal AI Agent. https://github.com/QwenLM/qwen-code

[7] Hong, S., et al. (2023). MetaGPT: Meta Programming for Multi-Agent Collaborative Framework. *ICLR 2024*. https://github.com/geekan/MetaGPT

[8] OpenClaw. (2025). OpenClaw: Open-Source Personal AI Assistant. https://github.com/openclaw/openclaw

[9] Axon. (2025). Axon: Kubernetes-Native Agent Orchestration. https://github.com/axon-core/axon

[10] Jimenez, C. E., et al. (2024). SWE-bench: Can Language Models Resolve Real-World GitHub Issues? *ICLR 2024*. https://github.com/swe-bench/SWE-bench

[11] snarktank. (2025). Ralph: Perpetual AI Coding Agent Loop. https://github.com/snarktank/ralph

[12] _[Reserved]_

[13] Martinvonz. (2025). Jujutsu: A Git-Compatible VCS. https://github.com/martinvonz/jj

[14] ggml-org. (2025). llama.cpp: LLM Inference in C/C++. https://github.com/ggml-org/llama.cpp

[15] Snell, C., et al. (2024). Scaling LLM Test-Time Compute Optimally. *arXiv:2408.03314*.

[16] Liu, Z., et al. (2025). BATS: Budget-Aware Tool-Use Enables Effective Agent Scaling. *arXiv:2511.17006*.

[17] DeepSeek-AI. (2025). DeepSeek-R1: Incentivizing Reasoning via RL. *Nature*. *arXiv:2501.12948*.

[18] Tan, H., et al. (2024). Argos: Multimodal RL with Agentic Verifier. *arXiv:2512.03438*.

[19] Qu, Y., et al. (2025). Meta Reinforcement Fine-Tuning (MRT). *arXiv:2503.07572*.

[20] Lu, Y., et al. (2025). VISTA-Gym: Scaling Agentic RL for Tool-Integrated Reasoning. *arXiv:2511.19773*.

[21] ggml-org. (2025). Multimodal GGUFs Collection. https://huggingface.co/collections/ggml-org/multimodal-ggufs-68244e01ff1f39e5bebeeedc
