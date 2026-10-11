package za.co.smartpantry;

import java.util.List;
public class Recipe {
    public long id; public String name; public String steps; public List<RecipeIngredient> ingredients;
    public Recipe(long id, String name, String steps, List<RecipeIngredient> ingredients) { this.id=id; this.name=name; this.steps=steps; this.ingredients=ingredients; }
    public static class RecipeIngredient { public String name, unit; public double quantity; public RecipeIngredient(String name,double quantity,String unit){this.name=name;this.quantity=quantity;this.unit=unit;} }
}
