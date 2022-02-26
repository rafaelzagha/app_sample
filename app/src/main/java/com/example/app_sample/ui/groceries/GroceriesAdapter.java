package com.example.app_sample.ui.groceries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.GroceryList;
import com.example.app_sample.data.local.models.Recipes;

import java.util.ArrayList;
import java.util.List;

public class GroceriesAdapter extends RecyclerView.Adapter<GroceriesAdapter.ViewHolder> {

    List<Recipes.Recipe> recipes;
    GroceriesFragment fragment;
    Context context;
    int expandedPosition;
    GroceriesRecipeAdapter[] adapters;

    public GroceriesAdapter(GroceriesFragment fragment) {
        this.fragment = fragment;
        this.context = fragment.getContext();
        this.expandedPosition = -1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_groceries, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        boolean isExpanded = position == expandedPosition;

        if(adapters[position] == null){
            GroceriesRecipeAdapter adapter = new GroceriesRecipeAdapter(recipes.get(position), fragment);
            adapters[position] = adapter;
            holder.list.setAdapter(adapter);
            holder.list.setLayoutManager(new LinearLayoutManager(context));
            holder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("tag", "onclick");
                    int previous = expandedPosition;
                    expandedPosition = position == expandedPosition ? -1 : position;
                    notifyItemChanged(position);
                    if(previous > -1 && previous != position)
                        notifyItemChanged(previous);
                }
            });
            fragment.getGroceryList(recipes.get(position).getId()).observe(fragment.getViewLifecycleOwner(), new Observer<GroceryList>() {
                @Override
                public void onChanged(GroceryList groceryList) {
                    if(groceryList != null){
                        adapter.setLocalList(groceryList.getList());

                        int count = 0;
                        for(boolean i : groceryList.getList()) if(i) count++;
                        String text = count + "/" + groceryList.getList().size() + " " + fragment.getString(R.string.ingredients);
                        holder.ingredients.setText(text);
                    }
                }
            });


            holder.setRecipe(recipes.get(position));
        }


        holder.checklist.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);


    }

    @Override
    public int getItemCount() {
        if (recipes != null)
            return recipes.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img, minus, plus;
        TextView name, ingredients, servings;
        LinearLayout checklist, details;
        RecyclerView list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            minus = itemView.findViewById(R.id.minus);
            plus = itemView.findViewById(R.id.plus);
            name = itemView.findViewById(R.id.recipe_name);
            ingredients = itemView.findViewById(R.id.ingredients);
            servings = itemView.findViewById(R.id.servings);
            checklist = itemView.findViewById(R.id.checklist);
            details = itemView.findViewById(R.id.details);
            list = itemView.findViewById(R.id.ingredient_list);

        }

        public void setRecipe(Recipes.Recipe recipe) {
            RecipeRepository.loadImage(context, recipe.getImage(), img);
            name.setText(recipe.getTitle());
            servings.setText(String.valueOf(recipe.getServings()));
        }
    }

    public void setRecipes(List<Recipes.Recipe> recipes) {
        this.recipes = recipes;
        adapters = new GroceriesRecipeAdapter[recipes.size()];
        notifyDataSetChanged();
    }
}
