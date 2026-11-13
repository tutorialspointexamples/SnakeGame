# EasySnake

A kid-friendly Snake game for Android, built with **Kotlin** and **Jetpack Compose**.

## Features

- Bright playground-style UI (portrait)
- On-screen D-pad **and** swipe controls
- Easy / Medium / Hard speeds (default Easy) with progressive speed-up in a run
- Special fruit power-ups (gold / slow / ghost star)
- Fill-the-board win celebration
- Sticker achievements + unlockable snake skins
- Haptics + score pulse polish
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

## Ads (AdMob)

The app shows:
- A **banner** on Home and Game Over
- An **interstitial** when a game ends

Debug builds use [Google's sample AdMob IDs](https://developers.google.com/admob/android/test-ads).

Before releasing, create an app + ad units at [AdMob](https://apps.admob.com) and put your IDs in `app/build.gradle.kts`:

1. `manifestPlaceholders["adMobAppId"]` — App ID (`ca-app-pub-xxxx~yyyy`)
2. `ADMOB_BANNER_ID` — Banner unit (`ca-app-pub-xxxx/zzzz`)
3. `ADMOB_INTERSTITIAL_ID` — Interstitial unit (`ca-app-pub-xxxx/zzzz`)

AdSense `ca-pub-…` / AMP `amp-ad` slots are for websites and will not work here.

Child-directed / G-rated request settings are enabled in `MainActivity` for this kids game.

## Package

- Application ID: `com.apnitechlab.easysnake`
- Min SDK 26 / Target SDK 34
