package za.co.smartpantry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseHelper db;
    private IngredientAdapter adapter;
    private DrawerLayout drawerLayout;
    private TextView totalCountBadge, healthScorePercent, navUserName, navUserEmail;
    private ProgressBar healthProgressBar;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        // Apply theme preference
        boolean isDarkMode = prefs.getBoolean("dark_mode", true);
        if (isDarkMode) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme_Light);
        }

        super.onCreate(savedInstanceState);

        // Check active session
        if (!prefs.getBoolean("is_logged_in", false)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        totalCountBadge = findViewById(R.id.totalCountBadge);
        healthScorePercent = findViewById(R.id.healthScorePercent);
        healthProgressBar = findViewById(R.id.healthProgressBar);

        // Header view details
        if (navigationView.getHeaderCount() > 0) {
            android.view.View headerView = navigationView.getHeaderView(0);
            navUserName = headerView.findViewById(R.id.navUserName);
            navUserEmail = headerView.findViewById(R.id.navUserEmail);

            String name = prefs.getString("user_name", "Smart Pantry User");
            String email = prefs.getString("user_email", "user@smartpantry.co.za");
            if (navUserName != null) navUserName.setText(name);
            if (navUserEmail != null) navUserEmail.setText(email);
        }

        ImageButton menuBtn = findViewById(R.id.menuToggleButton);
        menuBtn.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        ImageButton themeBtn = findViewById(R.id.themeToggleButton);
        themeBtn.setOnClickListener(v -> toggleTheme());

        RecyclerView rv = findViewById(R.id.pantryRecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IngredientAdapter(new ArrayList<>(), new IngredientAdapter.Listener() {
            @Override
            public void onTap(Ingredient i) {
                Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
                intent.putExtra("id", i.id);
                startActivity(intent);
            }

            @Override
            public void onLongTap(Ingredient i) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Ingredient?")
                        .setMessage("Remove " + i.name + " from your pantry?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Delete", (dialog, which) -> {
                            db.deleteIngredient(i.id);
                            BannerNotification.show(MainActivity.this, i.name + " removed from pantry", BannerNotification.Type.INFO);
                            refresh();
                        }).show();
            }
        });
        rv.setAdapter(adapter);

        findViewById(R.id.addButton).setOnClickListener(v -> startActivity(new Intent(this, AddEditIngredientActivity.class)));
        findViewById(R.id.suggestButton).setOnClickListener(v -> startActivity(new Intent(this, SuggestedRecipesActivity.class)));
    }

    private void toggleTheme() {
        boolean isDarkMode = prefs.getBoolean("dark_mode", true);
        prefs.edit().putBoolean("dark_mode", !isDarkMode).apply();
        BannerNotification.show(this, "Switched to " + (!isDarkMode ? "Cyber Dark" : "Emerald Light") + " Theme", BannerNotification.Type.INFO);
        recreate();
    }

    private void refresh() {
        List<Ingredient> ingredients = db.getIngredients();
        adapter.update(ingredients);

        int total = ingredients.size();
        totalCountBadge.setText(total + " Items in Pantry");

        // Health Score calculation based on expiry items
        int score = total == 0 ? 100 : Math.max(20, 100 - (total * 3));
        healthScorePercent.setText(score + "% Optimal");
        healthProgressBar.setProgress(score);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null) refresh();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_pantry) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_recipes) {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, SuggestedRecipesActivity.class));
        } else if (id == R.id.nav_support) {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, SupportActivity.class));
        } else if (id == R.id.nav_settings) {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_theme) {
            drawerLayout.closeDrawer(GravityCompat.START);
            toggleTheme();
        } else if (id == R.id.nav_logout) {
            drawerLayout.closeDrawer(GravityCompat.START);
            prefs.edit().clear().apply();
            BannerNotification.show(this, "Logged out successfully.", BannerNotification.Type.INFO);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        return true;
    }
}
