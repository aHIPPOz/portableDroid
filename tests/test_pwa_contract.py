import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
APP = ROOT / "runtime/src/jsMain/kotlin/com/example/complex/MainActivity.kt"
RESOURCES = ROOT / "runtime/src/jsMain/resources"


class PwaContractTests(unittest.TestCase):
    def test_demo_keeps_android_imports_and_bootstraps_activity(self):
        source = APP.read_text(encoding="utf-8")
        for symbol in ("android.app.Activity", "android.os.Bundle", "android.util.Log", "android.widget.Button"):
            self.assertIn(f"import {symbol}", source)
        self.assertIn("MainActivity().onCreate(null)", source)
        self.assertNotIn("portable.android", source)

    def test_pwa_has_manifest_service_worker_and_runtime_entry(self):
        index = (RESOURCES / "index.html").read_text(encoding="utf-8")
        self.assertIn('rel="manifest"', index)
        self.assertIn("serviceWorker.register", index)
        self.assertIn("portable-droid.js", index)
        self.assertIn('"display": "standalone"', (RESOURCES / "manifest.webmanifest").read_text(encoding="utf-8"))


if __name__ == "__main__":
    unittest.main()
