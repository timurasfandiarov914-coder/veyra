# Veyra architecture

Veyra is a regular Android app registered as a Home app. Android remains responsible for protected system UI, permissions, navigation, and other apps. Veyra owns its home surface, app drawer, launcher gestures, and launcher settings.

## MVP layers

- Activity: edge-to-edge window and Compose entry point.
- Home screen: live clock/date, glass-inspired surfaces, dock, and entry points to other launcher screens.
- App discovery: query launchable activities with PackageManager and launch selected apps.
- App drawer: app search and a scrollable grid.
- Quick panel: launcher-owned shortcuts that open Android settings. It does not replace the system notification shade or impersonate protected toggles.
- Design layer: deep navy palette, translucent fills, layered gradients, thin borders, consistent spacing, and subtle motion.
- CI: GitHub Actions assembles and uploads a debug APK.

## Compatibility and privacy

- Package: com.veyra.launcher
- Minimum API: 26 (Android 8.0).
- Compile / target SDK: 35.
- QUERY_ALL_PACKAGES supports the launcher's core function of showing installed apps. Google Play distribution would require reviewing current package-visibility policies.
- The MVP needs no network, analytics, account system, or background service. The app inventory stays on-device.

## Current limitations

- Glass visuals currently use translucent fills, gradients, and thin borders; true background blur is not implemented.
- Third-party Android widgets and resizing are out of scope for the first APK.
- A normal launcher cannot replace the lock screen or system shade without privileged/system-level capabilities.
- CI produces a debug APK, not a signed release build.
