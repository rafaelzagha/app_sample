package com.example.app_sample.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.app_sample.data.local.models.Recipes;

import java.util.ArrayList;

@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Recipes.Recipe recipe);

    @Query("DELETE FROM recipes where id = :id")
    void deleteRecipe(int id);

    @Query("SELECT * FROM recipes WHERE id=:id")
    Recipes.Recipe getRecipe(int id);

    @Query("SELECT EXISTS (SELECT 1 FROM recipes WHERE id = :id)")
    boolean inTable(int id);

    @Query("DELETE FROM recipes")
    void clearTable();

    @Query("UPDATE recipes Set color = :color WHERE id = :id")
    void setRecipeColor(int id, int color);



}
