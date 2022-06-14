package com.example.app_sample.ui.groceries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.GroceryList;
import com.example.app_sample.data.local.models.Ingredient;
import com.example.app_sample.data.local.models.Recipes;

import java.util.ArrayList;

@SuppressLint("NotifyDataSetChanged")
public class GroceriesRecipeAdapter extends RecyclerView.Adapter<GroceriesRecipeAdapter.ViewHolder>{

    private Recipes.Recipe recipe;
    private int servings;
    private GroceriesFragment fragment;
    private ArrayList<Boolean> local;
    private boolean updated;
    private Context context;

    public GroceriesRecipeAdapter(Recipes.Recipe recipe, GroceriesFragment fragment) {
        this.recipe = recipe;
        this.fragment = fragment;
        this.servings = recipe.getServings();
        this.context = fragment.getContext();
        local = new ArrayList<>();
        updated = false;
    }

    @NonNull
    @Override
    public GroceriesRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_grocery_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroceriesRecipeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Ingredient ingredient = recipe.getIngredients().get(position);
        holder.checkBox.setText(toCaps(ingredient.getName()));
        double amount = (ingredient.getAmount()/recipe.getServings())*servings;
        String amountString = toMixedFraction(amount) + " " + ingredient.getUnit();
        holder.amount.setText(amountString);

        if(local.size() > position){
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(local.get(position));
            holder.checkBox.setPaintFlags(local.get(position)? holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG : holder.checkBox.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.checkBox.setTextColor(fragment.getResources().getColor(local.get(position)? R.color.grey : R.color.black));
            updated = false;
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(local.size() > position){
                local.set(position, isChecked);
                updated = true;
                fragment.updateGroceriesList(new GroceryList(recipe.getId(), recipe.getServings(), local));
            }
            holder.checkBox.setPaintFlags(isChecked? holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG : holder.checkBox.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.checkBox.setTextColor(fragment.getResources().getColor(isChecked? R.color.grey : R.color.black));
        });

    }

    @Override
    public int getItemCount() {
        return recipe.getIngredients().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkbox);
            amount = itemView.findViewById(R.id.amount);

        }

    }

    private String toCaps(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
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

    public void updateList(GroceryList gl){
        if(updated)
            updated = false;
        else{
            this.local = gl.getList();
        }
        this.servings = gl.getServings();
        notifyDataSetChanged();
    }


}
