#!/usr/bin/env python3
"""
Generate a lightweight requirement pool JSON from a PRD markdown file.

This is a starter utility. It extracts sections (##) and bullet items (- )
into a simple JSON structure that can be refined by an agent.
"""

from __future__ import annotations

import argparse
import json
from dataclasses import asdict, dataclass
from pathlib import Path
from typing import Iterable, List, Sequence


@dataclass(frozen=True)
class Section:
    title: str
    items: Sequence[str]


@dataclass(frozen=True)
class RequirementPool:
    source: str
    sections: Sequence[Section]


def iter_lines(text: str) -> Iterable[str]:
    for line in text.splitlines():
        yield line.rstrip()


def parse_sections(lines: Iterable[str]) -> List[Section]:
    sections: List[Section] = []
    current_title = ""
    current_items: List[str] = []

    def flush() -> None:
        if current_title:
            sections.append(Section(title=current_title, items=tuple(current_items)))

    for line in lines:
        if line.startswith("## "):
            flush()
            current_title = line[3:].strip()
            current_items = []
            continue
        if line.startswith("- ") and current_title:
            current_items.append(line[2:].strip())

    flush()
    return sections


def build_requirement_pool(source: Path) -> RequirementPool:
    text = source.read_text(encoding="utf-8")
    sections = parse_sections(iter_lines(text))
    return RequirementPool(source=str(source), sections=tuple(sections))


def write_output(pool: RequirementPool, output: Path | None) -> None:
    payload = asdict(pool)
    if output:
        output.write_text(json.dumps(payload, indent=2) + "\n", encoding="utf-8")
    else:
        print(json.dumps(payload, indent=2))


def main() -> None:
    parser = argparse.ArgumentParser(description="Generate a requirement pool JSON.")
    parser.add_argument("prd", type=Path, help="Path to PRD markdown file")
    parser.add_argument("--output", type=Path, help="Write JSON to this path")
    args = parser.parse_args()

    pool = build_requirement_pool(args.prd)
    write_output(pool, args.output)


if __name__ == "__main__":
    main()
