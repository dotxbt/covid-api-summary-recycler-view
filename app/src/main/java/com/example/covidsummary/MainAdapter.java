package com.example.covidsummary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ModelHolder> {
    private ArrayList<ItemModel> items, dataFilter;
    private Context context;
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
        holder.tvItemData.setText("Country : " + item.getCountry() + "\n"
                + "Total Death : " + item.getTotalDeaths() + "\n"
                + "Total Confirmed : " + item.getTotalConfirmed());

        holder.itemView.setOnClickListener( v -> {
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
            notifyDataSetChanged();
        } else {
            this.items= new ArrayList<>();
            for (ItemModel item : this.dataFilter) {
                // filter here
                if (item.toString().toLowerCase().contains(keyWord.toLowerCase())) {
                    this.items.add(item);
                }
            }
            notifyDataSetChanged();
        }
    }

    // simple holder
    public class ModelHolder extends RecyclerView.ViewHolder {
        TextView tvItemData;
        public ModelHolder(@NonNull View itemView) {
            super(itemView);
            tvItemData = itemView.findViewById(R.id.item_data);
        }
    }

    interface OnItemClickListener {
        void onClick(ItemModel itemModel);
    }
}
