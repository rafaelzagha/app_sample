package com.example.app_sample.data.local.dao;

import androidx.room.TypeConverter;

import com.example.app_sample.data.local.models.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class IngredientsConverter {

    @TypeConverter
    public String toString(List<Ingredient> list){
        if(list == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        return gson.toJson(list, type);
    }

    @TypeConverter
    public List<Ingredient> toIngredientList(String ingredient) {
        if (ingredient == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        List<Ingredient> list = gson.fromJson(ingredient, type);
        return list;
    }

}
