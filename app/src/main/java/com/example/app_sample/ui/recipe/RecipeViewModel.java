package com.example.app_sample.ui.recipe;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Cookbook;
import com.example.app_sample.data.local.models.Recipes;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private RecipeRepository repo;
    private LiveData<Boolean> isSaved, isInGroceries;
    private Recipes.Recipe recipe;
    private LiveData<List<Cookbook>> cookbooks;

    public RecipeViewModel(@NonNull Application application, Recipes.Recipe recipe) {
        super(application);

        this.recipe = recipe;
        repo = new RecipeRepository(application);
        isSaved = repo.isRecipeSaved(recipe.getId());
        isInGroceries = repo.isInGroceries(recipe.getId());
        cookbooks  = repo.getCookbooks();

    }

    public LiveData<List<Cookbook>> getCookbooks() {
        return cookbooks;
    }

    public LiveData<List<String>> getCookbookImages(String id){
        return repo.getCookbookImages(id);
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

    public LiveData<Boolean> getIsInGroceries() {
        return isInGroceries;
    }

    public Task<Void> saveToGroceries(){
        return repo.saveGroceryList(recipe);
    }

    public Task<Void> deleteFromGroceries(){
        return repo.deleteGroceryList(recipe.getId());
    }

    public Task<Void> addToCookbook(String id, Recipes.Recipe recipe){
        return repo.addToCookbook(id, recipe);
    }
}
