# Hyms - AI Coding Agent Instructions

## Project Overview
**Hyms** is a fluid, minimal marketplace Android app following a strict design philosophy: "Do one thing at a time — extremely well." This is a UK-focused resale marketplace (GBP, UK sizing) built with Jetpack Compose and a multi-module clean architecture. Currently in Phase 1 development (core marketplace loop).

Key principle from [`docs/hyms-essence.md`](../docs/hyms-essence.md): **Never ship features that violate fluidity, minimalism, or create UI clutter. One primary action per screen. Progressive disclosure.**

## Architecture & Module Structure

### Multi-Module Clean Architecture
The project follows **strict layer separation** with granular modularization (see [`settings.gradle.kts`](../settings.gradle.kts)):

- **`app/`** - Application module, navigation, DI wiring, MainActivity
- **`core/`** - Shared infrastructure modules
  - `core:common` - Shared utilities
  - `core:ui` - Design system (Theme, Color, Spacing, Radius, Typography, reusable components)
  - `core:database` - Room database (`HymsDatabase`) with DAOs and entities
  - `core:datastore` - Preferences storage
  - `core:network` - Retrofit/OkHttp setup
  - `core:security` - Encryption utilities
- **`feature/`** - Feature modules (each can be data/domain/ui split)
  - `feature:feed` - Home feed, listing detail, recently viewed
  - `feature:listing:ui`, `feature:listing:data`, `feature:listing:domain` - Seller listing flow (3-layer split)
  - `feature:auth`, `feature:chat`, `feature:profile`, `feature:checkout` - Placeholder modules

**Pattern:** Large features like `listing` use 3-layer separation (ui/data/domain). Smaller features like `feed` use single-module structure until complexity warrants splitting.

### Dependency Flow
- Features depend on `core` modules, never on other features
- UI layer depends on domain, domain depends on data
- Example: `feature:listing:ui` → `feature:listing:domain` → `feature:listing:data` → `core:database`

## Technology Stack

### Build System & Dependencies
- **Gradle 9.0.0** with Kotlin DSL (.kts files everywhere)
- **Version catalog** at [`gradle/libs.versions.toml`](../gradle/libs.versions.toml) - ALWAYS use `libs.*` references, never hardcode versions
- **Kotlin 2.0.21** with Compose Compiler Plugin (`kotlin-compose`)
- **AGP 9.0.0** - Android Gradle Plugin

### Key Libraries (from version catalog)
- **Jetpack Compose** - 100% Compose UI (Material3, Navigation)
- **Hilt** - Dependency injection (`@HiltViewModel`, `@Inject`, `@AndroidEntryPoint`)
- **Room 2.8.4** - Local database with Flow + Coroutines
- **KSP** - Annotation processing for Hilt/Room (replaces kapt)
- **Coil** - Image loading
- **Retrofit + OkHttp + Kotlin Serialization** - Networking (not yet actively used)
- **DataStore** - Preferences
- **Security Crypto** - Encrypted storage

### Key Gradle Patterns
```kotlin
// Every feature module's build.gradle.kts includes:
ksp {
    arg("dagger.hilt.android.internal.disableAndroidSuperclassValidation", "true")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)  // Or jvmToolchain(17)
    }
}
```

## Code Conventions & Patterns

### Dependency Injection (Hilt)
- Activities: `@AndroidEntryPoint`
- ViewModels: `@HiltViewModel class FooViewModel @Inject constructor(...)`
- Repositories: Constructor injection with `@Inject`
- Modules: `@Module @InstallIn(SingletonComponent::class)` in `feature/*/data/di/` or `core/*/di/`
- Database DAOs provided in [`core:database:di/DatabaseModule.kt`](../core/database/src/main/java/com/hyms/core/database/di/DatabaseModule.kt)

### Data Persistence
- **Room + Flow** for reactive local data
- **Draft autosave pattern** (see `DraftListingViewModel`):
  - ViewModels expose `StateFlow<UiState>`
  - Debounced saves using `kotlinx.coroutines.delay(500ms)` + `cancel()` on new input
  - Hydrate once on init, avoid overwriting user edits
  - Schema exports enabled: `exportSchema = true` with schemas in `core/database/schemas/`

### UI Patterns
- **Compose-first** - No XML layouts
- **Design system tokens** from `core:ui`:
  - `Spacing.Small`, `Spacing.Medium`, etc. (4dp increments)
  - `Radius.Small`, `Radius.Medium` for corner radii
  - Custom components: `PrimaryButton`, `SecondaryButton`, `MinimalTextField`, `PillChip`
  - Theme: `HymsTheme { ... }`
- **Navigation**: Type-safe sealed classes (see [`MainActivity.kt`](../app/src/main/java/com/hyms/app/MainActivity.kt) `Tab` sealed class)
- **ViewModels expose StateFlow** - UI collects with `collectAsStateWithLifecycle()`

### Naming Conventions
- Entities: `*Entity.kt` (e.g., `DraftListingEntity`)
- DAOs: `*Dao.kt` (e.g., `DraftListingDao`)
- ViewModels: `*ViewModel.kt` with nested `UiState` data class
- Use cases: `*UseCase.kt` in domain layer
- Repositories: Interface in domain, `*RepositoryImpl` in data layer
- DI modules: `*Module.kt` in `di/` package

### Package Structure
```
com.hyms.{module}.{layer}
  - com.hyms.feature.listing.ui.viewmodel
  - com.hyms.feature.listing.domain.usecase
  - com.hyms.feature.listing.data.repository
  - com.hyms.core.database.dao
```

## Development Workflows

### Build & Run
```powershell
# Build project
.\gradlew build

# Run app on connected device/emulator
.\gradlew :app:installDebug
# Or use Android Studio Run (Shift+F10)

# Clean build
.\gradlew clean build
```

### Emulator Setup & Running
```powershell
# List available emulators
emulator -list-avds

# Start an emulator (replace with your AVD name)
emulator -avd Pixel_8_API_34

# Start emulator in background
Start-Process emulator -ArgumentList "-avd", "Pixel_8_API_34"

# Create a new emulator (via Android Studio)
# Tools → Device Manager → Create Device
# Recommended: Pixel 8, API 34, x86_64 image

# Check connected devices
adb devices

# Install and launch app
.\gradlew :app:installDebug
adb shell am start -n com.hyms.app/.MainActivity
```

**Recommended emulator settings:**
- Device: Pixel 8 or similar (1080x2400)
- API Level: 34 (Android 14)
- System Image: x86_64 (for performance)
- RAM: 2048 MB minimum
- Enable "Cold boot" for process death testing

### Database Schema Management
- Room schemas exported to `core/database/schemas/`
- Migration strategy: Version bumps in `HymsDatabase` + manual migrations (when needed)
- Current version: 2

### Module Creation Checklist
When adding a new feature module:
1. Add to [`settings.gradle.kts`](../settings.gradle.kts): `include(":feature:newfeature")`
2. Create `build.gradle.kts` with standard plugins (see `feature:listing:ui` as template)
3. Add KSP args for Hilt validation
4. Set namespace: `android.namespace = "com.hyms.feature.newfeature"`
5. Add module dependency to [`app/build.gradle.kts`](../app/build.gradle.kts)

### Common Gotchas
- **Always use KSP, not kapt** - This project uses `libs.plugins.ksp`
- **Photo URIs are local for now** - Phase 2 will upload to backend (see roadmap in README.md)
- **No backend yet** - Using `FakeFeedRepository` for feed data
- **UK defaults** - GBP currency, UK sizing hints expected
- **Process death** - Draft autosave must survive process death (tested pattern in place)

## Current Development Phase

**Phase 1 - Core Marketplace Loop** (see [`README.md`](../README.md) roadmap):
- ✅ Seller draft flow with local photos
- 🚧 **NEXT HIGH PRIORITY**: Listing Detail Screen (image carousel, price, title, size, condition, seller preview)
- ⏭️ Remove/reorder photos in draft
- ⏭️ Buyer actions (save, message UI)
- ⏭️ Search & filters (minimal)

**Not Yet Implemented:**
- Backend API integration (Phase 2)
- Photo upload to remote storage (Phase 2)
- Real chat (Phase 2)
- Payments/shipping (Phase 3)

## Design Philosophy Enforcement

When implementing features, ask:
1. Does this create visual clutter? → **Reject**
2. Are there multiple competing CTAs? → **Simplify to one primary action**
3. Is this "just in case" functionality? → **Don't build it**
4. Does the transition feel abrupt? → **Add fluid animation**
5. Is secondary info always visible? → **Use progressive disclosure**

Read [`docs/hyms-essence.md`](../docs/hyms-essence.md) for the full quality checklist.

## Quick Reference

### Key Files to Check
- Architecture decisions: [`README.md`](../README.md), [`docs/hyms-essence.md`](../docs/hyms-essence.md)
- Module structure: [`settings.gradle.kts`](../settings.gradle.kts)
- Dependencies: [`gradle/libs.versions.toml`](../gradle/libs.versions.toml)
- Navigation: [`app/src/main/java/com/hyms/app/MainActivity.kt`](../app/src/main/java/com/hyms/app/MainActivity.kt)
- Database schema: [`core/database/src/main/java/com/hyms/core/database/HymsDatabase.kt`](../core/database/src/main/java/com/hyms/core/database/HymsDatabase.kt)
- Design system: `core/ui/src/main/java/com/hyms/core/ui/` (Theme.kt, Spacing.kt, Color.kt, etc.)

### Example Patterns
- **ViewModel with autosave**: [`feature/listing/ui/viewmodel/DraftListingViewModel.kt`](../feature/listing/ui/src/main/java/com/hyms/feature/listing/ui/viewmodel/DraftListingViewModel.kt)
- **Room DAO with Flow**: `core/database/src/main/java/com/hyms/core/database/dao/DraftListingDao.kt`
- **Hilt module**: [`feature/feed/data/di/FeedDataModule.kt`](../feature/feed/src/main/java/com/hyms/feature/feed/data/di/FeedDataModule.kt)
- **Compose screen**: [`feature/feed/ui/HomeFeedScreen.kt`](../feature/feed/src/main/java/com/hyms/feature/feed/ui/HomeFeedScreen.kt)
