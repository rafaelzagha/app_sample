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
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.GroceryList;
import com.example.app_sample.data.local.models.Recipes;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class GroceriesAdapter extends RecyclerView.Adapter<GroceriesAdapter.ViewHolder> {

    List<Recipes.Recipe> recipes;
    GroceriesFragment fragment;
    Context context;
    int expandedPosition;
    GroceriesRecipeAdapter[] adapters;
    int[] servings;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        boolean isExpanded = position == expandedPosition;

        //todo: servings calculator
        if (adapters.length > position && adapters[position] == null) {//do all this only once
            Recipes.Recipe recipe = recipes.get(position);
            GroceriesRecipeAdapter adapter = new GroceriesRecipeAdapter(recipe, fragment);
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
                    if (previous > -1 && previous != position)
                        notifyItemChanged(previous);
                }
            });

            BottomSheetDialog dialog = new BottomSheetDialog(context);
            dialog.setContentView(R.layout.dialog_groceries);
            dialog.setCancelable(true);
            holder.options.setOnClickListener(v -> dialog.show());

            LinearLayout goToPage = dialog.findViewById(R.id.go_to_page);
            LinearLayout deleteList = dialog.findViewById(R.id.delete_list);
            LinearLayout clearList = dialog.findViewById(R.id.clear_list);

            goToPage.setOnClickListener(v -> {
                fragment.goToRecipePage(recipe);
                        dialog.dismiss();
            });
            deleteList.setOnClickListener(v -> {
                fragment.deleteGroceryList(recipe.getId());
                dialog.dismiss();
            });
            clearList.setOnClickListener( v -> {
                fragment.updateGroceriesList(new GroceryList(recipe.getId(),recipe.getServings(), recipe.getIngredients().size()));
                dialog.dismiss();
            });

            fragment.getGroceryList(recipes.get(position).getId()).observe(fragment.getViewLifecycleOwner(), new Observer<GroceryList>() {
                @Override
                public void onChanged(GroceryList groceryList) {
                    if (groceryList != null) {
                        adapter.setLocalList(groceryList.getList());
                        int count = 0;
                        for (boolean i : groceryList.getList()) if (i) count++;
                        String text = count + "/" + groceryList.getList().size() + " " + fragment.getString(R.string.ingredients);
                        holder.ingredients.setText(text);
                        servings[position] = groceryList.getServings();
                        holder.servings.setText(String.valueOf(servings[position]));
                        adapter.setServings(groceryList.getServings());
                    }
                }
            });

            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(servings[position] != 100){
                        servings[position] ++;
                        fragment.updateGroceryServings(recipe.getId(), servings[position]);
                    }
                }
            });

            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(servings[position] != 1){
                        servings[position]--;
                        fragment.updateGroceryServings(recipe.getId(), servings[position]);
                    }
                }
            });

            holder.setRecipe(recipe);
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

        ImageView img, options;
        MaterialButton minus, plus;
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
            options = itemView.findViewById(R.id.options);

        }

        public void setRecipe(Recipes.Recipe recipe) {
            RecipeRepository.loadImage(context, recipe.getImage(), img);
            name.setText(recipe.getTitle());
            servings.setText(String.valueOf(recipe.getServings()));

        }
    }

    public void setRecipes(List<Recipes.Recipe> recipes) {
        adapters = new GroceriesRecipeAdapter[recipes.size()];
        servings = new int[recipes.size()];
        this.recipes = recipes;
        notifyDataSetChanged();
    }
}
