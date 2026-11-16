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

**App ID:** `ca-app-pub-5172543149836113~9265123564`

The app shows:
- A **banner** at the bottom of Home, Play, and Game Over
- An **interstitial** when a game ends

### Banner rotation

Banners pick randomly from 10 ad units and recreate the `AdView` every **45 seconds** with a different unit (when possible):

- `ca-app-pub-5172543149836113/2088186170`
- `ca-app-pub-5172543149836113/2037195229`
- `ca-app-pub-5172543149836113/2172926590`
- `ca-app-pub-5172543149836113/5069572738`
- `ca-app-pub-5172543149836113/4942735172`
- `ca-app-pub-5172543149836113/5265013857`
- `ca-app-pub-5172543149836113/7312592690`
- `ca-app-pub-5172543149836113/1598211527`
- `ca-app-pub-5172543149836113/7297260761`
- `ca-app-pub-5172543149836113/1326748788`

Configured in `app/build.gradle.kts` as `ADMOB_BANNER_IDS` and used by `BannerAd`.

### Interstitial (separate from banners)

After each game end (lose or win), a full-screen interstitial is shown (if one is preloaded), then the Game Over screen opens. Units are chosen randomly from a pool that is **separate** from banner IDs (`ADMOB_INTERSTITIAL_IDS`):

- `ca-app-pub-5172543149836113/6986269571`
- `ca-app-pub-5172543149836113/2855452875`
- `ca-app-pub-5172543149836113/1269928525`
- `ca-app-pub-5172543149836113/4831054535`
- `ca-app-pub-5172543149836113/8294501878`
- `ca-app-pub-5172543149836113/1953434746`
- `ca-app-pub-5172543149836113/3588970109`
- `ca-app-pub-5172543149836113/3054938438`
- `ca-app-pub-5172543149836113/1509601673`
- `ca-app-pub-5172543149836113/2631111650`

The next interstitial is preloaded with a different random unit after each show/dismiss.

### app-ads.txt (required for AdMob apps)

Android uses **`app-ads.txt`**, not website `ads.txt`. This repo includes `app-ads.txt` with:

```
google.com, pub-5172543149836113, DIRECT, f08c47fec0942fa0
```

Host that file at the root of the website listed in your Play Console developer account, e.g. `https://yourdomain.com/app-ads.txt`, then set the same domain under AdMob → App settings → app-ads.txt.

### Troubleshooting (no ads)

- Ads wait for `MobileAds` init (`AdsInitializer`) before loading.
- Failed banner/interstitial loads retry other units from the same pool.
- Request config uses `MAX_AD_CONTENT_RATING_PG` (child-directed tagging removed so inventory can fill).
- Filter logcat by tag **`EasySnakeAds`** for load/show errors (`error.code` / message).
- AdMob may still return no-fill until the app is linked in AdMob, `app-ads.txt` is live on your developer website, and the app has been serving for a while.

## Package

- Application ID: `com.w3stech.easysnake`
- Min SDK 26 / Target SDK 36
- Version 1.1 (versionCode 2)
