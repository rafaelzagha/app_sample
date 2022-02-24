package com.example.app_sample.ui.groceries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.GroceriesList;
import com.example.app_sample.data.local.models.Recipes;

import java.util.List;

public class GroceriesAdapter extends RecyclerView.Adapter<GroceriesAdapter.ViewHolder> {

    List<Recipes.Recipe> recipes;
    Context context;
    int expandedPosition;

    public GroceriesAdapter(Context context) {
        this.context = context;
        this.expandedPosition = -1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_groceries, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        GroceriesRecipeAdapter adapter = new GroceriesRecipeAdapter(recipes.get(position), context);
        holder.list.setAdapter(adapter);
        holder.list.setLayoutManager(new LinearLayoutManager(context));
        holder.setRecipe(recipes.get(position));

        final boolean isExpanded = position == expandedPosition;
        holder.checklist.setVisibility(isExpanded? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expandedPosition > -1)
                    notifyItemChanged(expandedPosition);
                expandedPosition = isExpanded? -1 : position;
                notifyItemChanged(position);
            }
        });



    }

    @Override
    public int getItemCount() {
        if(recipes != null)
            return recipes.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img, minus, plus;
        TextView name, ingredients, servings;
        LinearLayout checklist;
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
            list = itemView.findViewById(R.id.ingredient_list);

        }

        public void setRecipe(Recipes.Recipe recipe){
            RecipeRepository.loadImage(context, recipe.getImage(), img);
            name.setText(recipe.getTitle());
            servings.setText(String.valueOf(recipe.getServings()));
        }
    }

    public void setRecipes(List<Recipes.Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }
}
