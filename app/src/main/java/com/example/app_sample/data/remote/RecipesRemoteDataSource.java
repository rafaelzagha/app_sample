package com.example.app_sample.data.remote;

import com.example.app_sample.data.local.models.RecipeImage;
import com.example.app_sample.data.local.models.RecipesResults;
import com.example.app_sample.data.remote.api.FoodApi;
import com.example.app_sample.data.remote.api.FoodService;
import com.example.app_sample.data.local.models.Recipes;

import retrofit2.Call;

public class RecipesRemoteDataSource {

    private static RecipesRemoteDataSource instance;

    private RecipesRemoteDataSource() {
    }

    public static RecipesRemoteDataSource getInstance() {

        if (instance == null) {
            instance = new RecipesRemoteDataSource();
        }
        return instance;

    }

    public Call<Recipes> getRandomRecipes(int number) {
        return FoodService.getFoodApi().getRandomRecipes(number);
    }

    public Call<RecipesResults> getRecipesByQuery(String query,
                                                                   String diet,
                                                                   String intolerances,
                                                                   String cuisine,
                                                                   String type,
                                                                   String sort,
                                                                   String sortDirection,
                                                                   int offset) {

        return FoodService.getFoodApi().getRecipesByQuery(20, query, true, true, diet, intolerances, cuisine, type, sort, offset, sortDirection);
    }

    public Call<Recipes.Recipe> getRecipeById(int id){
        return FoodService.getFoodApi().getRecipeById(id);
    }

    public Call<RecipeImage> getRecipeCard(long id){
        return FoodService.getFoodApi().getRecipeCard(id);
    }

}
