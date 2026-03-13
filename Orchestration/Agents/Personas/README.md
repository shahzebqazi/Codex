# Personas — Modular agents and personalities

**Location:** `Orchestration/Agents/Personas/`

This directory defines **personas** and how they combine with **skillsets** into **personalities** used by the harness. Personas are interchangeable; the project uses modular agents with persona + skillset = Personality.

---

## Glossary

| Term | Definition |
|------|-------------|
| **Persona** | A named behavioral profile: tone, style, and **model/sampling preferences** (temperature, top_p, top_k, streaming). Stored under `Personas/`; interchangeable. |
| **Skillset** | The set of tools and capabilities an agent can use (e.g. read_file, run_terminal, jj_commit). Can be enabled/disabled per agent or per persona. |
| **Personality** | A **persona** paired with a **skillset** (and optionally an agent implementation). The runnable “who + what” the user selects (e.g. “Precise Coder” persona + “SWE-agent” skillset). |

---

## Modular agents and interchangeable personas

- **Modular agents:** The harness runs agents that are composed from:
  - A **persona** (from `Personas/`) — defines temperature/sampling, streaming, and behavioral defaults.
  - A **skillset** — which functions/tools are enabled.
  - Optionally a specific agent implementation (e.g. Aider, SWE-agent, Ralph) that provides the tools.
- **Interchangeable personas:** Users can switch persona without changing skillset (e.g. same tools, different “Precise” vs “Creative” behavior), or change skillset for the same persona.

---

## Temperature & sampling (integrated into personas)

- **Per-persona:** Each persona specifies **temperature** (and where supported, **top_p**, **top_k**).
- **Presets:** Personas can map to presets (e.g. **Precise** = low temp, **Balanced** = mid, **Creative** = higher). Raw numbers can be overridden in persona config.
- **Reference:** FEATURES_PRD § Settings #13 (Temperature & sampling). Global settings can default from the selected persona.

---

## Streaming (integrated into personas)

- **Per-persona:** Each persona can specify whether **streaming** is preferred (on/off). Example: “Precise” might prefer off for tool-heavy flows; “Chat” persona might prefer on for responsive feel.
- **Fallback:** If the backend doesn’t support streaming, the harness uses non-streaming regardless of persona.
- **Reference:** FEATURES_PRD § Settings #14 (Streaming).

---

## Persona files (convention)

- One file or folder per persona under `Personas/` (e.g. `Precise.md`, `Creative.md`, or `precise/persona.json`).
- Each defines at least: **id**, **name**, **temperature** (and optional top_p/top_k), **streaming** (true/false), optional **system_prompt_snippet** or **behavior**.
- Skillsets are referenced by name; the harness resolves “Personality” = persona + skillset at runtime.

---

## References

- FEATURES_PRD.md § Settings #13 (Temperature & sampling), #14 (Streaming)
- FEATURES_PRD.md § Personality = persona + skillset
- Orchestration/Agents/Tools/ (AIDER, SWE_AGENT, RALPHY) for skillset/agent implementations
