package za.co.smartpantry;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminActivity provides the Admin Control Panel for reviewing registered users,
 * support tickets, and system inventory statistics.
 */
public class AdminActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", true);
        if (isDarkMode) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme_Light);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Apply dynamic background (Pure white in light mode, cyber dark in dark mode)
        View root = findViewById(R.id.rootLayout);
        if (root != null) {
            if (isDarkMode) {
                root.setBackgroundResource(R.drawable.bg_gradient_main);
            } else {
                root.setBackgroundColor(Color.WHITE);
            }
        }

        db = new DatabaseHelper(this);

        ImageButton backBtn = findViewById(R.id.backButton);
        if (backBtn != null) backBtn.setOnClickListener(v -> finish());

        ImageButton themeBtn = findViewById(R.id.themeToggleButton);
        if (themeBtn != null) {
            themeBtn.setOnClickListener(v -> {
                boolean dark = prefs.getBoolean("dark_mode", true);
                prefs.edit().putBoolean("dark_mode", !dark).apply();
                BannerNotification.show(this, "Theme toggled!", BannerNotification.Type.SUCCESS);
                recreate();
            });
        }

        refreshData();
    }

    /**
     * Loads system statistics, users, and support tickets into the admin list view.
     */
    private void refreshData() {
        List<User> users = db.getAllUsers();
        List<SupportTicket> tickets = db.getAllSupportTickets();
        int pantryCount = db.getIngredients().size();

        TextView statUsers = findViewById(R.id.statUsers);
        TextView statTickets = findViewById(R.id.statTickets);
        TextView statPantry = findViewById(R.id.statPantry);

        if (statUsers != null) statUsers.setText("Users: " + users.size());
        if (statTickets != null) statTickets.setText("Tickets: " + tickets.size());
        if (statPantry != null) statPantry.setText("Pantry: " + pantryCount);

        ListView listView = findViewById(R.id.adminListView);
        if (listView != null) {
            List<String> items = new ArrayList<>();
            items.add("=== REGISTERED USERS (" + users.size() + ") ===");
            for (User u : users) {
                items.add("• " + u.fullName + " (" + u.email + ")");
            }
            items.add("");
            items.add("=== SUPPORT TICKETS (" + tickets.size() + ") ===");
            for (SupportTicket t : tickets) {
                items.add("• [" + t.status + "] " + t.userEmail + "\n  Subject: " + t.subject);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, items
            );
            listView.setAdapter(adapter);
        }
    }
}
