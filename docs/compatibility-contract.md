# Contrat de compatibilité portableDroid

## Cible de produit

Une app Android Kotlin existante doit pouvoir être compilée **sans modifier ses fichiers source**, y compris ses imports `android.*`, vers Android, une PWA Kotlin/JS (Compose Web) et Linux (Compose Desktop, x86_64 et ARM64). Les Views XML et Jetpack Compose font partie du même contrat.

## Pourquoi un simple `typealias` ne suffit pas

Le framework Android possède déjà les noms `android.*`. Publier une autre classe portant le même nom sur le classpath Android crée une collision ; un `typealias android.os.Bundle = android.os.Bundle` serait circulaire. La compatibilité stricte exige donc un **front-end de compilation** qui choisit le fournisseur d'API par cible, pas une réécriture des imports et pas des faux stubs mélangés à `android.jar`.

## Architecture retenue

1. **Index API.** Pour chaque `android.jar` retenu, l'indexeur produit une liste versionnée des classes publiques et le SHA-256 du JAR. Cette liste est la mesure de couverture, non une liste saisie à la main.
2. **Front-end `portableDroid`.** Un plugin/compilateur Kotlin résout `android.*` vers le framework réel pour Android, et vers les déclarations portables pour JS et Native. Il conserve les sources, le manifeste, les layouts XML et la configuration de l'app tels quels.
3. **IR portable.** Le front-end transforme les appels Android en contrats portables, sans toucher aux fichiers de l'application. Les implémentations sont générées puis écrites par domaines (`content`, `app`, `view`, `widget`, `graphics`, `database`, etc.).
4. **Rendu.** `android.view`/`android.widget` est rendu par Compose Multiplatform Web (PWA) ou Compose Desktop (Linux), en conservant thèmes, mesures, événements et cycle de vie Android autant que le permet la cible.
5. **Services.** Les capacités sont adaptées aux Web APIs côté PWA, et à DBus/OS côté Linux. Lorsque leur sémantique Android est indispensable, un runtime local fournit une émulation avec communication inter-applications. Google Play et Play Services ne sont jamais simulés : leurs composants compatibles doivent être eux-mêmes construits et installés par portableDroid.
6. **Code natif.** JNI/NDK et bibliothèques C/C++ passent par une couche ABI explicite ; ils ne sont pas présumés fonctionner dans un navigateur.

## États possibles pour chaque API

| État | Signification |
| --- | --- |
| `implemented` | Même contrat observable, testé sur la cible. |
| `adapted` | Adaptateur Web/Linux, différences documentées et testées. |
| `emulated` | Runtime portable fourni et testé. |
| `blocked` | Pas encore acceptable pour une version stable. |

Une API ne peut pas être marquée stable lorsqu'elle est `blocked`. Les entrées d'index n'ayant pas de couverture constituent une dette de compatibilité visible.

## Décision importante

Aucun réécriveur d'imports n'est utilisé : remplacer `import android.*` par un autre package violerait le contrat. Les premiers livrables sont donc l'index reproductible, le contrôle de contrat, puis le front-end de résolution de symboles ; ils empêchent de confondre une couche de stubs avec une implémentation fonctionnelle.

## Tranche exécutable actuelle

Le module `:runtime` fournit désormais un exécutable Kotlin/JS avec des classes réellement placées dans `android.app`, `android.os`, `android.util`, `android.graphics`, `android.view` et `android.widget`. La fixture `MainActivity` n'importe que `android.*`, exécute le cycle `Activity.onCreate`, installe son contenu et le rendu s'effectue avec Compose Web. `assemblePwa` rassemble le bundle, le manifeste et le service worker.

Les targets `linuxX64` et `linuxArm64` existent également comme frontières de compilation pour le runtime. Un hôte Compose Desktop utilisable impose une cible JVM Linux (Compose Desktop n'est pas un backend Kotlin/Native) ; l'hôte native/DBus demeure donc un livrable distinct, et n'est pas annoncé comme fonctionnel tant qu'il ne produit pas une application exécutable testée.
