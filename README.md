# Veyra

Veyra is an experimental Android launcher for the Google Pixel 6 Pro, built around a layered glass interface, fluid motion, app discovery, and gesture-first navigation.

**Stage:** bootstrap / MVP. Veyra is a launcher that runs on top of Android, not a replacement operating system.

## First milestone

- Home app registered with Android.
- Glass-inspired home screen with live clock/date.
- Discovery and launching of installed apps.
- Searchable app drawer.
- Launcher-owned quick panel with shortcuts to Android settings.
- Basic swipe gestures.
- Debug APK builds through GitHub Actions.

## Stack

- Kotlin, Jetpack Compose, Material 3
- Android SDK 35, minimum API 26
- Gradle and Android Gradle Plugin
- GitHub Actions for repeatable debug APK builds

## Build an APK

1. Open the **Actions** tab and select **Android CI**.
2. Run the workflow with **Run workflow**, or push a commit to main.
3. Open the successful run and download the **veyra-debug-apk** artifact.
4. Extract the artifact and install `app-debug.apk` on your Android phone.

The CI workflow installs a pinned Gradle version on its runner, so this starter does not depend on a checked-in Gradle wrapper JAR.

## Install

Install the debug APK and open Veyra. Choose it as the default Home app when Android asks, or change the default in Android Settings under Home app / default-app settings.

## Development status

This is an early foundation, not a polished release. Real third-party Android widgets, persistent customization, deeper system integration, accessibility checks, and performance profiling are later milestones.

See [ARCHITECTURE.md](ARCHITECTURE.md) and [ROADMAP.md](ROADMAP.md).
