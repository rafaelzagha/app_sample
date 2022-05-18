package com.example.app_sample.ui.search;

import android.annotation.SuppressLint;
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
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.Recipes;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@SuppressWarnings("FieldCanBeLocal")
@SuppressLint("NotifyDataSetChanged")
public class ResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Recipes.Recipe> recipes;
    private final Context context;
    private final SearchFragment fragment;
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

        if (holder instanceof RecipeViewHolder) {
            if (recipes != null) {
                Recipes.Recipe recipe = recipes.get(position);
                RecipeViewHolder recipeHolder = (RecipeViewHolder) holder;

                recipeHolder.recipe_name.setText(recipe.getTitle());
                String type = recipe.getDishTypes().isEmpty() ? "No Type" : recipe.getDishTypes().get(0).substring(0, 1).toUpperCase(Locale.ROOT) + recipe.getDishTypes().get(0).substring(1);
                recipeHolder.meal_type.setText(type);
                RecipeRepository.loadImage(context, recipe.getImage(), recipeHolder.img);
                String time = recipe.getReadyInMinutes() + " " + context.getResources().getString(R.string.time);
                String servings = recipe.getServings() + " " + context.getResources().getString(R.string.servings);
                recipeHolder.time.setText(time);
                recipeHolder.servings.setText(servings);
                if (recipe.getColor() == 0) {
                    int x = new Random().nextInt(7);
                    recipe.setColor(context.getResources().getColor(Filters.MealType.values()[x].color()));
                }
                recipeHolder.meal_type.setBackgroundTintList(ColorStateList.valueOf(recipe.getColor()));
                holder.itemView.setOnClickListener(v -> fragment.goToRecipePage(recipe));
            }
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.loadMore.setVisibility(View.VISIBLE);
            loadingViewHolder.progress.setVisibility(View.GONE);
            if (recipes != null) {
                if ((recipes.size() - 1) % 20 == 0) {
                    if (recipes.size() > 1) {
                        loadingViewHolder.loadMore.setText(R.string.load_more_results);
                        loadingViewHolder.loadMore.setOnClickListener(v -> {
                            loadingViewHolder.loadMore.setVisibility(View.INVISIBLE);
                            loadingViewHolder.progress.setVisibility(View.VISIBLE);
                            fragment.doSearch(false);
                        });
                    } else {
                        loadingViewHolder.loadMore.setText(R.string.no_results);
                        loadingViewHolder.loadMore.setOnClickListener(null);
                    }

                } else {
                    loadingViewHolder.loadMore.setText(R.string.no_more_results);
                    loadingViewHolder.loadMore.setOnClickListener(null);
                }

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
            favorite = itemView.findViewById(R.id.clear);
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
