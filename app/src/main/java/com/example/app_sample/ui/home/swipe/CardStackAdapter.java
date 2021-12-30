package com.example.app_sample.ui.home.swipe;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_sample.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;

import java.util.List;
import java.util.Random;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.CardViewHolder> {

    List<Recipes.Recipe> recipes;
    Context context;


    public CardStackAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardStackAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stack, parent, false);
        return new CardViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull CardStackAdapter.CardViewHolder holder, int position) {
        //holder.txt.setText(categoryList.get(position).getName());
        int x = new Random().nextInt(7);
        Recipes.Recipe recipe = recipes.get(position);

        //todo: add all enums and make colors match
        if (recipes != null) {
            holder.recipe_name.setText(recipe.getTitle());
            holder.meal_type.setText(recipe.getDishTypes().isEmpty()?"Whatever":recipe.getDishTypes().get(0));
            RecipesRemoteDataSource.loadImage(context, recipe.getImage(), holder.img );
            holder.time.setText(recipe.getReadyInMinutes() + " " + context.getResources().getString(R.string.time));
            holder.servings.setText(recipe.getServings() + " " + context.getResources().getString(R.string.servings));

            holder.meal_type.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(Filters.MealType.values()[x].color())));



        }
    }

    @Override
    public int getItemCount() {
        if (recipes != null)
            return recipes.size();
        else return 0;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        TextView meal_type, time, servings, recipe_name;
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

    public void setRecipes(List<Recipes.Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();

    }

}
