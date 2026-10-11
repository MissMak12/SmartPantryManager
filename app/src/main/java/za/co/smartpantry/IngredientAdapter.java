package za.co.smartpantry;
import android.view.*;import android.widget.*;import androidx.annotation.NonNull;import androidx.recyclerview.widget.RecyclerView;import java.util.*;
public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.Holder>{public interface Listener{void onTap(Ingredient i);
    void onLongTap(Ingredient i);
}
private List<Ingredient> data;
    private Listener listener;public IngredientAdapter(List<Ingredient>d,Listener l){data=d;listener=l;
    }

    public void update(List<Ingredient>d){data=d;notifyDataSetChanged();
    }
    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup p,int t){View v=LayoutInflater.from(p.getContext()).inflate(R.layout.item_ingredient,p,false);
        return new Holder(v);}@Override public void onBindViewHolder(@NonNull Holder h,int pos){Ingredient i=data.get(pos);h.name.setText(i.name);
        h.qty.setText(i.quantity+" "+i.unit+(i.expiry==null||i.expiry.isEmpty()?"":" • Expires "+i.expiry));
        h.itemView.setOnClickListener(v->listener.onTap(i));h.itemView.setOnLongClickListener(v->{listener.onLongTap(i);return true;
        }
        );
    }
    @Override public int getItemCount(){return data.size();
    }
    static class Holder extends RecyclerView.ViewHolder{TextView name,qty;

    Holder(View v){super(v);name=v.findViewById(R.id.itemName);
        qty=v.findViewById(R.id.itemQuantity);
    }
    }
}
