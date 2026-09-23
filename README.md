# portableDroid

portableDroid est un projet de compatibilité qui permet à une application Android Kotlin de conserver ses imports `android.*` tout en ciblant Android, une PWA Kotlin/JS/Compose Web et une application Linux Kotlin/Native/Compose Desktop. Il ne s'agit pas d'un simple ensemble de déclarations : chaque API doit avoir une implémentation, un adaptateur de plateforme ou une émulation documentée avant une version stable.

## Contrat de compatibilité

* Les sources applicatives restent inchangées, notamment les imports `android.*`, les ressources XML, le manifeste et les configurations Gradle Android.
* La priorité est Kotlin/JS avec Compose Multiplatform Web et une PWA installable ; Linux x86_64 et ARM64 emploie Compose Desktop et des adaptateurs OS/DBus.
* Les API navigateur et Linux sont utilisées quand elles préservent la sémantique Android. Une émulation inter-applications est prévue lorsque c'est nécessaire.
* La stabilité n'est atteinte que lorsque l'inventaire de chaque API Android supportée par la version de référence est implémenté et testé sur toutes les cibles annoncées.

Les invariants et la stratégie de compilation sont détaillés dans [le contrat d'architecture](docs/compatibility-contract.md). L'inventaire reproductible d'un `android.jar` se génère avec [`tools/android_api_indexer/generate_api_index.py`](tools/android_api_indexer/generate_api_index.py).

## Démarrage de l'outillage

```bash
python3 tools/verify_workspace.py
python3 -m unittest discover -s tests -v
python3 tools/android_api_indexer/generate_api_index.py \
  "$ANDROID_HOME/platforms/android-<api>/android.jar" api/android-<api>.json
```

Le dernier programme n'invente aucune API : il lit le JAR fourni, émet une liste de classes publique et un manifeste dont le hachage identifie précisément l'entrée. Cette liste devient le backlog obligatoire de générateurs, implémentations et tests.

## Tranche PWA exécutable

Le module `runtime` est une première tranche réellement compilable, non une liste de stubs. Il contient une `Activity`, `Bundle`, `Log`, `Color`, `View`, `TextView` et `Button` dans les packages `android.*`; l'application de démonstration les importe directement et Compose Web les rend dans le navigateur. Le résultat PWA contient un manifeste et un service worker.

```bash
gradle :runtime:assemblePwa
# ouvrir runtime/build/pwa/index.html via un serveur HTTP local
```

Cette tranche ne revendique pas encore la couverture totale du framework Android. Les packages et comportements non implémentés restent explicitement hors de la condition de stabilité définie par le contrat.

La façade navigateur inclut aussi `android.content.Context`, `SharedPreferences`, `Intent` et `android.net.Uri`. Les préférences sont persistées dans `localStorage`; les intents `ACTION_VIEW` naviguent vers leur URI via le navigateur. Ces adaptateurs restent volontairement limités à des sémantiques réellement fournies par la plateforme Web.
