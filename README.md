# SelfGoals 🎯

A high-polish, local-first Android application designed to help you define, track, and achieve your life goals. Built with a modern **iOS 18-inspired aesthetic**, SelfGoals combines powerful functionality with a fluid, tactile user experience.

## ✨ Features

-   **iOS 18 Design Language:** Modular "widget" layouts, glassmorphism, bold typography, and fluid squircle shapes.
-   **Haptic Feedback:** Tactile "clicks" and physical confirmation for key interactions (toggles, archiving, adding items) for a premium, responsive feel.
-   **Intelligent Analytics:** Real-time activity dashboard showing goal and task completion rates via modular overview tiles.
-   **Goal Management:** Create, **edit**, and track goals with detailed descriptions, custom categories, and **Priority** status (marked with a ⭐).
-   **Milestones (Sub-tasks):** Break down large goals into smaller, actionable milestones with automated progress tracking and **manual reordering** (Up/Down controls).
-   **Advanced Sorting:** Organize your dashboard by **Deadline**, **Progress**, **Creation Date**, **Name**, or **Priority**.
-   **Custom Theme Control:** Robust, independent Light/Dark mode toggle that respects user preference over system settings.
-   **Build Verified:** Fixed theme consistency issues and confirmed stable build on AVD.
-   **Goal Archives:** Keep your dashboard focused by archiving completed goals. A dedicated view lets you manage and unarchive your history.
-   **Search & Filtering:** Dynamic, iOS-style search bar and interactive category tags for instant goal discovery.
-   **Smart Reminders:** Schedule local notifications using `WorkManager` to stay on top of your deadlines.
-   **Visual Deadlines:** Integrated Material 3 DatePicker with **overdue highlighting** (Red alert for past-due tasks).
-   **Data Export:** Share a comprehensive, beautifully formatted text summary of your goals, milestones, and stats with other apps.

## 🛠 Tech Stack

-   **Language:** Kotlin 1.9.22
-   **UI Framework:** Jetpack Compose (Declarative UI)
-   **Architecture:** MVVM (Model-View-ViewModel)
-   **Dependency Injection:** Hilt 2.50
-   **Local Persistence:** Room Database 2.6.1 (SQL abstraction)
-   **Background Tasks:** WorkManager (for robust reminders)
-   **Asynchronous Flows:** Kotlin Coroutines & StateFlow
-   **Build System:** Gradle 9.5.0 with KSP (Kotlin Symbol Processing)

## 📂 Project Structure

```text
SelfGoals/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/selfgoals/
│   │   │   ├── data/           # Entities (Goal, Category, Milestone), DAOs, and Repositories
│   │   │   ├── di/             # Hilt Dependency Injection modules
│   │   │   ├── ui/             # Compose Screens (Dashboard), ViewModels, and Themes
│   │   │   ├── worker/         # WorkManager background workers for notifications
│   │   │   └── utils/          # Helpers (Notifications, formatting)
│   │   └── AndroidManifest.xml # App manifest and configuration
└── build.gradle.kts            # Root project dependencies and configuration
```

## 🚀 Getting Started

1.  Clone the repository.
2.  Open the project in **Android Studio Hedgehog** or newer.
3.  Sync Gradle and run on an emulator or physical device (API 24+).
4.  **Haptics Note:** For the best experience, run on a physical device with a haptic engine enabled.

---
*SelfGoals is built with care using modern Android standards and a focus on UX.*

## ✅ Project Status: v1.0 Stable
- **Build Verified:** Successfully built using Gradle 9.5 and Kotlin 1.9.22.
- **Environment Tested:** Verified on Android Virtual Device (AVD) running API 34 (Pixel 6).
- **Architecture:** 100% Jetpack Compose with modular MVVM pattern.
