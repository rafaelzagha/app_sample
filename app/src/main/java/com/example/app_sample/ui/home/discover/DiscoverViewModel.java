package com.example.app_sample.ui.home.discover;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;
import com.example.app_sample.data.remote.api.ApiResponse;

public class DiscoverViewModel extends AndroidViewModel {

    MutableLiveData<ApiResponse<Recipes>> recipes;
    RecipesRemoteDataSource dataSource;

    public DiscoverViewModel(@NonNull Application application) {
        super(application);
        dataSource = RecipesRemoteDataSource.getInstance();
        recipes = new MutableLiveData<>(null);
    }

    public LiveData<ApiResponse<Recipes>> getRecipes() {
        return recipes;
    }

    public void addToRecipes(ApiResponse<Recipes> data) {
        if (data != null && recipes.getValue() != null) {
            recipes.setValue(ApiResponse.joinResponses2(recipes.getValue(), data));
        } else recipes.setValue(data);
    }

    public LiveData<ApiResponse<Recipes>> newRequest() {
        return dataSource.getRandomRecipes(20);
    }

}
