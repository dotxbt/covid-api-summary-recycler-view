package com.example.covidsummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ModelHolder> {
    private ArrayList<ItemModel> items, dataFilter;
    private Context context;
    private boolean isASC=true;
    private OnItemClickListener listener = m -> {
      // do nothing
    };

    public MainAdapter(Context context) {
        this.items = new ArrayList<>();
        this.dataFilter = new ArrayList<>();
        this.context = context;
    }

    public void addItem(ItemModel itemModel) {
        this.items.add(itemModel);
        this.dataFilter.add(itemModel);
        notifyItemInserted(this.items.size() -1);
    }

    public void updateItem(ItemModel model, int position) {
        this.items.set(position, model);
        this.dataFilter.set(position, model);
        notifyItemChanged(position);
    }

    public void deleteItem(int position) {
        this.items.remove(position);
        this.dataFilter.remove(position);
        notifyItemRemoved(position);
        notifyItemMoved(position, this.items.size() - 1);
    }

    public void sortData(boolean isASC) {
        this.isASC = isASC;
        if (isASC) {
            Collections.sort(items, new Comparator<ItemModel>() {
                @Override
                public int compare(ItemModel o1, ItemModel o2) {
                    return o1.getCountry().compareTo(o2.getCountry());
                }
            });
        } else {
            Collections.sort(items, new Comparator<ItemModel>() {
                @Override
                public int compare(ItemModel o1, ItemModel o2) {
                    return o2.getCountry().compareTo(o1.getCountry());
                }
            });
        }
        notifyDataSetChanged();
    }

    public void addOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ModelHolder(LayoutInflater.from(context).inflate(R.layout.item_model_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ModelHolder holder, int position) {
        ItemModel item = this.items.get(position);
        holder.tvCountry.setText(item.getCountry());
        holder.tvDeath.setText(String.valueOf(item.getTotalDeaths()));
        holder.tvConfirmed.setText(String.valueOf(item.getTotalConfirmed()));
        holder.tvRecovered.setText(String.valueOf(item.getTotalRecovered()));

        holder.itemClick.setOnClickListener( v -> {
            listener.onClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void filterData(String keyWord) {
        if (keyWord.length() == 0) {
            this.items = this.dataFilter;
        } else {
            this.items= new ArrayList<>();
            for (ItemModel item : this.dataFilter) {
                // filter here
                if (item.toString().toLowerCase().contains(keyWord.toLowerCase())) {
                    this.items.add(item);
                }
            }
        }
        sortData(isASC);
    }

    // simple holder
    public class ModelHolder extends RecyclerView.ViewHolder {
        TextView tvCountry, tvDeath, tvConfirmed, tvRecovered;
        LinearLayout itemClick;
        public ModelHolder(@NonNull View itemView) {
            super(itemView);
            itemClick = itemView.findViewById(R.id.item_click);
            tvCountry = itemView.findViewById(R.id.country);
            tvDeath = itemView.findViewById(R.id.death);
            tvConfirmed = itemView.findViewById(R.id.confirmed);
            tvRecovered = itemView.findViewById(R.id.recovered);
        }
    }

    interface OnItemClickListener {
        void onClick(ItemModel itemModel);
    }
}
