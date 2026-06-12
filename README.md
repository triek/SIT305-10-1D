# SIT305-10.1D Learning App

SIT305-10.1D is an Android learning prototype built with Kotlin. The app gives a student a simple personalised study flow: sign in, choose learning interests, receive a generated practice task, submit answers, review results, and track learning progress.

## What the app does

- Shows a student-friendly login and home dashboard.
- Lets students select study interests such as algorithms, data structures, web development, testing, mobile apps, databases, cybersecurity, and cloud computing.
- Creates a short practice task based on the selected interests.
- Saves task attempts locally with Room so students can review their history and profile statistics.
- Provides Basic, Plus, and Premium account levels with different learning features.
- Supports optional Gemini-powered study assistance when a `GEMINI_API_KEY` is configured.

## Main screens

- **Login**: enter a student name or continue with the sample profile.
- **Home**: view the current task, selected interests, plan status, profile, and history links.
- **Interest selection**: customise the topics used to choose learning tasks.
- **Generated task**: answer a multiple-choice question and a short follow-up prompt.
- **Results**: review the task summary and feedback.
- **Profile and history**: view account details, saved attempts, accuracy, and shareable progress information.
- **Upgrade account**: compare and select Basic, Plus, or Premium plans.

## Tech stack

- Kotlin and Android SDK
- XML layouts with Android activities
- Room database for local learning history
- Gradle Kotlin DSL
- Gemini API integration for optional AI study content

## Running the project

1. Open the project in Android Studio.
2. Sync Gradle dependencies.
3. Optional: add `GEMINI_API_KEY=your_key_here` to `local.properties` to enable Gemini features.
4. Run the `app` configuration on an emulator or Android device.

## Useful commands

```bash
./gradlew test
./gradlew assembleDebug
```
