package com.example.app_sample.ui.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Ingredient;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;
import com.example.app_sample.utils.Constants;

import java.util.List;
import java.util.Locale;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private List<Ingredient> ingredients;
    private Context context;

    public IngredientsAdapter(List<Ingredient> ingredients, Context context) {
        this.ingredients = ingredients;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        RecipeRepository.loadImage(context, (Constants.IMAGE_URL+ingredient.getImage()), holder.img);
        String name = ingredient.getName().substring(0,1).toUpperCase(Locale.ROOT) + ingredient.getName().substring(1);
        holder.name.setText(name);

        if(ingredient.getAmount().intValue() == ingredient.getAmount())
            holder.amount.setText(ingredient.getAmount().intValue() + " " +  ingredient.getUnit());
        else
            holder.amount.setText(ingredient.getAmount() + " " +  ingredient.getUnit());

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.ingredient_name);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}
