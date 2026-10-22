package za.co.smartpantry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SupportActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private EditText aiQueryInput, subjectInput, messageInput;
    private Spinner categorySpinner;
    private TextView aiResponseText;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        db = new DatabaseHelper(this);
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        userEmail = prefs.getString("user_email", "guest@smartpantry.co.za");

        aiQueryInput = findViewById(R.id.aiQueryInput);
        subjectInput = findViewById(R.id.ticketSubjectInput);
        messageInput = findViewById(R.id.ticketMessageInput);
        categorySpinner = findViewById(R.id.ticketCategorySpinner);
        aiResponseText = findViewById(R.id.aiResponseText);

        // Populate Category Spinner
        String[] categories = {"General Inquiry", "Recipe Matching Bug", "Expiry Alerts", "Account Support", "Feature Suggestion"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(adapter);

        findViewById(R.id.aiAskButton).setOnClickListener(v -> processAiQuery());
        findViewById(R.id.submitTicketButton).setOnClickListener(v -> submitTicket());
    }

    private void processAiQuery() {
        String query = aiQueryInput.getText().toString().trim().toLowerCase();
        if (query.isEmpty()) {
            BannerNotification.show(this, "Please enter a question for AI Assistant.", BannerNotification.Type.WARNING);
            return;
        }

        aiResponseText.setVisibility(View.VISIBLE);
        if (query.contains("recipe") || query.contains("match")) {
            aiResponseText.setText("🤖 AI Answer: Recipes are strictly matched! Every required ingredient and quantity must be present in your pantry.");
        } else if (query.contains("expiry") || query.contains("date")) {
            aiResponseText.setText("🤖 AI Answer: Expiry dates use YYYY-MM-DD format. Items show neon tags: Fresh (Green), Expiring Soon (Orange), or Expired (Red).");
        } else if (query.contains("unit") || query.contains("convert")) {
            aiResponseText.setText("🤖 AI Answer: Compatible units like 'pieces/pc', 'g/grams', and 'ml/milliliters' are automatically recognized!");
        } else {
            aiResponseText.setText("🤖 AI Answer: Smart Pantry AI operates on-device. If your question requires human support, please submit a ticket below!");
        }

        BannerNotification.show(this, "AI Assistant answer generated!", BannerNotification.Type.SUCCESS);
    }

    private void submitTicket() {
        String subject = subjectInput.getText().toString().trim();
        String message = messageInput.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();

        if (subject.isEmpty() || message.isEmpty()) {
            BannerNotification.show(this, "Please fill in both subject and message.", BannerNotification.Type.ERROR);
            return;
        }

        long ticketId = db.createSupportTicket(userEmail, category, subject, message);
        if (ticketId > 0) {
            BannerNotification.show(this, "Support ticket #" + ticketId + " submitted successfully!", BannerNotification.Type.SUCCESS);
            subjectInput.setText("");
            messageInput.setText("");
        } else {
            BannerNotification.show(this, "Failed to submit ticket. Please try again.", BannerNotification.Type.ERROR);
        }
    }
}
