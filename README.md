# CACAD'GAME

## Contexte

Application mobile iOS/Android destiné à un publique externe sur l'apprentissage de différents domaines de compétences que CACD2 maitrise.

- [Confluence](https://cacd2.atlassian.net/wiki/spaces/CACDGAME/overview?homepageId=1742110997)



## Organisation des projets

TBD

## Configuration

1. Outils

- `npm install` à la base du repo avec Node >= 12
- `git config core.hooksPath .githooks`

2. App 

- https://kotlinlang.org/docs/multiplatform-mobile-setup.html


## Stack technique

### Commun

- [Kotlin multi platform](https://kotlinlang.org/docs/multiplatform.html)
    - Une base de code commune en kotlin pour iOS et Android
- [Firebase](https://console.firebase.google.com/project/cacdgame/overview)
    - Crashlytics, Analytics, ....
- [Base de données](https://github.com/cashapp/sqldelight)
- [API GraphQL avec apollo](https://www.apollographql.com)
    - Avec DatoCMS
- [API Rest avec ktor](https://ktor.io/docs/getting-started-ktor-client-multiplatform-mobile.html#ktor-dependencies)

Ce projet est dans un **mono repo**, il contient les applications iOS et Android, un serveur et un projet commun entre les apps et le serveur.

Cette approche est adapté au contexte technique car le serveur étant en Kotlin et que les apps sont en partie en Kotlin, il est possible de mettre en commun du code et ainsi avec des applications plus fiables.


### IOS
- [SwiftUI](https://developer.apple.com/xcode/swiftui/)


### Android
- [Compose](https://developer.android.com/jetpack/compose?hl=fr)

### Outils

- [Commitizen](https://github.com/commitizen/cz-cli)


### gestion des versions des dépendances

La gestion des versions se fait avec le systeme TOML de gradle qui permet de partager et centralisé la gestion des deps et leurs versions.

voir le fichier `libs.versions.toml`








