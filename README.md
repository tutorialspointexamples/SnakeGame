# Kids Snake

A kid-friendly Snake game for Android, built with **Kotlin** and **Jetpack Compose**.

## Features

- Bright playground-style UI (portrait)
- On-screen D-pad **and** swipe controls
- Easy / Medium / Hard speeds (default Easy)
- Wrap-around walls (optional classic solid walls)
- Sound effects toggle
- Local high score (DataStore)
- Pause overlay and encouraging game-over messages

## Open in Android Studio

1. Install [Android Studio](https://developer.android.com/studio) (Hedgehog or newer recommended).
2. Open this folder: `SnakeGame`
3. Let Gradle sync download dependencies.
4. Connect a device or start an emulator (API 26+).
5. Click **Run**.

## Build from the command line

Requires the Android SDK (installed with Android Studio). Create `local.properties` with your SDK path (Android Studio does this automatically), then:

```bash
# Windows
gradlew.bat assembleDebug

# macOS / Linux
./gradlew assembleDebug
```

Debug APK output:

`app/build/outputs/apk/debug/app-debug.apk`

## Package

- Application ID: `com.snakegame.kids`
- Min SDK 26 / Target SDK 34
