package com.example.app_sample.ui.search;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;
import com.example.app_sample.ui.MainActivity;
import com.example.app_sample.ui.recipe.RecipeFragment;
import com.example.app_sample.utils.Utils;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Recipes.Recipe> recipes;
    private Context context;
    SearchFragment fragment;
    private final int VIEW_ITEM = 0;
    private final int VIEW_LOAD = 1;

    public ResultsAdapter(Context context, SearchFragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM)
            return new RecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.item_result, parent, false));
        else
            return new LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_results_load_more, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int x = new Random().nextInt(7);

        //todo: add all enums and make colors match
        if (holder instanceof RecipeViewHolder) {
            if (recipes != null) {
                Recipes.Recipe recipe = recipes.get(position);
                RecipeViewHolder recipeHolder = (RecipeViewHolder) holder;

                recipeHolder.recipe_name.setText(recipe.getTitle());
                recipeHolder.meal_type.setText(recipe.getDishTypes().isEmpty() ? "Whatever" : recipe.getDishTypes().get(0));
                RecipesRemoteDataSource.loadImage(context, recipe.getImage(), recipeHolder.img);
                recipeHolder.time.setText(recipe.getReadyInMinutes() + " " + context.getResources().getString(R.string.time));
                recipeHolder.servings.setText(recipe.getServings() + " " + context.getResources().getString(R.string.servings));
                recipeHolder.meal_type.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(Filters.MealType.values()[x].color())));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.goToRecipePage(recipe);
                    }
                });
            }
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.loadMore.setVisibility(View.VISIBLE);
            loadingViewHolder.progress.setVisibility(View.GONE);
            if (recipes != null) {
                if(recipes.size() >= 20){
                    ((LoadingViewHolder) holder).loadMore.setText("Load more results");
                    loadingViewHolder.loadMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadingViewHolder.loadMore.setVisibility(View.INVISIBLE);
                            loadingViewHolder.progress.setVisibility(View.VISIBLE);
                            fragment.doSearch(false);
                        }
                    });
                }
                else if (recipes.size() < 20 && recipes.size() > 0)
                    loadingViewHolder.loadMore.setText("No more results");

                else
                    loadingViewHolder.loadMore.setText("No results");

            }

        }

    }

    @Override
    public int getItemCount() {
        if (recipes != null) return recipes.size();
        else return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return recipes.get(position) == null ? VIEW_LOAD : VIEW_ITEM;
    }

    public void setRecipes(List<Recipes.Recipe> recipes) {
        int length = getItemCount();
        this.recipes = new ArrayList<>(recipes);
        this.recipes.add(null);

        if (recipes.size() <= 20)
            notifyDataSetChanged();
        else {

            notifyItemRangeChanged(length - 1, getItemCount());
        }
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView img, favorite;
        TextView recipe_name, meal_type, time, servings;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            favorite = itemView.findViewById(R.id.favorite);
            recipe_name = itemView.findViewById(R.id.recipe_name);
            meal_type = itemView.findViewById(R.id.meal_type);
            time = itemView.findViewById(R.id.time);
            servings = itemView.findViewById(R.id.servings);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        TextView loadMore;
        CircularProgressIndicator progress;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            loadMore = itemView.findViewById(R.id.load_more);
            progress = itemView.findViewById(R.id.progress);
        }
    }
}
