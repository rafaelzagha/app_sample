package com.example.app_sample.ui.groceries;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.GroceryList;
import com.example.app_sample.data.local.models.Recipes;

import java.util.List;

public class GroceriesViewModel extends AndroidViewModel {

    RecipeRepository repo;
    LiveData<List<Recipes.Recipe>> recipes;

    public GroceriesViewModel(@NonNull Application application) {
        super(application);
        repo = new RecipeRepository(application);
        recipes = repo.getGroceriesRecipes();

    }

    public LiveData<List<Recipes.Recipe>> getRecipes() {
        return recipes;
    }

    public LiveData<GroceryList> getGroceryList(int id){
        return repo.getGroceryList(id);
    }

    public void deleteGroceryList(int id){
        repo.deleteGroceryList(id);
    }

    public void updateGroceriesList(GroceryList gl){
        repo.updateGroceryList(gl);
    }

    public void updateGroceryServings(int id, int servings){
        repo.updateGroceryServings(id, servings);
    }


}
