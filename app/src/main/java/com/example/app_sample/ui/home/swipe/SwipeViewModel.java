package com.example.app_sample.ui.home.swipe;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.app_sample.R;
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
    MutableLiveData<String> error;
    RecipeRepository recipeRepository;
    int position;

    public SwipeViewModel(@NonNull Application application) {
        super(application);

        recipeRepository = new RecipeRepository(getApplication());
        recipes = new MutableLiveData<>();
        error = new MutableLiveData<>();
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
            recipes.setValue(new Recipes(list));
        } else recipes.setValue(data);
    }

    public void newRequest() {

        recipeRepository.loadRandomRecipes(20).enqueue(new Callback<Recipes>() {
            @Override
            public void onResponse(Call<Recipes> call, Response<Recipes> response) {
                if(response.body() != null){
                    addToRecipes(response.body());
                }
                else{
                    if(response.code() == 403)
                        error.setValue(getApplication().getString(R.string.request_limit));
                    else
                        error.setValue(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Recipes> call, Throwable t) {
                error.setValue(getApplication().getString(R.string.no_internet));
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

    public LiveData<String> getError() {
        return error;
    }

    public void resetError(){
        error.setValue(null);
    }
}
