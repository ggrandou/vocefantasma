# Voce Fantasma Di Milano

Voce Fantasma Di Milano (or simply **Voce Fantasma**) is a parody of "Spirit Voice" applications. Instead of detecting paranormal activity, it randomly pronounces phrases (initially pizza names) using Android's Text-to-Speech (TTS) engine.

## Project Overview

- **Purpose:** Satirical "Ghost Box" app that speaks pizza names.
- **Core Technology:** Android (Kotlin), Jetpack Compose, Material 3.
- **Key Features:**
    - **Manual Mode:** Trigger speech on demand.
    - **Auto Mode:** Listens for silence using the microphone and responds randomly after a delay.
    - **Interruption Logic:** Resets the countdown if sound is detected during the waiting phase.
    - **Catalogue:** Manage collections of phrases associated with specific languages (IT, FR).
    - **Advanced Settings:** Configure silence thresholds, random weights, and TTS parameters.

## Architecture

The project follows a standard MVVM architecture:

- **UI (`com.vocefantasma.ui`):** Built with Jetpack Compose.
    - `HomeScreen`: The main interface with the "Spirit Button".
    - `CatalogueScreen`: CRUD operations for phrase collections.
    - `SettingsScreen`: Configuration for audio and random logic.
- **ViewModel (`com.vocefantasma.viewmodel`):**
    - `MainViewModel`: Manages UI state and hosts the Auto Mode State Machine.
- **Audio Layer (`com.vocefantasma.audio`):**
    - `SilenceDetector`: Uses `AudioRecord` to monitor microphone input and detect silence based on RMS (dB).
    - `TtsManager`: Wrapper for `android.speech.tts.TextToSpeech`.
- **Data Models (`com.vocefantasma.models`):**
    - `PhraseCollection`: Defines a set of phrases and its language.
    - `AppSettings`: Persistent configuration for the app's behavior.

## Building and Running

This project is a standard Android application. Note that build configuration files (e.g., `build.gradle.kts`) are currently missing from the root and should be initialized for a complete environment.

### TODO: Standard Commands
- Build: `./gradlew assembleDebug`
- Run: `./gradlew installDebug`
- Test: `./gradlew test`

## Development Conventions

- **State Management:** Use `StateFlow` and `collectAsState` for reactive UI updates.
- **Audio Permissions:** `RECORD_AUDIO` is required for Auto Mode. Handle permissions gracefully in `MainActivity`.
- **Theming:** Dark paranormal aesthetic using Material 3 `darkColorScheme`.
- **State Machine:** The Auto Mode logic in `MainViewModel` must remain robust against rapid state transitions and interruptions.
- **Local Storage:** (Planned) Use Room or DataStore for custom collections and settings persistence.
