# Smart Pantry Manager

A Java Android application that stores pantry ingredients locally and suggests only recipes for which every required ingredient is present in sufficient quantity.

## Technology
- Java and Android Studio
- SQLite via `SQLiteOpenHelper` for persistent local storage
- AndroidX RecyclerView with custom adapters
- XML layouts and explicit Intents

## Features
- Create, read, update and delete pantry ingredients
- Optional expiry date and input validation
- 20 recipes seeded automatically on first database creation
- Strict recipe matching (no partial recipes in the suggested list)
- Recipe detail screen with required ingredients and method
- Settings preference for expiry reminders
- Local persistence across app restarts

## Recent Updates & Fixes
- **Layout Preview Render Fix**: Resolved Android Studio Layoutlib inflation error (`assetStream is null`) by replacing non-standard `fontFamily` definition with standard Android `sans-serif` in `styles.xml`.
- **Repository Commit Modularization**: Organized codebase across 7 structured, logical commits for enhanced code visibility and modular history tracking.

## Repository Commit Breakdown (7 Commits)
1. `chore: initial gradle build and project configuration` — Gradle build scripts, properties, wrapper, and Android SDK 35 configuration.
2. `feat: add core data models for Ingredient and Recipe` — Domain model classes for ingredients and recipes.
3. `feat: implement SQLite database helper and seed recipes` — SQLite schema, 20 default seeded recipes, CRUD operations, and strict ingredient matching logic.
4. `style: add Material theme styling, strings and layout preview fix` — Color palette, Material Components theme, string resources, and layout preview font family fix.
5. `feat: add XML layout views for all app screens` — XML layout definitions for Main, Add/Edit Ingredient, Recipe List, Recipe Details, Settings, and RecyclerView item layouts.
6. `feat: add RecyclerView adapters, Activities and Android Manifest` — Activity controllers, RecyclerView adapters, explicit Intent navigation, and manifest declarations.
7. `docs: update README with recent fixes and commit structure` — Project documentation, setup guide, database model, and commit history reference.

## Open and run
1. Install Android Studio with Android SDK 35 and a compatible JDK.
2. Open this project folder in Android Studio.
3. Allow Gradle to sync and install any prompted SDK components.
4. Run the `app` configuration on an emulator or Android device.

## Testing strict matching
Start with an empty pantry and open **Find Recipes**: no recipes should be suggested. Add every ingredient and sufficient quantity for a recipe (for example 2 pieces of egg, 2 pieces of tomato and 1 pinch of salt) and reopen suggestions. Remove or reduce any required item below the required amount; the recipe must disappear.

## Database model
- `ingredients(id, name, quantity, unit, expiry)`
- `recipes(id, name, steps)`
- `recipe_ingredients(id, recipe_id, name, quantity, unit)`

SQLite was selected because this is a single-device coursework prototype and SQLiteOpenHelper supports offline persistence without a hosted backend.

## Limitations / next steps
Ingredient synonyms and cross-unit conversions are deliberately limited. For example, this prototype recognises basic singular/plural forms and selected unit aliases but does not convert grams to kilograms or millilitres to litres. Expiry-alert preference is stored, but operating-system notifications are not implemented. Verify build and runtime behaviour in your own Android Studio environment before submission.
