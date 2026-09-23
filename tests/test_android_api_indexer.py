import hashlib
import json
import struct
import tempfile
import unittest
import zipfile
from pathlib import Path

from tools.android_api_indexer.generate_api_index import build_index, class_access_flags


def minimal_class(access_flags: int) -> bytes:
    # Magic, Java 8 version, constant_pool_count=1, then class access flags.
    return b"\xca\xfe\xba\xbe" + struct.pack(">HHH", 0, 52, 1) + struct.pack(">H", access_flags)


class AndroidApiIndexerTests(unittest.TestCase):
    def test_reads_class_access_flags(self):
        self.assertEqual(0x0001, class_access_flags(minimal_class(0x0001)))
        with self.assertRaises(ValueError):
            class_access_flags(b"not a class")

    def test_indexes_only_public_android_classes(self):
        with tempfile.TemporaryDirectory() as directory:
            jar = Path(directory) / "android.jar"
            with zipfile.ZipFile(jar, "w") as archive:
                archive.writestr("android/os/Bundle.class", minimal_class(0x0001))
                archive.writestr("android/internal/Hidden.class", minimal_class(0x0000))
                archive.writestr("java/lang/String.class", minimal_class(0x0001))
            result = build_index(jar)
        self.assertEqual(["android.os.Bundle"], result["packages"]["android"])
        self.assertEqual(1, result["coverage"]["blocked"])
        self.assertEqual(64, len(result["source"]["sha256"]))


if __name__ == "__main__":
    unittest.main()
