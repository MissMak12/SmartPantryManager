# Smart Pantry Manager

Repository: [https://github.com/MissMak12/SmartPantryManager.git](https://github.com/MissMak12/SmartPantryManager.git)

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

## Development & Commit History
This project was developed through 10 genuine incremental commits by **MissMak12** (`zandilemakhanya12@gmail.com`):
1. **Initial Project Setup & Gradle Configuration**: Setup root and app-level Gradle build scripts, settings, and project structure.
2. **Android Manifest & Application Setup**: Configured AndroidManifest.xml, application theme, activities, and permissions.
3. **Core Data Models**: Implemented `Ingredient.java` and `Recipe.java` data classes.
4. **SQLite Database Helper**: Implemented `DatabaseHelper.java` with local persistence, CRUD operations, and 20 seeded recipes.
5. **UI Layout Resources**: Created XML layouts (`activity_main.xml`, `activity_list.xml`, `activity_add_edit.xml`, `activity_recipe_detail.xml`, `activity_settings.xml`, `item_ingredient.xml`).
6. **RecyclerView Adapters, Activities & Android Manifest**: Implemented `IngredientAdapter.java`, `RecipeAdapter.java`, and core activities (`MainActivity`, `AddEditIngredientActivity`, `RecipeDetailActivity`, `SuggestedRecipesActivity`, `SettingsActivity`).
7. **Pantry Inventory Management**: Integrated main activity inventory views and interactions.
8. **Ingredient Add/Edit & Recipe Detail Flows**: Enabled adding, editing, deleting ingredients and viewing detailed recipe steps.
9. **Strict Recipe Matching Engine**: Implemented logic in `SuggestedRecipesActivity` ensuring recipes only appear when all required ingredients are in stock with sufficient quantity.
10. **Documentation & Final Polish**: Comprehensive README documentation and repository configuration.

## Open and run
1. Install Android Studio with Android SDK 35 and a compatible JDK.
2. Open this project folder in Android Studio.
3. Allow Gradle to sync and install any prompted SDK components.
4. Run the `app` configuration on an emulator or Android device.

## Testing strict matching
Start with an empty pantry and open **Find Recipes**: no recipes should be suggested. Add every ingredient and sufficient quantity for a recipe (for example 2 pieces of egg, 2 pieces of tomato and 2 pinches of salt) and reopen suggestions. Remove or reduce any required item below the required amount; the recipe must disappear.

## Database model
- `ingredients(id, name, quantity, unit, expiry)`
- `recipes(id, name, steps)`
- `recipe_ingredients(id, recipe_id, name, quantity, unit)`

SQLite was selected because this is a single-device coursework prototype and SQLiteOpenHelper supports offline persistence without a hosted backend.

## Limitations / next steps
Ingredient synonyms and cross-unit conversions are deliberately limited. For example, this prototype recognises basic singular/plural forms and selected unit aliases but does not convert grams to kilograms or millilitres to litres. Expiry-alert preference is stored, but operating-system notifications are not implemented. Verify build and runtime behaviour in your own Android Studio environment before submission.
