package com.example.app_sample.ui.home.swipe;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.utils.AppExecutors;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwipeViewModel extends AndroidViewModel {

    MutableLiveData<Recipes> recipes;
    RecipeRepository recipeRepository;
    int position;

    public SwipeViewModel(@NonNull Application application) {
        super(application);

        recipeRepository = new RecipeRepository(getApplication());
        recipes = new MutableLiveData<>();
        position = 0;
        newRequest();
    }

    public LiveData<Recipes> getRecipes() {
        return recipes;
    }

    private void addToRecipes(Recipes data) {
        if (data != null && recipes.getValue() != null && recipes.getValue().getRecipes() != null) {
            List<Recipes.Recipe> list = recipes.getValue().getRecipes();
            list.addAll(data.getRecipes());
            recipes.setValue(new Recipes(list).setCode(data.getCode()).setMessage(data.getMessage()));
        } else recipes.setValue(data);
    }

    public void newRequest() {

        recipeRepository.loadRandomRecipes(20).enqueue(new Callback<Recipes>() {
            @Override
            public void onResponse(Call<Recipes> call, Response<Recipes> response) {
                if(response.body() != null){
                    addToRecipes(response.body().setCode(response.code()).setMessage(response.message()));
                }
                else{
                    addToRecipes(new Recipes(null).setCode(response.code()).setMessage(response.message()));
                }
            }

            @Override
            public void onFailure(Call<Recipes> call, Throwable t) {
                if(recipes.getValue() != null)
                    recipes.setValue(recipes.getValue().setCode(0).setMessage(t.getMessage()));
                else{
                    recipes.setValue(new Recipes(null).setCode(0).setMessage(t.getMessage()));
                }
            }
        });



    }

    public void saveRecipe(Recipes.Recipe recipe){
        recipeRepository.saveRecipe(recipe);
    }

    public void deleteRecipe(int id){
        recipeRepository.removeRecipe(id);
    }

    public void incrementPosition() {
        position++;
    }

    public void decreasePosition() {
        position--;
    }

    public int getPosition() {
        return position;
    }

    public void resetError(){
        recipes.getValue().setCode(200);
    }
}
