package za.co.smartpantry;
import android.app.*;import android.content.*;import android.os.Bundle;import android.widget.*;import androidx.appcompat.app.AppCompatActivity;import androidx.recyclerview.widget.*;import java.util.*;
public class MainActivity extends AppCompatActivity{DatabaseHelper db;IngredientAdapter adapter;
    TextView count;
    @Override protected void onCreate(Bundle b){super.onCreate(b);
        setContentView(R.layout.activity_main);
        db=new DatabaseHelper(this);
        count=findViewById(R.id.countText);
        RecyclerView rv=findViewById(R.id.pantryRecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new IngredientAdapter(new ArrayList<>(),new IngredientAdapter.Listener(){public void onTap(Ingredient i){Intent x=new Intent(MainActivity.this,AddEditIngredientActivity.class);
            x.putExtra("id",i.id);startActivity(x);
        }public void onLongTap(Ingredient i){new AlertDialog.Builder(MainActivity.this).setTitle("Delete ingredient?").setMessage("Remove "+i.name+" from your pantry?").setNegativeButton("Cancel",null).setPositiveButton("Delete",(d,w)->{db.deleteIngredient(i.id);
            refresh();
        }).show();}});
        rv.setAdapter(adapter);
        findViewById(R.id.addButton).setOnClickListener(v->startActivity(new Intent(this,AddEditIngredientActivity.class)));
        findViewById(R.id.suggestButton).setOnClickListener(v->startActivity(new Intent(this,SuggestedRecipesActivity.class)));
        findViewById(R.id.settingsButton).setOnClickListener(v->startActivity(new Intent(this,SettingsActivity.class)));
    }
    private void refresh(){List<Ingredient> a=db.getIngredients();
        adapter.update(a);
        count.setText(a.size()+" ingredients");
    }
    @Override protected void onResume(){super.onResume();
        if(db!=null)refresh();}}

