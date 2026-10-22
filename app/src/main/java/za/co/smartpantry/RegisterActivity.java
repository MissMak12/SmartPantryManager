package za.co.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private EditText nameInput, emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        nameInput = findViewById(R.id.registerName);
        emailInput = findViewById(R.id.registerEmail);
        passwordInput = findViewById(R.id.registerPassword);

        findViewById(R.id.registerButton).setOnClickListener(v -> performRegistration());
        findViewById(R.id.goToLoginButton).setOnClickListener(v -> finish());
    }

    private void performRegistration() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            BannerNotification.show(this, "Please fill in all fields.", BannerNotification.Type.ERROR);
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            BannerNotification.show(this, "Please enter a valid email address.", BannerNotification.Type.ERROR);
            return;
        }

        if (password.length() < 6) {
            BannerNotification.show(this, "Password must be at least 6 characters.", BannerNotification.Type.ERROR);
            return;
        }

        if (db.getUserByEmail(email) != null) {
            BannerNotification.show(this, "An account with this email already exists.", BannerNotification.Type.ERROR);
            return;
        }

        long userId = db.registerUser(name, email, password);
        if (userId > 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("registered", true);
            startActivity(intent);
            finish();
        } else {
            BannerNotification.show(this, "Account creation failed. Please try again.", BannerNotification.Type.ERROR);
        }
    }
}
