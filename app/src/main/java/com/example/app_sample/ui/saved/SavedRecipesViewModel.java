package com.example.app_sample.ui.saved;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.remote.FirebaseManager;

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
}