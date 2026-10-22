package za.co.smartpantry;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.Holder> {
    public interface Listener {
        void open(Recipe r);
    }

    private List<Recipe> data;
    private Listener listener;

    public RecipeAdapter(List<Recipe> d, Listener l) {
        data = d;
        listener = l;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup p, int t) {
        TextView v = new TextView(p.getContext());
        v.setPadding(18, 22, 18, 22);
        v.setTextSize(17);
        v.setTextColor(0xff1b4332);
        v.setBackgroundColor(0xffffffff);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(-1, -2);
        lp.topMargin = 8;
        v.setLayoutParams(lp);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        Recipe r = data.get(pos);
        h.title.setText(r.name + "\n" + r.ingredients.size() + " ingredients \u2022 Tap to view method");
        h.title.setOnClickListener(v -> listener.open(r));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView title;

        Holder(View v) {
            super(v);
            title = (TextView) v;
        }
    }
}
