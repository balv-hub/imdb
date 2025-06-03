# IMDb Android App

## Setup Instructions

### 1. Build and Run in Android Studio
- Open the project in **Android Studio** (version Meerkat Feature Drop or newer recommended).
- Set JAVA_HOME to the JDK 21 path in your environment variables.
- Let Gradle sync and download dependencies.
- Connect an Android device or start an emulator.
- Click **Run** (▶️) to build and launch the app.

### 2. API Key Configuration
- The app requires an API key (e.g., for TMDb or IMDb API):
  1. Create a file named `local.properties` in the project root (if not present).
  2. Add your API key:
     ```
     IMDB_API_TOKEN=your_api_key_here
     ```
  3. The app will read this key at build/runtime.

### 3. Entry Point
- The main entry point is `MainActivity` located in `app/src/main/java/.../MainActivity.kt`

---

## Architecture
- The app follows a **clean architecture** pattern.
- The app uses the **MVVM (Model-View-ViewModel)** architecture for clear separation of concerns and testability.
- Data flows from Repository → ViewModel → UI (Activity/Fragment).

## Local Caching
- **Room Database** is used for local caching of movie data, enabling offline access and efficient data management
- Refresh rates are controlled based on use cases:
  * All movies: ALL_MOVIES_REFRESH_RATE_THRESHOLD = 2 hours
  * A movie's detail: DETAIL_POLL_THRESHOLD = 1 minute
  * Genre list: GENRE_POLL_THRESHOLD = 1 minute
- Pull to refresh is implemented for the main movie list screen, allowing users to manually refresh data.

## Time spent: probably around 4 days
## Bonus Features: Popular movies, Cast..