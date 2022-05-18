package com.example.app_sample.ui.home.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Filter;
import com.example.app_sample.data.local.models.Filters;

import java.util.ArrayList;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private final Context context;
    private final Filters.Cuisine[] cuisines;
    DiscoverFragment fragment;

    public GridAdapter(DiscoverFragment fragment) {
        this.context = fragment.getContext();
        this.fragment = fragment;
        this.cuisines = Filters.Cuisine.values();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cuisine, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Filters.Cuisine cuisine = cuisines[position];
        holder.img.setImageResource(cuisine.img());
        holder.name.setText(cuisine.name());

        holder.itemView.setOnClickListener(v -> {
            ArrayList<Filter> filter = new ArrayList<>();
            filter.add(cuisine);
            fragment.goToSearchScreen(null, filter);
        });
    }

    @Override
    public int getItemCount() {
        return cuisines.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_dish);
            name = itemView.findViewById(R.id.category_name);
        }
    }
}
