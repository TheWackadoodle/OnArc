# Settings Component

Planning notes for OnArc's **Settings Component**: the Settings screen, the data behind it, and the code that saves it.
Written so this still makes sense months from now.

---

## Where we're at

| | |
|---|---|
| **Phase** | Planning (nothing built yet) |
| **Done** | Final list of settings, data model, file map, what's-new rules, uninstall rule |
| **Next** | Build `Settings.java` (what's-new version only), with `SettingsTest.java` |
| **Blocked on** | Nothing for the `shared/` part. The `app/` part waits for README step 3 (Storage). |

_Last updated: 2026-11-06_

---

## Quick glossary

- **`shared/`**: the plain-Java module. No Android imports. The watch reuses it later. `Arc.java` and `DayLoop.java` live here.
- **`app/`**: the phone app module. Anything Android (screens, notifications, saving to the phone) lives here.
- **Field**: a variable that belongs to an object (data).
- **Method**: something an object can do (behaviour). Methods aren't saved; fields are.
- **Enum**: a type with a fixed, named list of values, e.g. `DayOfWeek`. Typos don't compile.
- **Storage layer**: phone-side code that saves and loads. The Room database (plans), a small key-value store (settings), and a repository class the rest of the app goes through.
- **Repository**: the one class the app talks to for loading and saving. The UI never touches storage directly (main README, step 3).

---

## The Settings screen (final order)

1. Sound Settings
2. Backup / export
3. Restore / import
4. Clear all data
5. Reset all settings
6. Send feedback / report a bug
7. About / version info
8. Privacy policy link

Items cut from this screen during planning were moved to the Customization and Dials screens (tracked separately) or dropped.

### What each item actually is

| # | Item | Stored data? | What it is |
|---|---|---|---|
| 1 | Sound Settings | **No (parked)** | Design placeholder only. Ignore for now. |
| 2 | Backup / export | No | Action: save everything to a file the user picks |
| 3 | Restore / import | No | Action: load everything from a file the user picks |
| 4 | Clear all data | No | Action: wipes settings **and** plans |
| 5 | Reset all settings | No | Action: settings back to defaults. Plans untouched. |
| 6 | Send feedback | No | Button that opens a web address |
| 7 | About / version info | No | Button that shows the version, read from the build |
| 8 | Privacy policy | No | Button that opens a web address |

Plus one value with no item of its own: **last what's-new version seen**.

---

## Requirements (the data structure must…)

- Hold every setting
- Give every setting a default
- Let you read any setting
- Let you change any setting
- Reset all settings to defaults
- Reject invalid values when they're set, not just at creation
- Never hold null
- Convert to a savable form and back
- Fall back to the default for any missing or broken saved value
- Allow new settings later without breaking saved ones
- Never let linked settings contradict each other

---

## Decisions made

### Data shape
- `Settings` is **mutable**. Changes go through setters that validate first and throw `IllegalArgumentException` on junk, the same style as `Arc`.
- Small bundle classes (if any appear later) are **immutable**, like `Arc`.
- Fixed choices are **enums**, not Strings.
- Enums are **saved by name, never by position number**, so reordering the values in code can't scramble saved data.

### What's new rules
- Stored: **the last app version the user saw "what's new" for.**
- On launch, compare it with the app's current `versionCode` (the number in `app/build.gradle.kts`, not the "1.0" text). Current is higher → show what's new, then mark it seen.
- Fresh install: set it to the current version immediately, so new users don't get a what's new for an app they've never used.
- The content (changelog text, demo screens) lives in `app/` resources, not in `Settings`.

### Confirmations
- Reset all settings and Clear all data each get **multiple** "Are you sure?" steps.
- **No** "Don't ask again" option, so nothing to store.
- The confirmation steps are screen-only state. Nothing saved.

### Uninstalling
- **Uninstalling deletes everything.** Reinstall = brand-new user.
- **Housekeeping needed:** the manifest currently has `android:allowBackup="true"`, which lets Android restore data on reinstall. Set it to `false`.
- **To verify when doing it:** on Android 12+, `allowBackup="false"` stops cloud backup but may still allow phone-to-phone transfer. `res/xml/data_extraction_rules.xml` may also need its `<device-transfer>` section set to exclude everything.
- Manual Backup / export (item 2) is unaffected. That's a file the user chose to save.

### Backup / restore approach
- Use Android's built-in "save a file where I choose" / "open a file" screens (the Storage Access Framework). Files saved there aren't the app's, so they survive an uninstall.
- Backups must include the day plans, not just settings.

### App info, links
- Version is read from the build at runtime. Never stored, so it can't go stale.
- Feedback and privacy addresses go in `app/src/main/res/values/strings.xml`. UI only, no class.

### Parked
- **Sound Settings.** Conceptual placeholder. If it comes back:
  - volume is 0–100 in steps of 5, as a fraction of the phone's current volume
  - OnArc would play its own sounds (the notification itself stays silent)
  - everything sound-related goes in one place, so it's easy to scrap

---

## File map

```
OnArc/
├── shared/src/main/java/com/onarc/shared/
│   ├── Settings.java
│   │     Fields:
│   │       lastWhatsNewVersionSeen
│   │     Methods:
│   │       create with defaults
│   │       resetToDefaults()
│   │       getLastWhatsNewVersionSeen()
│   │       markWhatsNewSeen(version)
│   │       shouldShowWhatsNew(currentVersion)
│   │
│   └── SettingsConverter.java       (later, with JSON)
│           toText(settings)
│           fromText(text)   → anything missing or broken falls back to defaults
│
├── shared/src/test/java/com/onarc/shared/   (Claude's)
│   ├── SettingsTest.java
│   └── SettingsConverterTest.java   (later)
│
└── app/src/main/
    ├── java/com/onarc/app/
    │   ├── SettingsRepository.java
    │   │       loadSettings()   → no saved settings = first install, use defaults
    │   │       saveSettings(settings)
    │   │       resetAllSettings()  → asks Settings to reset, then saves
    │   │       clearAllData()      → wipes settings and plans
    │   │       backupTo(file the user picked)
    │   │       restoreFrom(file the user picked)
    │   │
    │   └── SettingsActivity.java
    │           the screen with the 8 items
    │           the multiple "Are you sure?" steps for Reset and Clear
    │           opens the feedback and privacy links
    │           shows the version (read from the build)
    │           opens what's new
    │
    ├── res/layout/activity_settings.xml
    ├── res/values/strings.xml
    │       feedback URL, privacy URL, all screen text
    ├── res/raw/   (or similar)
    │       what's new content per version, format TBD
    ├── AndroidManifest.xml
    │       allowBackup → false
    └── res/xml/data_extraction_rules.xml
            exclude device-to-device transfer (verify)
```

Not in the map on purpose: sound settings (parked), the what's-new screen itself (its own component), a stored copy of the version (comes from the build).

---

## Open questions (answer before building)

1. **Where does the default "last what's-new version seen" come from?** Only `app/` knows the current version, and `shared/` can't read the build. Options:
   - (a) `Settings` is created with the current version passed in.
   - (b) `Settings` starts at 0 and the repository sets it on first install.
2. **What does "Reset all settings" do to the what's-new version?** Leaving it alone is probably right, so a reset doesn't trigger an old what's new.
3. **Settings storage: SharedPreferences or DataStore?** DataStore is Google's current recommendation, but it's built around Kotlin. From Java it needs its RxJava version. SharedPreferences is simpler from Java.
4. **What's-new content format?** Plain text per version, or something richer? Decide when the what's-new screen is planned.

---

## Build order

1. `Settings.java` with the what's-new field and methods. Claude writes `SettingsTest.java` alongside it.
2. **Wait for main README step 3 (Storage).**
3. `SettingsRepository.java`: load, save, reset, clear.
4. Settings screen + layout + strings.
5. Backup / restore + `SettingsConverter.java` (together with the main README's JSON serialization).
6. Housekeeping: `allowBackup="false"` and data extraction rules.

---

## Change log format (STRICT)

**Every agentic coder (Claude or any other AI tool) must follow these rules exactly, every time this file changes.** Humans should follow them too. No exceptions, no "close enough".

### Rules
1. **Every change to this file gets a change log entry.** A typo fix too.
2. **Newest entry goes at the top** of the Change log section.
3. **Never edit or delete an old entry.** If an old entry is wrong, add a new entry that corrects it and names the date of the one it corrects.
4. **One entry per working session.** Several changes in the same session go in the same entry as separate bullets.
5. **Update the "Where we're at" table and its `Last updated` date in the same change.** Mention it in the entry.
6. **Dates are `YYYY-MM-DD`.** Nothing else.
7. **Group bullets by README section,** using the section's exact heading name.
8. **Each bullet says what changed and why**, in plain English, one line where possible. "Updated stuff" is not allowed.
9. **Sign off with who made the change:** `-Claude-` for Claude, `-Ya Boi-` or `-MS-` for Matthew, or the tool's or person's name for anyone else.
10. **No model names or version identifiers** in entries. Just the sign-off.
11. **Decisions get recorded as decisions.** If a bullet records a choice, say what was chosen and what it replaced, if anything.
12. **Don't touch code-facing sections (File map, Build order) without an entry explaining the effect on the code.**

### Template
```
### YYYY-MM-DD
* Section Name:
- What changed, and why

* Another Section Name:
- What changed, and why

-Sign-off-
```

### Example
```
### 2026-12-01
* Open questions:
- Answered #2: Reset leaves the what's-new version alone, so a reset never replays an old what's new
- Moved #2 to Decisions made

* Decisions made:
- Added "Reset leaves the what's-new version alone" (was an open question)

* Where we're at:
- Updated Next and Last updated

-Claude-
```

---

## Change log

### 2026-11-06
* All sections:
- Created this README from the planning session
- Settings list finalised; data model, what's-new rules, confirmations, uninstall rule and file map decided
- Sound Settings parked as a design placeholder
- Tutorial planned, then dropped from the app for now (too much friction to build); all tutorial items, rules, files and questions removed. Settings list is 8 items

* Decisions made:
- Added "enums are saved by name, never by position number" (came out of the TutorialStatus review, still applies to future enums)

* Change log format (STRICT):
- Added strict change log rules, template and example for all agentic coders

* Where we're at:
- Set initial status and Last updated

-Claude-
