package com.example.app_sample.ui.recipe;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Recipes;
import com.google.android.gms.tasks.Task;

public class RecipeViewModel extends AndroidViewModel {

    RecipeRepository repo;
    LiveData<Boolean> isSaved;


    public RecipeViewModel(@NonNull Application application, int id) {
        super(application);

        repo = new RecipeRepository(application);
        isSaved = repo.isRecipeSaved(id);

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
}
