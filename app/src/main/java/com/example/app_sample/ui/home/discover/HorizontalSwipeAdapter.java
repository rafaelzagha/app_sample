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
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Filter;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;

public class HorizontalSwipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Filters.MealType[] items;
    public List<Recipes.Recipe> recipes;
    private Context context;
    private final LayoutInflater layoutInflater;
    private final int layout;
    DiscoverFragment fragment;

    public static final int LAYOUT_RECIPE = 0;
    public static final int LAYOUT_MEALTYPE = 1;
    private static final int LAYOUT_LOAD_MORE = 2;


    public HorizontalSwipeAdapter(Context context, int layout, DiscoverFragment fragment) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.layout = layout;
        this.recipes = null;
        this.fragment = fragment;
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
            return new LoadMoreViewHolder(layoutInflater.inflate(R.layout.item_popular_load_more, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (layout == LAYOUT_RECIPE) {
            if (recipes != null) {
                if(getItemViewType(position) == LAYOUT_RECIPE){
                    RecipeViewHolder viewHolder = (RecipeViewHolder) holder;
                    Recipes.Recipe recipe = recipes.get(position);
                    viewHolder.name.setText(recipe.getTitle());
                    viewHolder.type.setText(recipe.getDishTypes().isEmpty() ? "No Type" : recipe.getDishTypes().get(0));
                    RecipeRepository.loadImage(context, recipe.getImage(), viewHolder.img);
                    viewHolder.time.setText(recipe.getReadyInMinutes() + " " + context.getResources().getString(R.string.time));
                    viewHolder.servings.setText(recipe.getServings() + " " + context.getResources().getString(R.string.servings));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fragment.goToRecipePage(recipe);
                        }
                    });
                }
                else{
                    LoadMoreViewHolder viewHolder = (LoadMoreViewHolder) holder;
                    viewHolder.loadMore.setVisibility(View.VISIBLE);
                    viewHolder.indicator.setVisibility(View.INVISIBLE);
                    viewHolder.loadMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fragment.newRequest();
                            viewHolder.loadMore.setVisibility(View.INVISIBLE);
                            viewHolder.indicator.setVisibility(View.VISIBLE);
                        }
                    });
                }

            }
        } else {
            MealTypeViewHolder viewHolder = (MealTypeViewHolder) holder;
            Filters.MealType type = items[position];
            viewHolder.tv.setText(type.name());
            viewHolder.img.setImageResource(type.img());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Filter> filter = new ArrayList<>();
                    filter.add(type);
                    fragment.goToSearchScreen(null, filter);
                }
            });
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

    private static class LoadMoreViewHolder extends RecyclerView.ViewHolder{

        TextView loadMore;
        CircularProgressIndicator indicator;

        public LoadMoreViewHolder(@NonNull View itemView) {
            super(itemView);
            loadMore = itemView.findViewById(R.id.tv_load_more);
            indicator = itemView.findViewById(R.id.indicator);
        }
    }

    public void setRecipes(List<Recipes.Recipe> recipes) {
        int length = getItemCount();
        this.recipes = new ArrayList<>(recipes);
        if(!recipes.isEmpty()) this.recipes.add(null);
        notifyItemRangeChanged(length, getItemCount());
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
