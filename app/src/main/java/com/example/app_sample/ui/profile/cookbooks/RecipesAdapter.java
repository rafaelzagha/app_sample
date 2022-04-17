package com.example.app_sample.ui.profile.cookbooks;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.aitsuki.swipe.SwipeLayout;
import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.utils.MyItemDetail;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.List;
import java.util.Locale;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private List<Recipes.Recipe> recipes;
    private SelectionTracker selectionTracker;
    private Context context;

    public RecipesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_saved_simple, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipes.Recipe recipe = recipes.get(position);

        holder.recipe_name.setText(recipe.getTitle());
        RecipeRepository.loadImage(context, recipe.getImage(), holder.img);

        holder.checkBox.setChecked(selectionTracker.isSelected(holder.getItemDetails().getSelectionKey()));
        holder.checkBox.setOnClickListener(null);

        if(!selectionTracker.hasSelection()){
            holder.itemView.setOnClickListener(v -> selectionTracker.select(holder.getItemDetails().getSelectionKey()));
        }
        else{
            holder.itemView.setOnClickListener(null);
        }

    }


    @Override
    public int getItemCount() {
        if (recipes != null) return recipes.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView recipe_name;
        CustomCheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            recipe_name = itemView.findViewById(R.id.recipe_name);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

        public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
            return new MyItemDetail(getAdapterPosition(), recipes.get(getAdapterPosition()).getId());
        }
    }

    public List<Recipes.Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipes.Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public void setSelectionTracker(SelectionTracker selectionTracker) {
        this.selectionTracker = selectionTracker;
    }
}
