# OnArc

A visual day planner for neurodivergent minds: your day as a dial of coloured arcs, with a Wear OS watch that nudges you through each switch.

> **Status:** early development, still on the foundation.

## Stack

- **Phone app (DPJ):** a Java backbone (Room, repositories, IDs, time engine, alarms and notifications) with a Flutter (Dart) UI on top, connected by Pigeon
- **Watch app:** Kotlin, Compose for Wear OS (comes after the phone app)

## Building

Open the project in Android Studio and run the `app` configuration on a device or emulator.

---

# Design (decided)

## Dials and arcs

- A **Dial** is one whole day's layout. Arcs live on a Dial, the way paint lives on a painting.
- **Arcs never move between Dials.** Each arc stores the ID of the Dial it's on (`dialId`). Deleting a Dial deletes its arcs.
- A Dial's **name is only a label for the user**. The code only cares about its ID and its assignments.
- **The app ships with one default Dial.** The user can edit it or make new ones.

## The grid and arc rules

- Every Dial is a **5-minute grid**. This is fixed in code; the user never changes it.
- Arc edges always sit on the grid (9:05 yes, 9:07 no), and arcs resize in 5-minute steps.
- An arc is **5 minutes to 24 hours** long. A full-day arc ends at minute 1440 (midnight).
- **Arcs don't overlap.**
- **Arcs never cross midnight.** Each Dial is one self-contained 24-hour day.
- All of these rules are checked in one place (`ArcValidator`) before anything is saved.

## The tray

- The Dial editor has a **tray of 8 default arcs**, each with a preset colour and emoji. They're 1 hour long by default.
- The user **always starts an arc by placing one from the tray**, then edits it if they want.
- Tray arcs are presets, not stored arcs. Placing one creates a brand-new arc on the Dial.

## Weekdays and dates

- The user picks **which day the week starts on**. The Dials screen shows the weekdays left to right, starting with that day.
- **Each weekday has exactly one Dial assigned.** A Dial can be on 0–7 weekdays, in any combination.
- **Dates aren't stored for weekdays.** The app works out which dates are in "this week" on the fly. Days that have already passed stay on screen until the week ends.
- A Dial can be **assigned to a specific date ahead of time** (e.g. a family gathering). A date assignment **always beats** that date's weekday Dial. This is the ADHD accommodation: when you know a weird day is coming, set it up right then.
- The user gets **warned before deleting or editing a Dial that's assigned to a date**, so they don't wipe it by accident.
- **One-off Dials stay in the catalog** after their date passes. A setting to clean them up may come later.

## Limits

- **99 Dials** total.
- **288 arcs per Dial** (the most that fit in 24 hours at 5 minutes each).

## IDs

| Kind | Format | Example | Reads as |
|---|---|---|---|
| Dial | `D` + origin + dial # | `DU04` | Dial, user-made, #04 |
| Arc | `A` + origin + dial # + arc # | `AU04045` | Arc, user-made, on Dial 04, arc #045 |

- **Origin:** `S` = shipped with the app, `U` = user-made. For arcs, it's how the arc itself was born: arcs that ship on the default Dial are `S`, and arcs the user adds (to any Dial) are `U`.
- **No check digit.** Dropped as overkill.
- **The whole ID is the key.** `AS01001` and `AU01001` can both exist.
- **Numbering:** each new ID takes the lowest free number, worked out fresh from the database every time. Deleted numbers get reused.
- **Open:** can a `DS01` and a `DU01` both exist? If so, arc IDs can't tell which Dial 01 they mean.

## Shelved for now

- **Tags** on Dials (user-only labels for filtering).
- **Nudges** (reminders and alarms). The fields stay on `ArcEntity`, but the feature isn't part of the first draft.
- Temporary fallback IDs.

---

# Build Plan

## Ground rules

1. **Foundation first.** The data model, time engine and storage come before the dial. If the foundation is shaky, the dial is useless to the people it's for.
2. **The phone is finished before the watch.** The phone app must be solid on its own, and then the watch plugs into it.
3. **Keep the watch in mind without building it.** Schedules will be sent to the watch as JSON, so the data model has to serialize cleanly. (The old `shared` module was deleted on purpose. Everything lives in `app/` for now.)
4. **The phone is the source of truth.** The watch only shows the plan and sends actions back.
5. **All logic lives in Java.** The Flutter UI only shows things and sends user actions back through Pigeon.

---

## 0. Decisions

| Decision | Choice / recommendation | Notes |
|---|---|---|
| Name | **OnArc** | |
| Package name | **`com.onarc.app`** | Permanent once released. The phone and watch apps must share it. |
| Phone UI toolkit | **Flutter (Dart)** over a Java backbone, bridged by **Pigeon** ("DPJ") | Decided. All logic stays in Java; Dart is screens and drawing only. Project shape (add-to-app vs. a Flutter project with the Java in `android/`) gets decided when UI work starts. |
| Watch UI toolkit | **Compose for Wear OS** (Kotlin) | Decide when we reach the watch. |
| Phone minSdk | 26 (Android 8.0) | |
| Watch minSdk | 30 (Wear OS 3) | |
| Dial | 12-hour or 24-hour? | **Open.** Decide before step 6. |
| v1 scope | Local only: no accounts, no cloud, no calendar import | |

---

## 1. Project skeleton

```
OnArc/
├── app/      ← phone app: Java backbone now, Flutter UI added when UI work starts
└── wear/     ← watch app (Kotlin), added at step 8
```

- [x] Create a new Android Studio project, language **Java**. This becomes `app/`.
- [x] Add a version catalog (`gradle/libs.versions.toml`) and an Android `.gitignore`.
- [x] Check that it builds.
- [ ] *(Optional, later)* **CI**: a GitHub Action that builds and tests on every push.

## 2. Data model + time engine (Java)

- [x] `ArcEntity`
- [x] `DialEntity`
- [ ] Foreign key on `ArcEntity.dialId` (delete a Dial → its arcs go too)
- [ ] ID system (see **IDs** above)
- [ ] Weekday assignments (7 rows: weekday → Dial)
- [ ] Date assignments
- [ ] The shipped default Dial, and the 8 tray presets
- [ ] Time engine: the 5-minute grid, "this week's" dates, `ArcValidator`, which Dial a date gets (date assignment first, else the weekday's), current arc, next arc
- [ ] Transitions: what happens when an arc is completed, skipped or extended
- [ ] JSON serialization (used later for backups and for the watch)
- [ ] **Unit tests for all of it**

Still to work out: DST changes and timezone travel.

## 3. Storage (phone)

- [x] **Room** database for arcs and Dials.
- [x] A repository layer (`ArcRep`, `DialRep`). The UI never touches the database directly.
- [ ] Room tables for weekday and date assignments.
- [ ] `LiveData` on the Java side, a state holder (Provider or Riverpod) on the Flutter side, with Pigeon carrying data and "something changed" signals between them.
- [ ] Settings (week start day) in `SharedPreferences` or DataStore.

## 4. Ugly test screen

- [ ] A plain list of a Dial's arcs, with add, edit and delete, wired to the real storage.
- [ ] Use it for real for a few days. If the foundation is wrong, fix it here, before the dial.

## 5. The dial and screens

- [ ] The dial, drawn in Flutter (`CustomPainter`): the arcs, a "now" hand and labels.
- [ ] Tap an arc to edit it. Drag its ends to resize it, in 5-minute steps.
- [ ] TalkBack support through Flutter's `Semantics` widget.
- [ ] Screens: the week strip (Dials screen), the Dial catalog, the Dial editor with the tray, the arc editor, Settings, and onboarding.

## 6. Nudge engine (phone) — after the first draft

- [ ] Notification channels: "coming up" and "switch now".
- [ ] Ask for the `POST_NOTIFICATIONS` permission (Android 13+).
- [ ] Use exact alarms (`setExactAndAllowWhileIdle`) and handle `SCHEDULE_EXACT_ALARM` / `USE_EXACT_ALARM`.
- [ ] Reschedule after a reboot, a time or timezone change, and an app update.
- [ ] Notification actions: Done, +5 min, Skip.
- [ ] Test with Doze forced on (`adb shell dumpsys deviceidle force-idle`), and on Samsung or Xiaomi phones if possible.
- [ ] Build it so the watch can later be just another place a nudge gets delivered.

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

- [ ] Unit tests for the Java backbone (ID system, time engine).
- [ ] Room/DAO tests.
- [ ] Flutter widget and integration tests for the UI.
- [ ] Manual checklist:
  - [ ] nudges fire on time with the screen off
  - [ ] nudges still fire after a reboot
  - [ ] a full 24-hour arc (ending at minute 1440) works
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

Calendar import, cloud backup or accounts, home-screen widgets, stats (careful — can turn into shame), iOS, Tags.
