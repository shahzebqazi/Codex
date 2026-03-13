#!/usr/bin/env python3
"""
Create a distributable tarball: run layout setup (install.py), then archive current dir.
Excludes .git, node_modules, .env, *.gguf, .DS_Store, *.tar.gz.
Intended for use from xonsh or any Python-capable shell.
"""

import os
import sys
import tarfile
from pathlib import Path
from datetime import date

EXCLUDES = {
    ".git",
    "node_modules",
    ".env",
    ".DS_Store",
}
EXCLUDE_SUFFIXES = (".gguf", ".tar.gz")
EXCLUDE_PREFIXES = (".git",)


def should_exclude(name: str, path: Path) -> bool:
    if path.name.startswith(".") and path.name not in (".env",):
        if path.name == ".git" or path.name.startswith((".git", ".DS_Store")):
            return True
    if path.name in EXCLUDES:
        return True
    if path.suffix in EXCLUDE_SUFFIXES or path.name.endswith(EXCLUDE_SUFFIXES):
        return True
    for p in path.parts:
        if p in EXCLUDES or p.startswith(EXCLUDE_PREFIXES):
            return True
    return False


def main() -> int:
    cwd = Path.cwd().resolve()
    name = cwd.name
    stamp = date.today().strftime("%Y%m%d")
    out_name = f"{name}-{stamp}.tar.gz"
    out_path = cwd / out_name

    print(f"Creating {out_name} ...")
    count = 0
    with tarfile.open(out_path, "w:gz") as tar:
        for root, dirs, files in os.walk(cwd, topdown=True):
            root_path = Path(root)
            dirs[:] = [d for d in dirs if not should_exclude(d, root_path / d)]
            for f in files:
                member_path = root_path / f
                if member_path == out_path or should_exclude(f, member_path):
                    continue
                try:
                    arcname = member_path.relative_to(cwd)
                except ValueError:
                    continue
                tar.add(member_path, arcname=arcname)
                count += 1
    print(f"Created {out_name} ({count} entries).")
    return 0


if __name__ == "__main__":
    sys.exit(main())
