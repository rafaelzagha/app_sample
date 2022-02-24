package com.example.app_sample.ui.groceries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.GroceriesList;
import com.example.app_sample.data.local.models.Ingredient;
import com.example.app_sample.data.local.models.Recipes;

import java.util.List;

public class GroceriesRecipeAdapter extends RecyclerView.Adapter<GroceriesRecipeAdapter.ViewHolder>{

    Recipes.Recipe recipe;
    GroceriesList groceriesList;
    Context context;

    public GroceriesRecipeAdapter(Recipes.Recipe recipe, Context context) {
        this.recipe = recipe;
        this.context = context;
    }

    @NonNull
    @Override
    public GroceriesRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroceriesRecipeAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_grocery_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroceriesRecipeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Ingredient ingredient = recipe.getIngredients().get(position);
        holder.checkBox.setText(ingredient.getName());
        String amount = toMixedFraction(ingredient.getAmount()) + " " + ingredient.getUnit();
        holder.amount.setText(amount);

    }

    @Override
    public int getItemCount() {
        return recipe.getIngredients().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkbox);
            amount = itemView.findViewById(R.id.amount);

        }

    }

    private String toMixedFraction(double x) {
        int whole = (int) x;
        int denominator = 64;
        int numerator = (int) ((x - whole) * denominator);

        if (numerator == 0) {
            return String.valueOf(whole);
        }
        while (numerator % 2 == 0) // simplify fraction
        {
            numerator /= 2;
            denominator /= 2;
        }
        return String.format("%s %s/%s", whole == 0? "" : whole, numerator, denominator);
    }
}
