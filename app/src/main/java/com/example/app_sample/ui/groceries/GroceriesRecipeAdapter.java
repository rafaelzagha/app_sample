package com.example.app_sample.ui.groceries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.aitsuki.swipe.SwipeLayout;
import com.example.app_sample.R;
import com.example.app_sample.data.local.models.GroceryList;
import com.example.app_sample.data.local.models.Ingredient;
import com.example.app_sample.data.local.models.Recipes;

import java.util.ArrayList;
import java.util.Collections;

public class GroceriesRecipeAdapter extends RecyclerView.Adapter<GroceriesRecipeAdapter.ViewHolder>{

    Recipes.Recipe recipe;
    int servings;
    GroceriesFragment fragment;
    ArrayList<Boolean> local;
    boolean updated;
    Context context;

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
        return new GroceriesRecipeAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_grocery_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroceriesRecipeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Ingredient ingredient = recipe.getIngredients().get(position);
        holder.checkBox.setText(toCaps(ingredient.getName()));
        double amount = (ingredient.getAmount()/recipe.getIngredients().size())*servings;
        String amountString = toMixedFraction(amount) + " " + ingredient.getUnit();
        holder.amount.setText(amountString);

        if(local.size() > position){
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(local.get(position));
            holder.checkBox.setPaintFlags(local.get(position)? holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG : holder.checkBox.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.checkBox.setTextColor(fragment.getResources().getColor(local.get(position)? R.color.grey : R.color.black));
            updated = false;
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(local.size() > position){
                    local.set(position, isChecked);
                    updated = true;
                    fragment.updateGroceriesList(new GroceryList(recipe.getId(), recipe.getServings(), local));
                }
                holder.checkBox.setPaintFlags(isChecked? holder.checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG : holder.checkBox.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                holder.checkBox.setTextColor(fragment.getResources().getColor(isChecked? R.color.grey : R.color.black));
            }
        });

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

    public void setLocalList(ArrayList<Boolean> list){
        if(updated)
            updated = false;
        else{
            this.local = list;
            notifyDataSetChanged();
        }
    }

    public void setServings(int servings){
        this.servings = servings;
        notifyDataSetChanged();
    }
}
