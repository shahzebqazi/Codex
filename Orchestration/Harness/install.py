#!/usr/bin/env python3
"""
Installer for dotAi project layout.
Ensures Orchestration and README exist in the target directory; creates them if missing.
Does not create Project/, Documents/, or Extensions/ (use repo root memories/, dependencies/, Documentation/).
Intended for use from xonsh or any Python-capable shell.

Usage: python install.py [TARGET_DIR]
  TARGET_DIR defaults to current directory (e.g. run from repo root).
"""

from pathlib import Path
import sys

LAYOUT = [
    (
        "Orchestration",
        "Orchestration/README.md",
        """# Orchestration

Tasks, harness, and agent config for the dotAi stack. Memory and rules live in repo root memories/. External tools in dependencies/. Local model via llama-server; see orchestrator-compose.yml when present.

See repo root README.md for quick start and directory layout. System prompt: Orchestration/Harness/SYSTEM_PROMPT.md.
""",
    ),
]

ROOT_README = """# dotAi project

Layout: memories/ (rules, config), dependencies/ (external tools), Documentation/ (PRDs, requirements), Orchestration/ (tasks, harness). System prompt: Orchestration/Harness/SYSTEM_PROMPT.md. Quick start: see README.md. Local model: `docker compose -f Orchestration/orchestrator-compose.yml up -d llama-server` (when present).
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

    system_prompt = target / "Orchestration" / "Harness" / "SYSTEM_PROMPT.md"
    if not system_prompt.is_file():
        print("Note: Orchestration/Harness/SYSTEM_PROMPT.md not found. For full dotAi setup, clone the repo or copy system prompt and tasks from a full install.")

    return 0


if __name__ == "__main__":
    sys.exit(main())
