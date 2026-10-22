package za.co.smartpantry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private EditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check active session
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        if (prefs.getBoolean("is_logged_in", false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);
        db = new DatabaseHelper(this);

        emailInput = findViewById(R.id.loginEmail);
        passwordInput = findViewById(R.id.loginPassword);

        findViewById(R.id.loginButton).setOnClickListener(v -> performLogin());
        findViewById(R.id.goToRegisterButton).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        // Show welcome info banner if returned from registration
        if (getIntent().getBooleanExtra("registered", false)) {
            BannerNotification.show(this, "Account created successfully! Please sign in.", BannerNotification.Type.SUCCESS);
        }
    }

    private void performLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            BannerNotification.show(this, "Please enter your email and password.", BannerNotification.Type.ERROR);
            return;
        }

        User user = db.authenticateUser(email, password);
        if (user != null) {
            SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
            editor.putBoolean("is_logged_in", true);
            editor.putString("user_email", user.email);
            editor.putString("user_name", user.fullName);
            editor.apply();

            BannerNotification.show(this, "Login successful! Welcome back, " + user.fullName, BannerNotification.Type.SUCCESS);

            emailInput.postDelayed(() -> {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }, 1000);
        } else {
            BannerNotification.show(this, "Login failed. Incorrect email or password.", BannerNotification.Type.ERROR);
        }
    }
}
