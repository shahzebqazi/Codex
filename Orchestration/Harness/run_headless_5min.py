#!/usr/bin/env python3
"""Run headless for 5 minutes. Acceptance test runs first; agent summarizes work at end."""
import subprocess
import sys
from pathlib import Path

script_dir = Path(__file__).resolve().parent
main_script = script_dir / "headless_run_10min.py"
sys.exit(subprocess.run([sys.executable, str(main_script), "--minutes", "5"] + sys.argv[1:], cwd=str(script_dir)).returncode)
