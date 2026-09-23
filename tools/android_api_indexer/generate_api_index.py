#!/usr/bin/env python3
"""Generate a deterministic public-class inventory from an Android API JAR."""
from __future__ import annotations

import argparse
import hashlib
import json
import struct
import sys
import zipfile
from pathlib import Path

ACC_PUBLIC = 0x0001
ACC_PROTECTED = 0x0004
ACC_PRIVATE = 0x0002


def _u1(data: bytes, offset: int) -> tuple[int, int]:
    return data[offset], offset + 1


def _u2(data: bytes, offset: int) -> tuple[int, int]:
    return struct.unpack_from(">H", data, offset)[0], offset + 2


def class_access_flags(bytecode: bytes) -> int:
    """Return access flags from a valid JVM class file without dependencies."""
    if bytecode[:4] != b"\xca\xfe\xba\xbe":
        raise ValueError("not a JVM class file")
    offset = 8
    constant_pool_count, offset = _u2(bytecode, offset)
    index = 1
    while index < constant_pool_count:
        tag, offset = _u1(bytecode, offset)
        if tag == 1:  # Utf8
            length, offset = _u2(bytecode, offset)
            offset += length
        elif tag in (3, 4):  # Integer, Float
            offset += 4
        elif tag in (5, 6):  # Long, Double consume two slots
            offset += 8
            index += 1
        elif tag in (7, 8, 16, 19, 20):
            offset += 2
        elif tag in (9, 10, 11, 12, 17, 18):
            offset += 4
        elif tag == 15:
            offset += 3
        else:
            raise ValueError(f"unknown constant-pool tag {tag}")
        index += 1
    flags, _ = _u2(bytecode, offset)
    return flags


def public_android_classes(jar: Path) -> list[str]:
    classes: list[str] = []
    with zipfile.ZipFile(jar) as archive:
        for entry in sorted(archive.namelist()):
            if not entry.startswith("android/") or not entry.endswith(".class"):
                continue
            flags = class_access_flags(archive.read(entry))
            if flags & ACC_PUBLIC and not flags & ACC_PRIVATE:
                classes.append(entry.removesuffix(".class").replace("/", "."))
    return classes


def build_index(jar: Path) -> dict[str, object]:
    data = jar.read_bytes()
    classes = public_android_classes(jar)
    return {
        "schema_version": 1,
        "source": {"filename": jar.name, "sha256": hashlib.sha256(data).hexdigest()},
        "packages": {"android": classes},
        "coverage": {"implemented": 0, "adapted": 0, "emulated": 0, "blocked": len(classes)},
    }


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("android_jar", type=Path)
    parser.add_argument("output", type=Path)
    args = parser.parse_args()
    if not args.android_jar.is_file():
        parser.error(f"Android API JAR not found: {args.android_jar}")
    try:
        index = build_index(args.android_jar)
    except (ValueError, zipfile.BadZipFile) as error:
        parser.error(f"Invalid Android API JAR: {error}")
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(json.dumps(index, indent=2, sort_keys=True) + "\n", encoding="utf-8")
    print(f"indexed {len(index['packages']['android'])} public android.* classes -> {args.output}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
