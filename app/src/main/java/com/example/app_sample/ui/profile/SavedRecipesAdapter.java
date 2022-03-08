package com.example.app_sample.ui.profile;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.aitsuki.swipe.SwipeLayout;
import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.Recipes;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SavedRecipesAdapter extends RecyclerView.Adapter<SavedRecipesAdapter.ViewHolder> {

    private List<Recipes.Recipe> recipes;
    private Context context;
    private SavedRecipesFragment fragment;

    public SavedRecipesAdapter(SavedRecipesFragment fragment) {
        this.fragment = fragment;
        this.context = fragment.getContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_saved, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (recipes != null) {
            Recipes.Recipe recipe = recipes.get(position);

            holder.recipe_name.setText(recipe.getTitle());
            String type = recipe.getDishTypes().isEmpty()?"No Type":recipe.getDishTypes().get(0).substring(0,1).toUpperCase(Locale.ROOT) + recipe.getDishTypes().get(0).substring(1);
            holder.meal_type.setText(type);
            RecipeRepository.loadImage(context, recipe.getImage(), holder.img);
            holder.time.setText(recipe.getReadyInMinutes() + " " + context.getResources().getString(R.string.time));
            if(recipe.getColor() == 0){
                int x = new Random().nextInt(7);
                int color = context.getResources().getColor(Filters.MealType.values()[x].color());
                recipe.setColor(color);
                fragment.setRecipeColor(recipe.getId(), color);
            }
            holder.meal_type.setBackgroundTintList(ColorStateList.valueOf(recipe.getColor()));
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!((SwipeLayout)holder.itemView).isRightMenuOpened())
                    fragment.goToRecipePage(recipe);
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.deleteRecipe(recipe.getId());
                    ((SwipeLayout)holder.itemView).closeRightMenu(true);
                }
            });

            View.OnClickListener inGroceries = v -> fragment.deleteFromGroceries(recipe.getId());

            View.OnClickListener notInGroceries = v -> fragment.addToGroceries(recipe);

            fragment.isInGroceries(recipe.getId()).observe(fragment.getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean saved) {
                    if(saved){
                        holder.groceries.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.green)));
                        holder.fix.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.green)));
                        holder.groceriesIcon.setImageResource(R.drawable.ic_clear);
                        holder.groceries.setOnClickListener(inGroceries);
                    }
                    else{
                        holder.groceries.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.sky)));
                        holder.fix.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.sky)));

                        holder.groceriesIcon.setImageResource(R.drawable.ic_add);
                        holder.groceries.setOnClickListener(notInGroceries);
                    }


                }
            });

        }
    }

    @Override
    public int getItemCount() {
        if (recipes != null) return recipes.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img, favorite, groceriesIcon;
        View fix;
        TextView recipe_name, meal_type, time, servings;
        CardView cardView;
        LinearLayout delete, groceries;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            favorite = itemView.findViewById(R.id.clear);
            recipe_name = itemView.findViewById(R.id.recipe_name);
            meal_type = itemView.findViewById(R.id.meal_type);
            time = itemView.findViewById(R.id.time);
            cardView = itemView.findViewById(R.id.cardview);
            delete = itemView.findViewById(R.id.delete);
            groceries = itemView.findViewById(R.id.groceries);
            groceriesIcon = itemView.findViewById(R.id.icon);
            fix = itemView.findViewById(R.id.fix);
        }
    }

    public void setRecipes(List<Recipes.Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }
}
