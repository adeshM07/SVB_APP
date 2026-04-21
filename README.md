# SVB Field Ops (Android)

Kotlin, Jetpack Compose, Material 3, Navigation Compose, and Dagger Hilt. This repo currently implements the **shared five-screen clock-in flow** (P0–P2): Splash through Clock-in preview, plus a **placeholder** screen after clock-in.

## Requirements

- JDK 17
- Android SDK (API 35 compile). Set `sdk.dir` in **`local.properties`** (Android Studio usually creates this file):

```properties
sdk.dir=C\:/Users/YOU/AppData/Local/Android/Sdk
```

Use forward slashes or escaped Windows paths.

## Build

```bash
./gradlew assembleDebug
```

On Windows: `gradlew.bat assembleDebug`

## Run

Open the `svb_app` folder in **Android Studio**, select a device or emulator, and run the `app` configuration.

## Project layout

- `ui/theme` — SVB Field Ops colors, typography scale, shapes
- `presentation/navigation` — `AppNavHost`, route constants
- `presentation/screens` — five flow screens + `FlowCompleteScreen`
- `presentation/viewmodel` — shared `ClockInFlowViewModel` for the nested graph
- `presentation/components` — shared buttons (54–56dp height, 12dp radius)
- `di` — Hilt modules (`AppModule` placeholder)

## Next milestones

- **P3:** CameraX preview + capture + Coil on preview
- **P4:** Fused location on geofence (optional mock until APIs)
- **P5:** Retrofit + kotlinx.serialization + encrypted session; replace placeholder with real role homes
