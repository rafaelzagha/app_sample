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
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.utils.MyItemDetail;
import com.google.android.material.button.MaterialButton;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.List;

public class GroceriesAdapter extends RecyclerView.Adapter<GroceriesAdapter.ViewHolder> {

    private List<Recipes.Recipe> recipes;
    private final GroceriesFragment fragment;
    private final Context context;
    private int expandedPosition;
    private GroceriesRecipeAdapter[] adapters;
    private int[] servings;
    private SelectionTracker<Long> selectionTracker;

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

        if (adapters.length > position) {
            Recipes.Recipe recipe = recipes.get(position);

            GroceriesRecipeAdapter adapter = new GroceriesRecipeAdapter(recipe, fragment);
            adapters[position] = adapter;
            holder.list.setAdapter(adapter);
            holder.list.setLayoutManager(new LinearLayoutManager(context));


            holder.details.setOnClickListener(v -> {
                int previous = expandedPosition;
                expandedPosition = position == expandedPosition ? -1 : position;
                notifyItemChanged(position);
                if (previous > -1 && previous != position)
                    notifyItemChanged(previous);
            });

            fragment.getGroceryList(recipe.getId()).observe(fragment.getViewLifecycleOwner(), groceryList -> {
                if (groceryList != null && adapters.length > position && adapters[position] != null) {
                    adapters[position].updateList(groceryList);
                    int count = 0;
                    for (boolean i : groceryList.getList()) if (i) count++;
                    String text = count + "/" + groceryList.getList().size() + " " + fragment.getString(R.string.ingredients);
                    holder.ingredients.setText(text);
                    if (servings.length > position) {
                        servings[position] = groceryList.getServings();
                        holder.servings.setText(String.valueOf(servings[position]));
                    }
                }
            });

            holder.servings.setText(String.valueOf(servings[position]));

            holder.options.setOnClickListener(v -> fragment.goToRecipePage(recipe));

            holder.plus.setOnClickListener(v -> {
                if (servings[position] != 100) {
                    servings[position]++;
                    fragment.updateGroceryServings(recipe.getId(), servings[position]);
                }
            });

            holder.minus.setOnClickListener(v -> {
                if (servings[position] != 1) {
                    servings[position]--;
                    fragment.updateGroceryServings(recipe.getId(), servings[position]);
                }
            });

            holder.details.setOnClickListener(v -> {
                int previous = expandedPosition;
                expandedPosition = position == expandedPosition ? -1 : position;
                notifyItemChanged(position);
                if (previous > -1 && previous != position)
                    notifyItemChanged(previous);
            });

            holder.setRecipe(recipe);
            boolean isExpanded = position == expandedPosition;
            boolean selected = selectionTracker.hasSelection();

            holder.checkBox.setVisibility(selected ? View.VISIBLE : View.GONE);
            holder.checkBox.setChecked(selectionTracker.isSelected(holder.getItemDetails().getSelectionKey()));
            holder.plus.setVisibility(selected ? View.GONE : View.VISIBLE);
            holder.minus.setVisibility(selected ? View.GONE : View.VISIBLE);
            holder.servings.setVisibility(selected ? View.GONE : View.VISIBLE);
            holder.ingredients.setVisibility(selected ? View.GONE : View.VISIBLE);

            holder.checklist.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.itemView.setActivated(isExpanded);
        }
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
        CustomCheckBox checkBox;

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
            checkBox = itemView.findViewById(R.id.checkbox);

        }

        public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
            return new MyItemDetail(getBindingAdapterPosition(), recipes.get(getBindingAdapterPosition()).getId());
        }

        public void setRecipe(Recipes.Recipe recipe) {
            RecipeRepository.loadImage(context, recipe.getImage(), img);
            name.setText(recipe.getTitle());
            if (GroceriesAdapter.this.servings.length > getBindingAdapterPosition() && GroceriesAdapter.this.servings[getBindingAdapterPosition()] != 0)
                servings.setText(String.valueOf(GroceriesAdapter.this.servings[getBindingAdapterPosition()]));
            else
                servings.setText(String.valueOf(recipe.getServings()));
        }

    }

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    public void setRecipes(List<Recipes.Recipe> recipes) {
        this.recipes = recipes;
        adapters = new GroceriesRecipeAdapter[recipes.size()];
        servings = new int[recipes.size()];
        notifyDataSetChanged();
    }

    public void reset() {
        adapters = new GroceriesRecipeAdapter[recipes.size()];
        servings = new int[recipes.size()];
        expandedPosition = -1;
        notifyDataSetChanged();
    }

    public List<Recipes.Recipe> getRecipes() {
        return recipes;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
