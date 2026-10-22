package za.co.smartpantry;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_recipe_detail);

        Recipe r = new DatabaseHelper(this).getRecipe(getIntent().getLongExtra("recipeId", -1));
        if (r == null) {
            finish();
            return;
        }

        ((TextView) findViewById(R.id.recipeTitle)).setText(r.name);

        StringBuilder ing = new StringBuilder();
        StringBuilder steps = new StringBuilder("1. ")
                .append(r.steps)
                .append("\n\n2. Check that all ingredients are cooked safely and thoroughly.\n\n3. Serve immediately.");

        for (Recipe.RecipeIngredient i : r.ingredients) {
            ing.append("• ")
                    .append(i.quantity)
                    .append(" ")
                    .append(i.unit)
                    .append(" ")
                    .append(i.name)
                    .append("\n");
        }

        ((TextView) findViewById(R.id.recipeIngredients)).setText(ing.toString());
        ((TextView) findViewById(R.id.recipeSteps)).setText(steps.toString());
    }
}
