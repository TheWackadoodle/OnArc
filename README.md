# OnArc

A visual day planner for neurodivergent minds: your day as a dial of coloured arcs, with a Wear OS watch that nudges you through each switch.

> **Status:** early development, still on the foundation.

## Stack

- **Phone app:** Java, Android Views + Material Components
- **Watch app:** Kotlin, Compose for Wear OS (comes after the phone app)
- **Shared core:** a plain Java module with the data model, time engine and sync format, used by both apps

## Project layout

```
OnArc/
├── app/      ← phone app (Java)
├── shared/   ← plain Java core, no Android dependencies
└── wear/     ← watch app (Kotlin), added later
```

## Building

Open the project in Android Studio and run the `app` configuration on a device or emulator.

## Plan

See [`docs/BUILD_PLAN.md`](docs/BUILD_PLAN.md) for the full roadmap. In short: foundation first, then the dial; the phone app is finished before the watch.
