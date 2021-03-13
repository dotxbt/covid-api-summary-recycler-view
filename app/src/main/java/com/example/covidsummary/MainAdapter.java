package com.example.covidsummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
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
    private int currentTop =  -1;
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

    public void removeAllItems() {
        this.items = new ArrayList<>();
        this.dataFilter = new ArrayList<>();
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
        if (position > currentTop) {
            currentTop = position;
            AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(300);
            holder.itemView.startAnimation(animation);
        }

        ItemModel item = this.items.get(position);
        holder.tvCountry.setText(item.getCountry());
        holder.tvDeath.setText(formatNumber(item.getTotalDeaths()));
        holder.tvConfirmed.setText(formatNumber(item.getTotalConfirmed()));
        holder.tvRecovered.setText(formatNumber(item.getTotalRecovered()));

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

    private String formatNumber(int number) {
        return String.format("%,d",number).replace(',', '.');
    }

    interface OnItemClickListener {
        void onClick(ItemModel itemModel);
    }
}
