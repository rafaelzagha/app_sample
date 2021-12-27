package com.example.app_sample.data.remote;

import androidx.lifecycle.LiveData;

import com.example.app_sample.data.remote.api.ApiResponse;
import com.example.app_sample.data.remote.api.FoodApi;
import com.example.app_sample.data.remote.api.FoodService;
import com.example.app_sample.models.Recipes;

import java.util.List;

public class RecipesRemoteDataSource {

    private FoodApi foodApi;

    private static RecipesRemoteDataSource instance;

    private RecipesRemoteDataSource() {
        this.foodApi = FoodService.getFoodApi();
    }

    public static RecipesRemoteDataSource getInstance(){

        if(instance == null){
            instance = new RecipesRemoteDataSource();
        }
        return instance;

    }

    public LiveData<ApiResponse<Recipes>> getRandomRecipes(int number, List<String> tags){
        return foodApi.getRandomRecipes(number, tags);
    }
}
