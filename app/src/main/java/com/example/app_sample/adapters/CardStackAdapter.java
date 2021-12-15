package com.example.app_sample.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.app_sample.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.models.Category;

import java.util.List;


public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.CardViewHolder>   {

    List<Category> categoryList;

    public CardStackAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CardStackAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_item, parent, false);
        /*
        ViewGroup.LayoutParams p = view.getLayoutParams();
        p.height = 450;
        view.setLayoutParams(p);
         */

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardStackAdapter.CardViewHolder holder, int position) {
        holder.txt.setText(categoryList.get(position).getName());
        holder.img.setImageResource(categoryList.get(position).getImgId());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        TextView txt;
        ImageView img;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);
            img = itemView.findViewById(R.id.img);
        }
    }
}
