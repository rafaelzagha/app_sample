package com.example.app_sample.ui.profile.cookbooks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.utils.MyItemDetail;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.List;
import java.util.Locale;

@SuppressLint("NotifyDataSetChanged")
public class CookbookRecipesAdapter extends RecyclerView.Adapter<CookbookRecipesAdapter.ViewHolder> {

    private List<Recipes.Recipe> recipes;
    private final Context context;
    private final CookbookPageFragment fragment;
    private SelectionTracker<Long> selectionTracker;

    public CookbookRecipesAdapter(CookbookPageFragment fragment) {
        this.fragment = fragment;
        this.context = fragment.getContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recipe_cookbook, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (recipes != null) {
            Recipes.Recipe recipe = recipes.get(position);

            holder.recipe_name.setText(recipe.getTitle());
            String type = recipe.getDishTypes().isEmpty() ? "No Type" : recipe.getDishTypes().get(0).substring(0, 1).toUpperCase(Locale.ROOT) + recipe.getDishTypes().get(0).substring(1);
            holder.meal_type.setText(type);
            RecipeRepository.loadImage(context, recipe.getImage(), holder.img);
            String time = recipe.getReadyInMinutes() + " " + context.getResources().getString(R.string.time);
            holder.time.setText(time);

            try {
                holder.meal_type.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, recipe.getColor())));
            } catch (Exception e) {
                holder.meal_type.setBackgroundTintList(ColorStateList.valueOf(recipe.getColor()));
            }

            holder.cardView.setOnClickListener(v -> fragment.goToRecipePage(recipe));

            if (selectionTracker != null) {
                boolean selected = selectionTracker.hasSelection();

                holder.checkBox.setVisibility(selected ? View.VISIBLE : View.GONE);
                holder.meal_type.setVisibility(selected ? View.GONE : View.VISIBLE);
                holder.go.setVisibility(selected ? View.GONE : View.VISIBLE);
                holder.time.setVisibility(selected ? View.GONE : View.VISIBLE);

                holder.checkBox.setChecked(selectionTracker.isSelected(holder.getItemDetails().getSelectionKey()));
            }

        }
    }


    @Override
    public int getItemCount() {
        if (recipes != null) return recipes.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img, favorite, go;
        TextView recipe_name, meal_type, time;
        CardView cardView;
        CustomCheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            favorite = itemView.findViewById(R.id.clear);
            recipe_name = itemView.findViewById(R.id.recipe_name);
            meal_type = itemView.findViewById(R.id.meal_type);
            time = itemView.findViewById(R.id.time);
            cardView = itemView.findViewById(R.id.cardview);
            checkBox = itemView.findViewById(R.id.checkbox);
            go = itemView.findViewById(R.id.go);
        }

        public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
            return new MyItemDetail(getAdapterPosition(), recipes.get(getAdapterPosition()).getId());
        }
    }

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    public void setRecipes(List<Recipes.Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public List<Recipes.Recipe> getRecipes() {
        return recipes;
    }
}
