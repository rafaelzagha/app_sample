package com.example.app_sample.ui.home.swipe;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.Recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("FieldCanBeLocal")
public class CardStackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Recipes.Recipe> recipes;
    private final SwipeFragment swipeFragment;
    private final Context context;
    private final int LAYOUT_CARDVIEW = 0;
    private final int LAYOUT_LOADMORE = 1;


    public CardStackAdapter(SwipeFragment swipeFragment) {
        this.swipeFragment = swipeFragment;
        context = swipeFragment.getContext();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == LAYOUT_CARDVIEW)
            return new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_swipe, parent, false));
        else
            return new LoadMoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_swipe_load_more, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(recipes.get(position) != null){
            Recipes.Recipe recipe = recipes.get(position);
            CardViewHolder cardViewHolder = (CardViewHolder)holder;

            holder.itemView.setOnClickListener(v -> swipeFragment.goToRecipePage(recipe));
            cardViewHolder.recipe_name.setText(recipe.getTitle());
            String type = recipe.getDishTypes().isEmpty()?"No Type":toCaps(recipe.getDishTypes().get(0));
            cardViewHolder.meal_type.setText(type);
            RecipeRepository.loadImage(context, recipe.getImage(), cardViewHolder.img );
            String time = recipe.getReadyInMinutes() + " " + context.getResources().getString(R.string.time);
            String servings = recipe.getServings() + " " + context.getResources().getString(R.string.servings);
            cardViewHolder.time.setText(time);
            cardViewHolder.servings.setText(servings);

            if(recipe.getColor() == 0){
                int x = new Random().nextInt(7);
                recipe.setColor(context.getResources().getColor(Filters.MealType.values()[x].color()));
            }
            cardViewHolder.meal_type.setBackgroundTintList(ColorStateList.valueOf(recipe.getColor()));
        }
        else{
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(v -> swipeFragment.loadMore());
        }

    }

    @Override
    public int getItemCount() {
        if (recipes != null)
            return recipes.size();
        else return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(recipes.get(position) != null)
            return LAYOUT_CARDVIEW;
        else
            return LAYOUT_LOADMORE;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

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

    public static class LoadMoreViewHolder extends RecyclerView.ViewHolder{

        CardView loadMore;

        public LoadMoreViewHolder(@NonNull View itemView) {
            super(itemView);
            loadMore = itemView.findViewById(R.id.load_more);
        }
    }

    private String toCaps(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public void setRecipes(List<Recipes.Recipe> recipes) {
        int length = getItemCount();
        this.recipes = new ArrayList<>(recipes);
        if(!this.recipes.contains(null))
            this.recipes.add(null);

        notifyItemRangeChanged(length, getItemCount());
    }

    public Recipes.Recipe getRecipe(int position){
        return recipes.get(position);
    }

}
