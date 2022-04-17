package com.example.app_sample.utils;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.ui.groceries.GroceriesAdapter;
import com.example.app_sample.ui.profile.cookbooks.CookbookRecipesAdapter;
import com.example.app_sample.ui.profile.cookbooks.RecipesAdapter;

public class MyItemLookup extends ItemDetailsLookup<Long> {

    private final RecyclerView recyclerView;

    public MyItemLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof GroceriesAdapter.ViewHolder) {
                return ((GroceriesAdapter.ViewHolder) viewHolder).getItemDetails();
            } else if (viewHolder instanceof CookbookRecipesAdapter.ViewHolder) {
                return ((CookbookRecipesAdapter.ViewHolder) viewHolder).getItemDetails();
            } else if (viewHolder instanceof RecipesAdapter.ViewHolder) {
                return ((RecipesAdapter.ViewHolder) viewHolder).getItemDetails();
            }
        }
        return null;
    }
}