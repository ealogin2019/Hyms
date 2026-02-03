# Repository Agent Instructions (Hyms)

## Purpose
Hyms is a fluid, minimal marketplace app. Every change must preserve the UX essence and progressive disclosure rules described below.

## Non‑negotiable UX/Product Rules
- Minimal UI, image-first layouts, calm typography, no visual clutter.
- One primary action per screen; secondary actions are visually quieter or hidden.
- Progressive disclosure only; avoid advanced or power-user controls by default.
- Fluid motion and safe navigation; no abrupt jumps or dead taps.
- Buyer and seller flows must stay distinct; do not mix intents on the same screen.

Authoritative checklist: `docs/hyms-essence.md`.

## Architecture & Modules
Project name: Hyms. Modular structure with core and feature separation.
- App: `app`
- Core: `core:common`, `core:ui`, `core:network`, `core:database`, `core:datastore`, `core:security`
- Features: `feature:auth`, `feature:feed`, `feature:chat`, `feature:profile`, `feature:checkout`
- Listing feature is granular: `feature:listing:ui`, `feature:listing:data`, `feature:listing:domain`

Keep feature boundaries clean and avoid leaking business logic into UI.

## Tech Stack
- Kotlin + Jetpack Compose
- Hilt for DI, KSP for annotation processing
- Android SDK: compile/target 34, min 26

## Build & Test
Use Gradle wrapper from repo root:
- Build: `./gradlew assembleDebug`
- Unit tests: `./gradlew test`
- Instrumentation tests: `./gradlew connectedAndroidTest`

## Change Guidance
- Prioritize UX essence over feature completeness.
- Avoid quick UI hacks; fix data flow if it causes UI clutter.
- Keep screens single-purpose and use minimal copy.
- If unsure, consult `README.md` for roadmap context.
