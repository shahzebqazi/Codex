# URLS -- Master Reference Index

All external links cataloged by the dotAi system. AI agents add new links here when discovered. Organized by category.

## 1. Autonomous Coding Agents

| Project | URL | Notes |
|---|---|---|
| SWE-agent (Princeton/Stanford) | https://github.com/swe-agent/swe-agent | YAML-as-agent-spec, ACI abstraction, trajectory logging |
| OpenAI Codex CLI | https://github.com/openai/codex | AGENTS.md convention, MCP integration, skills system |
| Claude Code (Anthropic) | https://github.com/anthropics/claude-code | CLAUDE.md scoped instructions, slash commands, plugins |
| Aider (AI pair programming) | https://github.com/Aider-AI/aider | Edit formats, repo map via tree-sitter, self-healing loop |
| OpenHands (formerly OpenDevin) | https://github.com/OpenDevin | SDK-first, AGENTS.md, sandboxed Docker containers |
| Qwen Code (terminal agent) | https://github.com/QwenLM/qwen-code | SubAgent decomposition, multi-protocol, headless mode |
| MetaGPT (multi-agent SWE) | https://github.com/geekan/MetaGPT | SOP-as-code, role-based decomposition (PM/Architect/Engineer) |
| OpenClaw (personal AI assistant) | https://github.com/openclaw/openclaw | Gateway control plane, markdown-first prompts, Docker sandboxes |
| OpenClaw docs | https://docs.openclaw.ai/ | Configuration, session model, multi-agent routing |
| Axon (K8s-native orchestration) | https://github.com/axon-core/axon | Declarative CRDs, TaskSpawner, AgentConfig, feedback loops |
| SWE-bench (benchmark suite) | https://github.com/swe-bench/SWE-bench | Evaluation framework for autonomous coding agents |
| Ralph (perpetual coding loop) | https://github.com/snarktank/ralph | PRD-driven loop, fresh context per iteration, progress.txt |

## 2. Multimodal Open-Source Models (GGUF)

| Model | URL | Notes |
|---|---|---|
| ggml-org multimodal collection | https://huggingface.co/collections/ggml-org/multimodal-ggufs-68244e01ff1f39e5bebeeedc | Master collection |
| Qwen 2.5 VL | (in collection above) | Vision-language |
| Pixtral 12B | https://huggingface.co/ggml-org/pixtral-12b-GGUF | Mistral multimodal |
| MiniCPM-V 4.5 | https://huggingface.co/openbmb/MiniCPM-V-4_5-gguf | 8B, competitive with GPT-4o |
| Gemma 3 (Google) | (in collection above) | 4B/12B/27B |
| InternVL 3 | (in collection above) | Vision-language |
| Mistral Small 3.1 24B | (in collection above) | Image-text |
| SmolVLM | (in collection above) | Small vision-language |
| Moondream2 | (in collection above) | 1B lightweight vision |
| LLaVA | (in collection above) | Original multimodal GGUF |
| Kimi-VL | (in collection above) | Vision-language |
| llama.cpp | https://github.com/ggml-org/llama.cpp | GGUF inference engine, llama-server, OpenAI-compatible API |

## 3. Technology Documentation

### Languages and Runtimes
| Technology | URL |
|---|---|
| Python docs | https://docs.python.org/3/ |
| Python language reference | https://docs.python.org/3/reference/ |
| Python standard library | https://docs.python.org/3/library/ |
| CPython source | https://github.com/python/cpython |
| Node.js API docs (v25.x) | https://nodejs.org/docs/latest/api/ |
| Node.js source | https://github.com/nodejs/node |

### Web Development
| Technology | URL |
|---|---|
| MDN Web Docs | https://developer.mozilla.org/ |
| HTML spec (WHATWG) | https://html.spec.whatwg.org/ |
| CSS spec (W3C) | https://www.w3.org/Style/CSS/ |
| Web.dev (Google) | https://web.dev/ |

### Architecture
| Technology | URL |
|---|---|
| Intel SDM | https://www.intel.com/content/www/us/en/developer/articles/technical/intel-sdm.html |
| AMD64 Programmer's Manual | https://developer.amd.com/resources/developer-guides-manuals/ |
| OSDev x86 wiki | https://wiki.osdev.org/X86 |
| ARM Developer docs | https://developer.arm.com/documentation |
| ARM Assembly guide | https://developer.arm.com/documentation/107829/latest/ |
| ARM Compiler User Guide | https://developer.arm.com/documentation/dui0473/latest/ |

### Networks
| Technology | URL |
|---|---|
| Beej's Guide to Network Programming | https://beej.us/guide/bgnet/ |
| IETF RFCs | https://www.rfc-editor.org/ |
| Wireshark docs | https://www.wireshark.org/docs/ |

### Unix and POSIX
| Technology | URL |
|---|---|
| POSIX spec (Open Group) | https://pubs.opengroup.org/onlinepubs/9699919799/ |
| FreeBSD Handbook | https://docs.freebsd.org/en/books/handbook/ |
| Linux man-pages | https://man7.org/linux/man-pages/ |

### Shells
| Shell | Docs | Source |
|---|---|---|
| Bash | https://www.gnu.org/software/bash/manual/bash.html | https://git.savannah.gnu.org/cgit/bash.git |
| Zsh | https://zsh.sourceforge.io/Doc/ | https://github.com/zsh-users/zsh |
| Fish | https://fishshell.com/docs/current/ | https://github.com/fish-shell/fish-shell |
| Xonsh | https://xon.sh/contents.html | https://github.com/xonsh/xonsh |
| CMD (Windows) | https://learn.microsoft.com/en-us/windows-server/administration/windows-commands/windows-commands | -- |
| PowerShell | https://learn.microsoft.com/en-us/powershell/ | https://github.com/PowerShell/PowerShell |

### Tools and Platforms
| Tool | URL |
|---|---|
| Chrome DevTools | https://developer.chrome.com/docs/devtools/ |
| Chromium source | https://chromium.googlesource.com/chromium/src |
| Chrome Extensions | https://developer.chrome.com/docs/extensions/ |
| Cursor docs | https://docs.cursor.com/ |
| Cursor website | https://www.cursor.com/ |
| Codex CLI GitHub | https://github.com/openai/codex |
| OpenAI platform docs | https://platform.openai.com/docs |
| Claude Code docs | https://docs.anthropic.com/en/docs/claude-code |
| Claude Code full docs | https://code.claude.com/docs |
| Signal protocol specs | https://signal.org/docs/ |
| Signal GitHub org | https://github.com/signalapp |
| Signal-Desktop | https://github.com/signalapp/Signal-Desktop |
| Signal-Server | https://github.com/signalapp/Signal-Server |
| libsignal (Rust) | https://github.com/signalapp/libsignal |
| Jujutsu (jj) | https://github.com/martinvonz/jj |

## 4. RL Optimization and Scaling Research

| Paper | URL | Key Finding |
|---|---|---|
| Argos: Multimodal RL with Agentic Verifier | https://arxiv.org/abs/2512.03438 | Ensemble of reward signals beats single pass/fail |
| VISTA-Gym: Scaling Agentic RL | https://arxiv.org/abs/2511.19773 | Standardized tool interfaces + trajectory logging |
| ScaleRL: Scaling RL Compute for LLMs | https://openreview.net/pdf/73a75bec6ecd647f8a93556e89bd4ed972c80ce1.pdf | RL compute scaling laws |
| LLM RL Post-training Scaling Laws | https://openreview.net/pdf?id=KBut2YCZ4g | Qwen 2.5 series scaling |
| DeepSeek-R1: Incentivizing Reasoning via RL | https://arxiv.org/abs/2501.12948 | Pure RL produces emergent reasoning without SFT |
| Predictive Scaling Laws for GRPO | https://arxiv.org/html/2507.18014v2 | Efficient GRPO training |
| GRPO Dynamics and Success Amplification | https://arxiv.org/html/2503.06639v4 | GRPO loss dynamics |
| Scaling LLM Test-Time Compute Optimally | https://arxiv.org/abs/2408.03314 | Smaller model + optimal compute beats 14x larger model |
| Meta RL Fine-Tuning (MRT) | https://arxiv.org/abs/2503.07572 | Dense progress rewards, 2-3x performance gain |
| BATS: Budget-Aware Tool-Use | https://arxiv.org/html/2511.17006v1 | Budget tracking unlocks continued scaling, CONTINUE/PIVOT/SUCCESS |
| Multimodal Reward Shaping for Web Agents | https://openreview.net/pdf?id=j0HKGS29cg | Cross-modal attention gating |
| Learning When to Plan (Test-Time Compute) | https://arxiv.org/html/2509.03581v2 | Adaptive planning for LLM agents |
| Embodied Agent Scaling Laws | https://arxiv.org/pdf/2411.04434 | Power laws in world modeling |
