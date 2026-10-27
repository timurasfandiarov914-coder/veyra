# Roadmap

## M0 — Buildable foundation
- [x] Android project identity and Home intent filters
- [x] Compose-based glass-inspired home screen
- [x] Installed-app discovery and launch
- [x] Searchable app drawer
- [x] Launcher-owned quick panel with real Settings shortcuts
- [x] Basic vertical gestures
- [x] GitHub Actions debug APK workflow
- [ ] Verify first CI run
- [ ] Install and test on Pixel 6 Pro

## M1 — Daily-use launcher
- [ ] Persist user preferences with DataStore
- [ ] Customizable grid and dock
- [ ] Favorites and hidden-app handling
- [ ] Refresh app list when returning from external apps
- [ ] Accessibility and larger-text checks
- [ ] Unit tests for non-UI logic

## M2 — Widgets and personalization
- [ ] Integrate Android AppWidgetHost
- [ ] Place and resize supported home-screen widgets
- [ ] Wallpaper selection and adaptive colors
- [ ] Reduced-motion setting

## M3 — Polish
- [ ] Profile startup and frame timing
- [ ] Validate orientation and display-size changes
- [ ] Add release signing instructions
- [ ] Review package visibility and store distribution requirements

## Working rule

Ship and test one small slice at a time. A feature is not complete until it builds and is exercised on-device.
