#!/usr/bin/env python3
"""Enforce the non-rewrite invariant and basic repository layout."""
from __future__ import annotations

import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SAMPLE = ROOT / "samples/complex-android-app/app/src/main/kotlin/com/example/complex/MainActivity.kt"
REQUIRED = (ROOT / "docs/compatibility-contract.md", ROOT / "tools/android_api_indexer/generate_api_index.py")


def main() -> int:
    missing = [str(path.relative_to(ROOT)) for path in REQUIRED if not path.is_file()]
    source = SAMPLE.read_text(encoding="utf-8") if SAMPLE.is_file() else ""
    required_imports = ("import android.app.Activity", "import android.os.Bundle", "import android.util.Log")
    if missing:
        print("missing required files: " + ", ".join(missing), file=sys.stderr)
        return 1
    if not all(item in source for item in required_imports):
        print("sample no longer proves unchanged android.* imports", file=sys.stderr)
        return 1
    if "portable.android" in source:
        print("sample uses forbidden rewritten import", file=sys.stderr)
        return 1
    print("workspace contract verified: unchanged android.* source imports")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
