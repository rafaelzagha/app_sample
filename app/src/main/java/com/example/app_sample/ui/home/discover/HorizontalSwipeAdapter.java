package com.example.app_sample.ui.home.discover;

import android.content.Context;
import android.text.Layout;
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
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

public class HorizontalSwipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Filters.MealType[] items;
    public List<Recipes.Recipe> recipes;
    private Context context;
    private final LayoutInflater layoutInflater;
    private final int layout;

    public static final int LAYOUT_RECIPE = 0;
    public static final int LAYOUT_MEALTYPE = 1;
    private static final int LAYOUT_LOAD_MORE = 2;


    public HorizontalSwipeAdapter(Context context, int layout) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.layout = layout;
        this.recipes = null;
        this.items = layout == LAYOUT_MEALTYPE ? Filters.MealType.values() : null;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LAYOUT_RECIPE)
            return new RecipeViewHolder(layoutInflater.inflate(R.layout.item_popular, parent, false));
        else if( viewType == LAYOUT_MEALTYPE)
            return new MealTypeViewHolder(layoutInflater.inflate(R.layout.item_meal_type, parent, false));
        else{
            //todo: create load more viewholder
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (layout == LAYOUT_RECIPE) {
            if (recipes != null) {
                RecipeViewHolder viewHolder = (RecipeViewHolder) holder;
                Recipes.Recipe recipe = recipes.get(position);
                viewHolder.name.setText(recipe.getTitle());
                viewHolder.type.setText(recipe.getDishTypes().isEmpty() ? "Whatever" : recipe.getDishTypes().get(0));
                RecipesRemoteDataSource.loadImage(context, recipe.getImage(), viewHolder.img);
                viewHolder.time.setText(recipe.getReadyInMinutes() + " " + context.getResources().getString(R.string.time));
                viewHolder.servings.setText(recipe.getServings() + " " + context.getResources().getString(R.string.servings));
            }
        } else {
            MealTypeViewHolder viewHolder = (MealTypeViewHolder) holder;
            Filters.MealType type = items[position];
            viewHolder.tv.setText(type.name());
            viewHolder.img.setImageResource(type.img());
        }
    }


    @Override
    public int getItemCount() {
        if (layout == LAYOUT_MEALTYPE) return items.length;
        else return recipes != null ? recipes.size() : 0;
    }

    private static class MealTypeViewHolder extends RecyclerView.ViewHolder {

        private TextView tv;
        private ImageView img;

        public MealTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_cat);
            img = itemView.findViewById(R.id.img_cat);

        }
    }

    private static class RecipeViewHolder extends RecyclerView.ViewHolder {

        private TextView name, time, servings, type;
        private ImageView img;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            img = itemView.findViewById(R.id.imageView);
            time = itemView.findViewById(R.id.tv_time);
            servings = itemView.findViewById(R.id.tv_servings);
            type = itemView.findViewById(R.id.tv_type);

        }
    }

    public void setRecipes(List<Recipes.Recipe> recipes) {
        this.recipes = new ArrayList<>(recipes);
        if(!recipes.isEmpty()) this.recipes.add(null);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (layout == LAYOUT_RECIPE) {

            if (recipes.get(position) != null)
                return LAYOUT_RECIPE;
            else
                return LAYOUT_LOAD_MORE;
        }
        else return LAYOUT_MEALTYPE;
    }

}
