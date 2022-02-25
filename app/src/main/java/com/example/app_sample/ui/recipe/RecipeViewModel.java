package com.example.app_sample.ui.recipe;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.GroceryList;
import com.example.app_sample.data.local.models.Recipes;
import com.google.android.gms.tasks.Task;

public class RecipeViewModel extends AndroidViewModel {

    RecipeRepository repo;
    LiveData<Boolean> isSaved;
    Recipes.Recipe recipe;


    public RecipeViewModel(@NonNull Application application, Recipes.Recipe recipe) {
        super(application);

        this.recipe = recipe;
        repo = new RecipeRepository(application);
        isSaved = repo.isRecipeSaved(recipe.getId());

    }

    public LiveData<Boolean> getIsSaved() {
        return isSaved;
    }

    public Task<Void> removeRecipe(int id){
        return repo.removeRecipe(id);
    }

    public Task<Void> saveRecipe(Recipes.Recipe recipe){
        return repo.saveRecipe(recipe);
    }

    public void saveToGroceries(){
        repo.setGroceryList(new GroceryList(recipe.getId(), recipe.getIngredients().size()));
    }
}
