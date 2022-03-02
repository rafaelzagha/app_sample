package com.example.app_sample.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.GroceryList;
import com.example.app_sample.data.local.models.Recipes;

import java.util.List;

public class SavedRecipesViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;
    private LiveData<List<Recipes.Recipe>> recipes;

    public SavedRecipesViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
        recipes = recipeRepository.getSavedRecipes();
    }

    public LiveData<List<Recipes.Recipe>> getRecipes() {
        return recipes;
    }

    public void deleteRecipe(int id){
        recipeRepository.removeRecipe(id);
    }

    public void setRecipeColor(int id, int color){
        recipeRepository.setRecipeColor(id, color);
    }

    public void addToGroceries(int id, int size){
        recipeRepository.saveGroceryList(new GroceryList(id, size));
    }

    public void deleteFromGroceries(int id){
        recipeRepository.deleteGroceryList(id);
    }

    public LiveData<Boolean> isInGroceries(int id){
        return recipeRepository.isInGroceries(id);
    }
}