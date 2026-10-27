# Veyra

Veyra is an experimental Android launcher for the Google Pixel 6 Pro, with a layered glass interface, fluid motion, app discovery, and gesture-first navigation.

**Stage:** early MVP. Veyra runs on top of Android; it is not a replacement operating system.

## First milestone

- Selectable Android Home app
- Compose home screen with live clock/date
- App library and installed-app launching
- Launcher-owned quick panel with Android Settings shortcuts
- Basic vertical gesture
- Repeatable GitHub Actions APK build

## Stack

Kotlin, Jetpack Compose, Material 3, Android SDK 35 (minimum API 26), Gradle, and GitHub Actions.

## APK delivery and updates

The GitHub Actions workflow builds a CI input artifact named **Veyra-CI-input**. It is signed with the runner's temporary debug key and is not the file to install for long-term in-place updates.

The installable file shared for device testing is named **Veyra.apk** and is re-signed with the stable Veyra development keystore stored outside the repository. Every delivered APK must keep:

- Application ID: `com.veyra.launcher`
- A signing certificate from the same persistent Veyra keystore
- A strictly increasing Android versionCode

With these preserved, Android can install Veyra.apk as an update over the prior delivered version. Do not delete the app before updating unless Android explicitly reports that the signatures differ. Never commit the signing keystore or its Base64 contents to this repository.

If the persistent keystore is lost, future builds cannot update an installed copy signed with it; a reinstall will be required. Keep the keystore backup private and secure.

## Downloading the CI build

1. Open **Actions → Android CI**.
2. Select the successful run.
3. Download **Veyra-CI-input** to inspect the raw CI build.
4. For device updates, use the separately delivered **Veyra.apk** signed with Veyra's stable development key.

## Development status

The current glass treatment uses translucent fills, layered gradients, and subtle borders; true background blur is not implemented yet. Third-party Android widgets, persistent customization, accessibility verification, and performance profiling remain future milestones.

See [ARCHITECTURE.md](ARCHITECTURE.md) and [ROADMAP.md](ROADMAP.md).
