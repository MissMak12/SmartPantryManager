package za.co.smartpantry;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.Holder> {

    public interface Listener {
        void onTap(Ingredient i);
        void onLongTap(Ingredient i);
    }

    private List<Ingredient> data;
    private final Listener listener;

    public IngredientAdapter(List<Ingredient> d, Listener l) {
        data = d;
        listener = l;
    }

    public void update(List<Ingredient> d) {
        data = d;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup p, int t) {
        View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_ingredient, p, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        Ingredient i = data.get(pos);
        h.name.setText(i.name);
        h.qty.setText(i.quantity + " " + i.unit);

        if (i.expiry == null || i.expiry.trim().isEmpty()) {
            h.expiryBadge.setText("Fresh");
            h.expiryBadge.setBackgroundResource(R.drawable.bg_badge_fresh);
            h.expiryBadge.setTextColor(Color.parseColor("#38EF7D"));
        } else {
            h.expiryBadge.setText("Expires " + i.expiry);
            if (i.expiry.compareTo("2026-10-25") < 0) {
                h.expiryBadge.setText("Expiring");
                h.expiryBadge.setBackgroundResource(R.drawable.bg_badge_warning);
                h.expiryBadge.setTextColor(Color.parseColor("#F2994A"));
            } else {
                h.expiryBadge.setBackgroundResource(R.drawable.bg_badge_fresh);
                h.expiryBadge.setTextColor(Color.parseColor("#38EF7D"));
            }
        }

        h.itemView.setOnClickListener(v -> listener.onTap(i));
        h.itemView.setOnLongClickListener(v -> {
            listener.onLongTap(i);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView name, qty, expiryBadge;

        Holder(View v) {
            super(v);
            name = v.findViewById(R.id.itemName);
            qty = v.findViewById(R.id.itemQuantity);
            expiryBadge = v.findViewById(R.id.itemExpiryBadge);
        }
    }
}
