# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**withmo** is a Japanese Android launcher application that combines "Digital Figure × Launcher" functionality, allowing users to display 3D models (.vrm format) on their home screen using Unity as a Library. The app serves both as an interactive launcher and live wallpaper, with time-based visual changes and extensive customization options.

## Architecture

### Multi-Module Clean Architecture with MVI

The project implements Clean Architecture with MVI (Model-View-Intent) pattern across feature modules:

**Core Modules:**
- `core:common` - Unity integration, constants, dispatchers
- `core:data` - Room database, DataStore repositories, managers
- `core:domain` - Use cases, repository interfaces, business logic
- `core:designsystem` - Compose components, themes, Material 3 design system
- `core:featurebase` - Base MVI classes (State, Action, Effect, BaseViewModel)
- `core:model` - Domain models and user settings data classes
- `core:service` - Background services (UnityWallpaperService, NotificationListener)
- `core:ui` - Shared UI utilities, providers, modifiers
- `core:util` - General utilities, extension functions

**Feature Modules:**
- `feature:home` - Main launcher with 3D model display, widget management
- `feature:onboarding` - Initial setup flow for model selection and app configuration
- `feature:setting` - Settings screens (app icons, clock, themes, favorites, etc.)

### MVI Implementation

Each feature follows strict MVI pattern with unified data flow:

**Core Components:**
- **Action**: User operations passed to ViewModel as input
- **State**: Data required for screen rendering
- **Effect**: One-time side effects like navigation and toasts that UI executes once

**BaseViewModel Architecture:**
```kotlin
abstract class BaseViewModel<S : State, A : Action, E : Effect> : ViewModel() {
    protected val _state = MutableStateFlow<S>(createInitialState())
    val state: StateFlow<S> = _state.asStateFlow()
    
    protected val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()
    
    abstract fun onAction(action: A)
    fun updateState(update: S.() -> S)
    fun sendEffect(effect: E)
}
```

**Screen Structure:**
- **Main Screen**: Receives ViewModel, subscribes to effects, handles navigation
- **Content Screen**: Receives state + onAction, pure UI rendering only

**Naming Conventions:**
- **Action**: `On` + target + past tense (e.g., `OnSaveButtonClick`, `OnNavigateSettingsButtonClick`)
- **Effect**: Imperative verb + object (e.g., `NavigateBack`, `ShowToast`, `OpenDocument`)
- **State**: Noun properties (e.g., `clockSettings`, `isSaveButtonEnabled`)

**Data Flow Example:**
1. User presses back button
2. `onAction(OnBackButtonClick)` executed
3. ViewModel calls `sendEffect(NavigateBack)`
4. Screen's LaunchedEffect collects and executes navigation

### Unity as a Library Integration

Sophisticated Unity integration supporting dual-surface rendering:

**UnityManager** (`core:common`):
- Singleton managing UnityPlayer lifecycle
- Surface switching between Activity and Wallpaper modes
- Lifecycle-aware methods for different contexts

**Messaging Bridge**:
- `AndroidToUnityMessenger`: Structured messaging using enums (UnityObject/UnityMethod)
- `UnityToAndroidMessenger`: Callback interface with WeakReference for memory safety
- Bidirectional communication for model loading, animations, theme changes

**Multi-Surface Support**:
- Activity surface: Unity as background layer with Compose UI overlay
- Wallpaper surface: Live wallpaper implementation via WallpaperService

## Build System

### Convention Plugins

Custom Gradle convention plugins in `build-logic/convention/`:
- `withmo.android.application` - Main app configuration
- `withmo.android.feature` - Feature module setup with MVI dependencies
- `withmo.android.library.compose` - Compose library configuration
- `withmo.detekt` - Code quality with custom rules
- `withmo.hilt` - Dependency injection setup

### Common Commands

```bash
# Run static analysis
./gradlew detekt
```

## Key Technical Details

### Data Layer
- **Room Database**: App info, widget data with Flow-based reactive queries
- **DataStore**: User settings, one-time events (replacing SharedPreferences)
- **File Management**: VRM model files stored in internal storage

### Unity Integration Points
- MainActivity initializes UnityManager and observes model changes
- HomeViewModel implements MessageReceiverFromUnity for callbacks
- NotificationListener triggers Unity animations on system notifications
- Theme system sends time-based commands to Unity for visual changes

### Important Constraints
- **Unity Library Not Included**: unityLibrary is excluded from git due to size
- **VRM File Support**: Only VRM format 3D models are supported
- **NDK ABIs**: Limited to armeabi-v7a and arm64-v8a
- **Closed Testing**: Currently in closed testing phase

### Design System Notes
- Material 3 theming with time-based background changes
- Japanese-first UI with comprehensive localization
- Custom Compose components with KDoc documentation
- Paddings object for consistent spacing (Tiny=2dp to ExtraLarge=25dp)

### Development Patterns
- Use `@sample` annotations in KDoc for preview functions
- Follow existing MVI patterns when adding new features
- Implement proper lifecycle management for Unity interactions
- Use Flow-based reactive programming throughout data layer

## Testing & Quality
- **Detekt**: Comprehensive static analysis with compose-specific rules
- **GitHub Actions**: Automated CI/CD with code quality checks
- **Convention-based Structure**: Enforced through custom Gradle plugins
- **Minimal Test Coverage**: Focus on critical Unity integration points if adding tests

## Project Recognition

- 技育CAMP2023 vol14 優秀賞 (Excellence Award)
- 技育展2024 YUMEMI賞 (YUMEMI Award)
- Featured in closed testing on Google Play