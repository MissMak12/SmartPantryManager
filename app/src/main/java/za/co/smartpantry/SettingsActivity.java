package za.co.smartpantry;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_settings);

        SwitchMaterial expirySwitch = findViewById(R.id.expirySwitch);
        SwitchMaterial themeSwitch = findViewById(R.id.themeSwitch);

        SharedPreferences p = getSharedPreferences("settings", MODE_PRIVATE);
        expirySwitch.setChecked(p.getBoolean("expiry_alerts", true));
        themeSwitch.setChecked(p.getBoolean("dark_mode", true));

        expirySwitch.setOnCheckedChangeListener((v, checked) -> {
            p.edit().putBoolean("expiry_alerts", checked).apply();
            BannerNotification.show(this, "Expiry reminders " + (checked ? "enabled" : "disabled"), BannerNotification.Type.INFO);
        });

        themeSwitch.setOnCheckedChangeListener((v, checked) -> {
            p.edit().putBoolean("dark_mode", checked).apply();
            BannerNotification.show(this, "Theme updated! Restart screen to apply.", BannerNotification.Type.SUCCESS);
        });
    }
}
