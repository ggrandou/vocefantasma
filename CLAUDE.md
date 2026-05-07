# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Description du projet

**Voce Fantasma Di Milano** est une application Android qui parodie les apps de "détection de fantômes". Au lieu d'esprits, elle prononce aléatoirement des noms de pizzas via le Text-to-Speech Android. Deux modes :
- **Mode Manuel** : appui sur le bouton pour déclencher une phrase
- **Mode Auto** : écoute via micro, détecte les silences, répond après un délai aléatoire (interruptible si du bruit reprend)

## Commandes de build

```bash
# Build APK debug
./gradlew assembleDebug

# Installer sur un appareil connecté
./gradlew installDebug

# Build release
./gradlew assembleRelease

# Nettoyer
./gradlew clean
```

L'Android SDK doit être configuré dans `local.properties`. Le développement nécessite Android Studio ou un toolchain Android équivalent.

## Architecture

### MVVM avec StateFlow

```
MainActivity
    ↓
MainViewModel  (state machine, settings, collections)
    ↓
Compose UI (HomeScreen, CatalogueScreen, SettingsScreen, InfoScreen)
```

**MainViewModel** expose plusieurs `StateFlow` :
- `appMode` — MANUAL ou AUTO
- `autoState` — machine à états à 6 états (voir ci-dessous)
- `settings` — paramètres configurables (`AppSettings`)
- `activeCollection` / `collections` — phrases disponibles

### Machine à états du Mode Auto

`MainViewModel.runStateMachine()` gère 6 états séquentiels :

1. **IDLE** → attente de démarrage
2. **LISTENING** → surveille l'absence de son (attend `isSilent = false`)
3. **WAITING_FOR_SILENCE** → son détecté, attend le retour au silence (`isSilent = true`)
4. **DECISION** → tirage probabiliste selon `responseProbability` ; si échec, retour à LISTENING
5. **DELAYED_ACTION** → attente de 1–10 s (configurable) ; **interrompu immédiatement si son reprend**
6. **SPEAKING** → appel `speakRandomPhrase()`, bref délai, retour à LISTENING

### Couche audio

**SilenceDetector** (`com.vocefantasma.audio.SilenceDetector`) :
- `AudioRecord` : 44.1 kHz, mono, PCM-16 bit
- RMS en dB : `20 * log10(rms / 32767.0)`
- `isSilent: StateFlow<Boolean>` selon seuil + durée (défaut : −50 dB, 1500 ms)
- Exécuté sur le dispatcher IO

**TtsManager** (`com.vocefantasma.audio.TtsManager`) :
- Wrappeur sur `android.speech.tts.TextToSpeech`
- 5 langues : italien, français, anglais, allemand, espagnol
- Pitch et débit configurables ; initialisation asynchrone

### Modèles de données

**PhraseCollection** : regroupement de phrases par langue. Collections par défaut : pizzas italiennes (50) et françaises (53).

**AppSettings** : paramètres exposés dans SettingsScreen — `silenceThresholdDb`, `silenceDurationMs`, `responseProbability`, `minWaitSeconds`, `maxWaitSeconds`, `ttsPitch`, `ttsRate`, `ttsVoiceName`.

## Fichiers clés

```
src/main/kotlin/com/vocefantasma/
├── MainActivity.kt              # Point d'entrée, gestion des permissions, factory ViewModel
├── viewmodel/MainViewModel.kt   # Machine à états, settings, collections
├── audio/SilenceDetector.kt     # Détection de silence RMS
├── audio/TtsManager.kt          # Wrappeur TTS avec filtrage de voix
├── models/Models.kt             # PhraseCollection, AppSettings, Language, DefaultData
└── ui/
    ├── VoceFantasmaApp.kt       # Scaffold de navigation avec barre du bas
    ├── HomeScreen.kt            # Bouton principal, toggle mode, visualiseur dB
    ├── CatalogueScreen.kt       # Liste des collections (CRUD partiellement implémenté)
    ├── SettingsScreen.kt        # Sliders audio & comportement
    └── InfoScreen.kt            # Aide et description
```

## Points d'attention

- **Permissions** : `RECORD_AUDIO` demandé dans `MainActivity.onCreate()`. Le Mode Auto échoue silencieusement si refusé.
- **Pas de persistance** : settings et collections sont uniquement en mémoire (Room/DataStore prévu).
- **Sélection de voix** : code présent dans SettingsScreen mais désactivé dans l'UI.
- **Pas de tests automatisés** : aucun test suite n'existe actuellement.
- **DI minimal** : pas de Hilt, uniquement `MainViewModelFactory`.

## Recettes courantes

**Ajouter un paramètre** : `AppSettings` dans `Models.kt` → UI slider dans `SettingsScreen.kt` → usage dans ViewModel ou couche audio.

**Modifier le comportement du Mode Auto** : éditer les transitions dans `MainViewModel.runStateMachine()` et les valeurs par défaut de `AppSettings`.

**Ajouter une langue / collection** : `Language` enum dans `Models.kt` → `PhraseCollection` dans `DefaultData` → vérifier le support locale dans `TtsManager.speak()`.
