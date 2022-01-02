package com.example.app_sample.ui.home.search;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;

import java.util.List;
import java.util.Random;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {

    private List<Recipes.Recipe> recipes;
    private Context context;

    public ResultsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_result, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

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
        if(recipes != null) return recipes.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img, favorite;
        TextView recipe_name, meal_type, time, servings;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            favorite = itemView.findViewById(R.id.favorite);
            recipe_name = itemView.findViewById(R.id.recipe_name);
            meal_type = itemView.findViewById(R.id.meal_type);
            time = itemView.findViewById(R.id.time);
            servings = itemView.findViewById(R.id.servings);
        }
    }

    public void setRecipes(List<Recipes.Recipe> recipes) {
        if(this.recipes == null){
            this.recipes = recipes;
        }
        else
            this.recipes.addAll(recipes);

        notifyDataSetChanged();
    }
}
