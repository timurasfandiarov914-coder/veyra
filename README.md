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

## One-time setup for update-compatible APKs

Android only installs an APK over an existing app when both builds use the same application ID and signing certificate. The CI workflow uses a persistent debug signing key stored as a GitHub Actions secret, not as a repository file.

1. Open **Settings → Secrets and variables → Actions** in this repository.
2. Select **New repository secret**.
3. Name: `VEYRA_DEBUG_KEYSTORE_BASE64`.
4. Value: paste the complete contents of the generated `veyra_debug_keystore_base64.txt` file supplied separately.
5. Save the secret, then open **Actions → Android CI** and rerun the latest workflow.

Do not commit the keystore or the Base64 text into the repository. The secret must remain the same for all future builds; replacing it changes the signing identity and prevents in-place updates.

## Downloading the APK

After setup, each successful workflow uploads an artifact named **Veyra-APK** containing a file named **Veyra.apk**. In this collaboration, the APK is also provided directly as a downloadable file. Future APKs will keep the same filename and signing identity, so Android can offer **Update** over the installed version.

1. Open **Actions → Android CI**.
2. Select the successful run.
3. Download the **Veyra-APK** artifact if you need the GitHub copy.
4. Install `Veyra.apk` on the device.

## Development status

The glass treatment currently uses translucent fills, layered gradients, and subtle borders; true background blur is not implemented yet. Third-party Android widgets, persistent customization, accessibility verification, and performance profiling remain future milestones.

See [ARCHITECTURE.md](ARCHITECTURE.md) and [ROADMAP.md](ROADMAP.md).
