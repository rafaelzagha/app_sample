package com.example.app_sample.ui.home.swipe;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_sample.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;

import java.util.List;
import java.util.Random;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.CardViewHolder> {

    List<Recipes.Recipe> recipes;
    SwipeFragment swipeFragment;
    Context context;


    public CardStackAdapter(Context context, SwipeFragment swipeFragment) {
        this.swipeFragment = swipeFragment;
        this.context = context;
    }

    @NonNull
    @Override
    public CardStackAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_swipe, parent, false);
        return new CardViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull CardStackAdapter.CardViewHolder holder, int position) {
        //holder.txt.setText(categoryList.get(position).getName());
        Recipes.Recipe recipe = recipes.get(position);

        if (recipes != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeFragment.goToRecipePage(recipe);
                }
            });
            holder.recipe_name.setText(recipe.getTitle());
            holder.meal_type.setText(recipe.getDishTypes().isEmpty()?"Whatever":recipe.getDishTypes().get(0));
            RecipeRepository.loadImage(context, recipe.getImage(), holder.img );
            holder.time.setText(recipe.getReadyInMinutes() + " " + context.getResources().getString(R.string.time));
            holder.servings.setText(recipe.getServings() + " " + context.getResources().getString(R.string.servings));

            if(recipe.getColor() == 0){
                int x = new Random().nextInt(7);
                recipe.setColor(context.getResources().getColor(Filters.MealType.values()[x].color()));
            }
            holder.meal_type.setBackgroundTintList(ColorStateList.valueOf(recipe.getColor()));

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
        int length = getItemCount();
        this.recipes = recipes;
        notifyItemRangeChanged(length, getItemCount());
    }

    public Recipes.Recipe getRecipe(int position){
        return recipes.get(position);
    }

}
