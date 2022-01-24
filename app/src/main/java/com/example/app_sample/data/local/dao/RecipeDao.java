package com.example.app_sample.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.app_sample.data.local.models.Recipes;

@Dao
public interface RecipeDao {

    @Insert
    void insert(Recipes.Recipe recipe);

    @Query("DELETE FROM recipes where id = :id")
    void deleteRecipe(int id);

}
