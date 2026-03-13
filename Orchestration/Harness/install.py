#!/usr/bin/env python3
"""
Installer for dotAi project layout.
Ensures Documents, Extensions, Orchestration, Project, and README exist in the target
directory; creates them if missing. Intended for use from xonsh or any Python-capable shell.

Usage: python install.py [TARGET_DIR]
  TARGET_DIR defaults to current directory (e.g. run from repo root).
"""

from pathlib import Path
import sys

LAYOUT = [
    (
        "Documents",
        "Documents/README.md",
        """# Documents

This directory is the placeholder for document index. Project and user documents (PRDs, requirements, user stories, reports, plans, references) live under **Project/Product/**.

See Project/README.md and SYSTEM_PROMPT.md for layout and conventions.
""",
    ),
    (
        "Extensions",
        "Extensions/README.md",
        """# Extensions

Technology compatibility and integration points (APIs, runtimes, platforms). One subfolder per technology; primary doc per folder in UPPERCASE.md.

See Project/README.md and Orchestration/Memories/system/ for config and model routing.
""",
    ),
    (
        "Orchestration",
        "Orchestration/README.md",
        """# Orchestration

Tasks, constraints, memories, harness, and agent config for the dotAi stack. Local model via llama-server; see orchestrator-compose.yml when present.

See Project/README.md for quick start and directory layout.
""",
    ),
    (
        "Project",
        "Project/README.md",
        """# Project

Project-level docs, rules, and guide. Read SYSTEM_PROMPT.md first (convention). Product docs, PRDs, and requirements live under Project/Product/.

See repo root README.md for user guide and quick start.
""",
    ),
]

ROOT_README = """# dotAi project

Required layout (created by Orchestration/Harness/install.py):

- **Documents/** — Document index; see Project/Product/ for PRDs, requirements, reports.
- **Extensions/** — Technology compatibility (APIs, runtimes).
- **Orchestration/** — Tasks, memories, harness, agent config.
- **Project/** — Project docs and guide; read Project/README.md and Project/SYSTEM_PROMPT.md.

Quick start: see Project/README.md. Local model: `docker compose -f Orchestration/orchestrator-compose.yml up -d llama-server` (when present).
"""


def main() -> int:
    raw = (sys.argv[1] if len(sys.argv) > 1 else ".").strip()
    try:
        target = Path(raw).resolve()
        if not target.is_dir() and target != Path.cwd().resolve():
            target = Path.cwd().resolve()
        else:
            target = target if target.is_dir() else target.parent
    except Exception:
        target = Path.cwd().resolve()
    if not target.exists():
        print(f"Invalid target: {raw}", file=sys.stderr)
        return 1

    print(f"Target directory: {target}")
    print("Checking required layout: Documents, Extensions, Orchestration, Project, README.md")
    print()

    created = 0

    for dir_name, readme_rel, readme_body in LAYOUT:
        dir_path = target / dir_name
        readme_path = target / readme_rel
        if not dir_path.is_dir():
            dir_path.mkdir(parents=True, exist_ok=True)
            print(f"  Created: {dir_name}/")
            created += 1
        if not readme_path.is_file():
            readme_path.parent.mkdir(parents=True, exist_ok=True)
            readme_path.write_text(readme_body.strip() + "\n", encoding="utf-8")
            print(f"  Created: {readme_rel}")
            created += 1

    root_readme = target / "README.md"
    if not root_readme.is_file():
        root_readme.write_text(ROOT_README.strip() + "\n", encoding="utf-8")
        print("  Created: README.md")
        created += 1

    print()
    if created > 0:
        print(f"Done. Created {created} item(s).")
    else:
        print("All required items already exist. Nothing created.")

    system_prompt = target / "Project" / "SYSTEM_PROMPT.md"
    if not system_prompt.is_file():
        print("Note: Project/SYSTEM_PROMPT.md not found. For full dotAi setup, clone the repo or copy system prompt and tasks from a full install.")

    return 0


if __name__ == "__main__":
    sys.exit(main())
