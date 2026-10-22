package za.co.smartpantry;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private long id = -1;
    private EditText name, quantity, unit, expiry;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_add_edit);

        db = new DatabaseHelper(this);
        name = findViewById(R.id.nameInput);
        quantity = findViewById(R.id.quantityInput);
        unit = findViewById(R.id.unitInput);
        expiry = findViewById(R.id.expiryInput);

        id = getIntent().getLongExtra("id", -1);
        if (id > 0) {
            Ingredient i = db.getIngredient(id);
            if (i != null) {
                ((TextView) findViewById(R.id.formTitle)).setText("Edit Ingredient");
                name.setText(i.name);
                quantity.setText(String.valueOf(i.quantity));
                unit.setText(i.unit);
                expiry.setText(i.expiry);
            }
        }

        findViewById(R.id.saveButton).setOnClickListener(v -> save());
    }

    private void save() {
        String n = name.getText().toString().trim();
        String q = quantity.getText().toString().trim();
        String u = unit.getText().toString().trim();
        String e = expiry.getText().toString().trim();

        if (n.isEmpty() || q.isEmpty() || u.isEmpty()) {
            BannerNotification.show(this, "Ingredient name, quantity and unit are required.", BannerNotification.Type.ERROR);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(q);
            if (!Double.isFinite(amount) || amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            BannerNotification.show(this, "Enter a quantity greater than zero.", BannerNotification.Type.ERROR);
            return;
        }

        if (!e.isEmpty() && !e.matches("\\d{4}-\\d{2}-\\d{2}")) {
            BannerNotification.show(this, "Use YYYY-MM-DD date format.", BannerNotification.Type.ERROR);
            return;
        }

        long saved = db.saveIngredient(new Ingredient(id, n, amount, u, e));
        if (saved < 0) {
            BannerNotification.show(this, "Could not save ingredient.", BannerNotification.Type.ERROR);
        } else {
            BannerNotification.show(this, n + " saved to pantry!", BannerNotification.Type.SUCCESS);
            name.postDelayed(this::finish, 800);
        }
    }
}
