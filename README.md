# SelfGoals 🎯

A high-polish, local-first Android application designed to help you define, track, and achieve your life goals. Built with a modern **iOS 18-inspired aesthetic**, SelfGoals combines powerful functionality with a fluid, tactile user experience.

## ✨ Features

-   **iOS 18 Design Language:** Modular "widget" layouts, glassmorphism, bold typography, and fluid squircle shapes.
-   **Haptic Feedback:** Tactile "clicks" and physical confirmation for key interactions (toggles, archiving, adding items) for a premium, responsive feel.
-   **Intelligent Analytics:** Real-time activity dashboard showing goal and task completion rates via modular overview tiles.
-   **Goal Management:** Create, **edit**, and track goals with detailed descriptions, custom categories, and **Priority** status.
-   **Milestones (Sub-tasks):** Break down large goals into smaller, actionable milestones with automated progress tracking and **manual reordering**.
-   **Advanced Sorting:** Organize your dashboard by **Deadline**, **Progress**, **Creation Date**, **Name**, or **Priority**.
-   **Custom Theme Control:** Switch between Light, Dark, or System mode independently of system settings.
-   **Goal Archives:** Keep your dashboard focused by archiving completed goals. A dedicated view lets you manage your history.
-   **Search & Filtering:** Dynamic, iOS-style search bar and interactive category tags for instant goal discovery.
-   **Smart Reminders:** Schedule local notifications using `WorkManager` to stay on top of your deadlines.
-   **Data Export:** Share a comprehensive text summary of your goals and progress with other apps.

## 🛠 Tech Stack

-   **Language:** Kotlin
-   **UI Framework:** Jetpack Compose (Declarative UI)
-   **Architecture:** MVVM (Model-View-ViewModel)
-   **Dependency Injection:** Hilt (Dagger)
-   **Local Persistence:** Room Database (SQL abstraction)
-   **Background Tasks:** WorkManager (for reminders)
-   **Asynchronous Flows:** Kotlin Coroutines & StateFlow

## 📂 Project Structure

```text
SelfGoals/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/selfgoals/
│   │   │   ├── data/           # Entities (Goal, Category, Milestone), DAOs, and Repositories
│   │   │   ├── di/             # Hilt Dependency Injection modules
│   │   │   ├── ui/             # Compose Screens, ViewModels (Dashboard, Progress), and Themes
│   │   │   ├── worker/         # WorkManager background workers for reminders
│   │   │   └── utils/          # Helpers (Notifications, Haptics)
│   │   └── AndroidManifest.xml # App configuration
└── build.gradle.kts            # Project dependencies
```

## 🚀 Getting Started

1.  Clone the repository.
2.  Open the project in **Android Studio Hedgehog** or newer.
3.  Sync Gradle and run on an emulator or physical device (API 24+).

---
*SelfGoals is built with care using modern Android standards.*
