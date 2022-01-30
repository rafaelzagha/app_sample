package com.example.app_sample.ui.home.discover;

import android.app.Application;
import android.util.Log;

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

    MutableLiveData<Recipes> recipes;
    RecipeRepository recipeRepository;
    MutableLiveData<String> error;

    public DiscoverViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(getApplication());
        recipes = new MutableLiveData<>();
        error = new MutableLiveData<>();
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
            public void onResponse(Call<Recipes> call, Response<Recipes> response) {
                if (response.isSuccessful()) {
                    addToRecipes(response.body());
                }
                else {
                    error.setValue("Request Error  " + response.code());
                    Log.d("tag", ""+response.code());
                }
            }

            @Override
            public void onFailure(Call<Recipes> call, Throwable t) {
                Log.d("tag", t.getMessage());
                error.setValue("Request Error " + t.getMessage());
            }
        });

    }

    public MutableLiveData<String> getError() {
        return error;
    }
}
