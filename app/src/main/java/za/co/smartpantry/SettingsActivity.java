package za.co.smartpantry;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle b) {
        SharedPreferences p = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDarkMode = p.getBoolean("dark_mode", true);
        setTheme(isDarkMode ? R.style.AppTheme_Dark : R.style.AppTheme_Light);

        super.onCreate(b);
        setContentView(R.layout.activity_settings);

        View root = findViewById(android.R.id.content);
        if (root != null) {
            if (isDarkMode) {
                root.setBackgroundResource(R.drawable.bg_gradient_main);
            } else {
                root.setBackgroundColor(Color.WHITE);
            }
        }

        SwitchMaterial expirySwitch = findViewById(R.id.expirySwitch);
        SwitchMaterial themeSwitch = findViewById(R.id.themeSwitch);

        if (expirySwitch != null) expirySwitch.setChecked(p.getBoolean("expiry_alerts", true));
        if (themeSwitch != null) themeSwitch.setChecked(isDarkMode);

        if (expirySwitch != null) {
            expirySwitch.setOnCheckedChangeListener((v, checked) -> {
                p.edit().putBoolean("expiry_alerts", checked).apply();
                BannerNotification.show(this, "Expiry reminders " + (checked ? "enabled" : "disabled"), BannerNotification.Type.INFO);
            });
        }

        if (themeSwitch != null) {
            themeSwitch.setOnCheckedChangeListener((v, checked) -> {
                p.edit().putBoolean("dark_mode", checked).apply();
                BannerNotification.show(this, "Theme updated to " + (checked ? "Dark" : "Light") + "!", BannerNotification.Type.SUCCESS);
                recreate();
            });
        }
    }
}
