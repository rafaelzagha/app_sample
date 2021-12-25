package com.example.app_sample.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.app_sample.R;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.models.Category;
import com.example.app_sample.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.CardViewHolder>   {

    List<Category> categoryList;
    Context context;
    ArrayList<Integer> colors;


    public CardStackAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
        this.colors = Utils.getColors();
    }

    @NonNull
    @Override
    public CardStackAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stack, parent, false);
        /*
        ViewGroup.LayoutParams p = view.getLayoutParams();
        p.height = 450;
        view.setLayoutParams(p);
         */

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardStackAdapter.CardViewHolder holder, int position) {
        //holder.txt.setText(categoryList.get(position).getName());
        Collections.shuffle(colors);
        holder.meal_type.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(colors.get(0))));;
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        TextView meal_type, time, servings, recipe_name ;
        ImageView img;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            meal_type = itemView.findViewById(R.id.meal_type);
            time = itemView.findViewById(R.id.time);
            servings = itemView.findViewById(R.id.servings);
            recipe_name = itemView.findViewById(R.id.recipe_name);
            img = itemView.findViewById(R.id.img);
        }
    }
}
