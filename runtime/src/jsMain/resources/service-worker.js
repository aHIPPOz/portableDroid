const CACHE = "portable-droid-v1";
const ASSETS = ["./", "index.html", "styles.css", "manifest.webmanifest", "portable-droid.js"];
self.addEventListener("install", event => event.waitUntil(caches.open(CACHE).then(cache => cache.addAll(ASSETS))));
self.addEventListener("fetch", event => event.respondWith(caches.match(event.request).then(hit => hit || fetch(event.request))));
