package com.example.app_sample.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

import com.example.app_sample.data.local.models.Recipes;

import java.util.ArrayList;
import java.util.List;

public class MyItemKeyProvider extends ItemKeyProvider<Long> {

    private List<Recipes.Recipe> itemList;

    public MyItemKeyProvider(int scope) {
        super(scope);
        this.itemList = new ArrayList<>();
    }

    @Nullable
    @Override
    public Long getKey(int position) {
        return Long.valueOf(itemList.get(position).getId());
    }

    @Override
    public int getPosition(@NonNull Long key) {
        for (Recipes.Recipe i : itemList)
            if (i.getId() == key.intValue())
                return itemList.indexOf(i);
        return -1;
    }

    public void setItemList(List<Recipes.Recipe> recipes) {
        this.itemList = recipes;
    }

    public Iterable<Long> getKeyIterable(){
        ArrayList<Long> iterable = new ArrayList<>();
        for(Recipes.Recipe i : itemList)
            iterable.add(Long.valueOf(i.getId()));
        return iterable;
    }
}