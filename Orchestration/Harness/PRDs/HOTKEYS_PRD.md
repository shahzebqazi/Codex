---
name: "Hotkeys PRD — Keyboard shortcuts and default mappings"
overview: "Determine all keyboard shortcuts for the AI coding harness (Electron + Ollama + LFM2), default mappings, and rules for customization. Feeds into Settings menu feature #20."
todos:
  - id: hotkeys-inventory
    content: "List every harness action that should have a shortcut (chat, settings, sidebar, send, new chat, etc.)"
    status: pending
  - id: hotkeys-defaults-mac
    content: "Define default key mappings for macOS (including Cmd vs Ctrl, Option, system conflicts)"
    status: pending
  - id: hotkeys-defaults-win-linux
    content: "Define default key mappings for Windows and Linux (Ctrl, Alt, Super)"
    status: pending
  - id: hotkeys-doc
    content: "Document final hotkeys in PRD and in-app shortcut view"
    status: pending
  - id: hotkeys-settings
    content: "Wire Settings menu to view/customize hotkeys (FEATURES_PRD #20)"
    status: pending
isProject: false
---

# Hotkeys PRD — Keyboard shortcuts and default mappings

**Purpose:** Single source of truth for which actions have keyboard shortcuts in the AI coding harness, and what their default mappings are. Used to implement Settings menu feature #20 (keyboard shortcuts).

**Project:** Desktop chat app (Electron). See FEATURES_PRD.md for overall settings.

---

## Scope

- **In scope:** All shortcuts that trigger harness UI or agent actions (e.g. send message, new chat, open settings, toggle sidebar, focus input, stop generation).
- **Out of scope for this PRD:** Shortcuts that are entirely inside the system prompt or agent logic (e.g. “run plan”) unless they are exposed as a UI action.

---

## Action categories (candidate list)

*To be refined in todos: hotkeys-inventory, hotkeys-defaults-*.*

| Category        | Example actions |
|----------------|-----------------|
| Chat           | Send message, New chat, Focus input, Stop generation, Clear/Reset chat |
| Navigation     | Toggle sidebar, Open settings, Switch model, Next/previous chat |
| Editor / view  | Toggle streaming, Copy response, Expand/collapse panel |
| General       | Toggle fullscreen, Reload, DevTools (dev only) |

---

## Platform conventions

- **macOS:** Prefer **Cmd** for primary (e.g. Cmd+Enter send, Cmd+N new chat). Use **Option** for variants. Avoid Cmd where it conflicts with system (e.g. Cmd+W close).
- **Windows / Linux:** Prefer **Ctrl** for primary. **Alt** for variants. **Super/Windows** only if needed and non-conflicting.
- Document any conflict with OS or Electron defaults and choose an alternative.

---

## Deliverables

1. **Inventory** — Full list of actions and proposed default keys (per platform).
2. **Default mappings table** — Final table in this PRD (macOS, Windows, Linux).
3. **In-app** — Settings → Keyboard shortcuts: view and, when implemented, customize and export/import keymap.

---

## References

- FEATURES_PRD.md § Settings Menu #20 (keyboard shortcuts)
- Electron: accelerator strings, menu role shortcuts, global vs local
