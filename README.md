# OnArc

A visual day planner for neurodivergent minds: your day as a dial of coloured arcs, with a Wear OS watch that nudges you through each switch.

> **Status:** early development, still on the foundation.

## Stack

- **Phone app:** Java, Android Views + Material Components
- **Watch app:** Kotlin, Compose for Wear OS (comes after the phone app)
- **Shared core:** a plain Java module with the data model, time engine and sync format, used by both apps

## Building

Open the project in Android Studio and run the `app` configuration on a device or emulator.

---

# Build Plan

## Ground rules

1. **Foundation first.** The data model, time engine, storage and nudges come before the dial. If the foundation is shaky, the dial is useless to the people it's for.
2. **The phone is finished before the watch.** The phone app must be solid on its own, and then the watch plugs into it.
3. **Build for the watch from day one, without building the watch.** Anything both apps need goes in a `shared` module, so the watch can reuse it later without a rewrite.
4. **The phone is the source of truth.** The watch only shows the plan and sends actions back.

---

## 0. Decisions

| Decision | Choice / recommendation | Notes |
|---|---|---|
| Name | **OnArc** | |
| Package name | e.g. `com.onarc.app` | Permanent once you release. The phone and watch apps must share it. |
| Phone UI toolkit | **XML layouts + Views** (Material Components) | Jetpack Compose is Kotlin-only. The dial will be a custom `View`. |
| Watch UI toolkit | **Compose for Wear OS** (Kotlin) | Decide when we reach the watch. |
| Phone minSdk | 26 (Android 8.0) | |
| Watch minSdk | 30 (Wear OS 3) | |
| Dial | 12-hour or 24-hour? | **Open.** Decide before step 6. |
| v1 scope | Local only: no accounts, no cloud, no calendar import | |

---

## 1. Project skeleton

```
OnArc/
├── app/      ← phone app (Java)
├── shared/   ← plain Java library: data model, time engine, sync format
└── wear/     ← watch app (Kotlin), added at step 8
```

- [ ] Create a new Android Studio project: **Empty Views Activity**, language **Java**. This becomes `app/`.
- [ ] Add a **Java Library** module called `shared/`, and make `app` depend on it.
- [ ] Rules for `shared/`: no Android imports. Plain Java only, so it can be unit-tested fast and the watch can reuse it.
- [ ] Add a version catalog (`gradle/libs.versions.toml`) and an Android `.gitignore`.
- [ ] Check that it builds and launches, then commit.
- [ ] *(Optional, later)* **CI**: a GitHub Action that builds and tests on every push.

## 2. Data model + time engine (in `shared/`)

This is the heart of the app. **Work it out together before writing code.** Questions to answer:

- What is an **Arc**? Title, start, end, colour, icon, notes, warning time… What else?
- What does a **day** look like? Can there be gaps? Can arcs overlap?
- **Templates and recurring days**: "Workday", "Weekend", and so on. How does one get applied to a day?
- **Transitions**: how many warnings before a switch, and what kind? Do different arcs need different warnings?
- **Real life**: what happens when an arc overruns, or gets skipped or snoozed? Does the rest of the day shift, or stay put?
- **Edge cases**: arcs that cross midnight, DST changes, timezone travel.

Then:

- [ ] Model classes (Arc, DayPlan, Template, …)
- [ ] Time engine: current arc, next arc, time left, upcoming nudges, and what to do when things change
- [ ] JSON serialization (used later for backups and for the watch)
- [ ] **Unit tests for all of it**

## 3. Storage (phone)

- [ ] **Room** database for arcs, days and templates.
- [ ] A repository layer. The UI never touches the database directly.
- [ ] `ViewModel` + `LiveData`.
- [ ] Settings in `SharedPreferences` or DataStore.

## 4. Nudge engine (phone)

- [ ] Notification channels: "coming up" and "switch now".
- [ ] Ask for the `POST_NOTIFICATIONS` permission (Android 13+).
- [ ] Use exact alarms (`setExactAndAllowWhileIdle`) and handle `SCHEDULE_EXACT_ALARM` / `USE_EXACT_ALARM`.
- [ ] Reschedule after a reboot, a time or timezone change, and an app update.
- [ ] Notification actions: Done, +5 min, Skip.
- [ ] Test with Doze forced on (`adb shell dumpsys deviceidle force-idle`), and on Samsung or Xiaomi phones if possible.
- [ ] Build it so the watch can later be just another place a nudge gets delivered.

## 5. Ugly test screen

- [ ] A plain list of today's arcs, with add, edit and delete, wired to the real storage and nudges.
- [ ] Use it for real for a few days. If the foundation is wrong, fix it here, before the dial.

## 6. The dial

- [ ] A custom `DialView extends View`: the arcs, a "now" hand and labels.
- [ ] Tap an arc to edit it. Later, drag its ends to resize it.
- [ ] TalkBack support through `ExploreByTouchHelper`.
- [ ] Screens: Today, the arc editor (a bottom sheet), Templates, Settings, and onboarding.

## 7. Polish

- [ ] Dark mode, large text, reduced motion.
- [ ] A calm palette with enough contrast between neighbouring arcs.
- [ ] Gentle wording. Never make the user feel bad for falling behind.
- [ ] Use it yourself for a couple of weeks.
- [ ] **The phone app is "done" here.**

## 8. Watch (Kotlin)

- [ ] Add the `wear/` module with the **same applicationId and signing key** as the phone app.
- [ ] Sync with the Wearable Data Layer: the phone pushes the plan (DataClient) and the watch sends actions back (MessageClient).
- [ ] Main screen: the current arc and time left, plus what's next. Also a mini dial.
- [ ] The watch schedules its own haptic alarms from the synced plan, so nudges still work when the phone is away.
- [ ] A Tile and a complication.
- [ ] Respect ambient mode and battery life.

## 9. Testing (ongoing, not only at the end)

- [ ] Unit tests for `shared/`.
- [ ] Room/DAO tests.
- [ ] Espresso smoke tests.
- [ ] Manual checklist:
  - [ ] nudges fire on time with the screen off
  - [ ] nudges still fire after a reboot
  - [ ] plans that cross midnight work
  - [ ] DST changes are handled
  - [ ] TalkBack can read the dial

## 10. Release

- [ ] Create a release signing key. **Back it up, never commit it, and never lose it.**
- [ ] Turn on Play App Signing. Create a Play Console account ($25).
- [ ] Icon, screenshots (phone and watch), store description, and a privacy policy URL.
- [ ] Fill in the Data safety form.
- [ ] Target the API level Play currently requires.
- [ ] Test the release build, including R8 keep rules for Room and Gson.
- [ ] Run a closed test (new accounts need a set number of testers for a set period; check the current rules), then release to production.

---

## Later, not v1

Calendar import, cloud backup or accounts, home-screen widgets, stats (careful — can turn into shame), iOS.
