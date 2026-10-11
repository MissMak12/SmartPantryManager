package za.co.smartpantry;
import android.content.*;import android.os.Bundle;import android.view.View;import android.widget.*;import androidx.appcompat.app.AppCompatActivity;import androidx.recyclerview.widget.*;
public class SuggestedRecipesActivity extends AppCompatActivity
{
    @Override protected void onCreate(Bundle b){super.onCreate(b);
        setContentView(R.layout.activity_list);
        ((TextView)findViewById(R.id.screenTitle)).setText("Suggested Recipes");
        RecyclerView rv=findViewById(R.id.listRecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        DatabaseHelper db=new DatabaseHelper(this);
        java.util.List<Recipe> recipes=db.getSuggestedRecipes();
        rv.setAdapter(new RecipeAdapter(recipes,r->{Intent i=new Intent(this,RecipeDetailActivity.class);
            i.putExtra("recipeId",r.id);
            startActivity(i);}));
        TextView empty=findViewById(R.id.emptyText);
        empty.setVisibility(recipes.isEmpty()?View.VISIBLE:View.GONE);}}
