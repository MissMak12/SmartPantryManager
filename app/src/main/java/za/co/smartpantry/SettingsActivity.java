package za.co.smartpantry;
import android.os.Bundle;import android.content.SharedPreferences;import android.widget.Switch;import androidx.appcompat.app.AppCompatActivity;
public class SettingsActivity extends AppCompatActivity{@Override protected void onCreate(Bundle b){super.onCreate(b);
    setContentView(R.layout.activity_settings);
    Switch s=findViewById(R.id.expirySwitch);
    SharedPreferences p=getSharedPreferences("settings",MODE_PRIVATE);
    s.setChecked(p.getBoolean("expiry_alerts",true));
    s.setOnCheckedChangeListener((v,checked)->p.edit().putBoolean("expiry_alerts",checked).apply());}}
