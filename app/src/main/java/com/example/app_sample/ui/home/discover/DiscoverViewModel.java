package com.example.app_sample.ui.home.discover;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Recipes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverViewModel extends AndroidViewModel {

    private MutableLiveData<Recipes> recipes;
    private RecipeRepository recipeRepository;
    private MutableLiveData<String> error;

    public DiscoverViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(getApplication());
        recipes = new MutableLiveData<>();
        error = new MutableLiveData<>();
        newRequest();
    }

    public LiveData<Recipes> getRecipes() {
        return recipes;
    }

    private void addToRecipes(Recipes data) {
        if (data != null && recipes.getValue() != null) {
            List<Recipes.Recipe> list = recipes.getValue().getRecipes();
            list.addAll(data.getRecipes());
            recipes.setValue(new Recipes(list));
        } else recipes.setValue(data);
    }

    public void newRequest() {
        recipeRepository.loadRandomRecipes(20).enqueue(new Callback<Recipes>() {
            @Override
            public void onResponse(@NonNull Call<Recipes> call, @NonNull Response<Recipes> response) {
                if (response.isSuccessful()) {
                    addToRecipes(response.body());
                    clearError();
                }
                else {
                    error.setValue("Request Error  " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Recipes> call, @NonNull Throwable t) {
                error.setValue("Request Error " + t.getMessage());
            }
        });

    }

    public LiveData<String> getError() {
        return error;
    }

    public void clearError(){
        error.setValue(null);
    }
}
