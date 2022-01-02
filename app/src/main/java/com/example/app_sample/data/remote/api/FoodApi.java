package com.example.app_sample.data.remote.api;

import androidx.lifecycle.LiveData;

import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.local.models.RecipesResults;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FoodApi {

    @GET("recipes/random")
    LiveData<ApiResponse<Recipes>> getRandomRecipes(@Query("number") int number,
                                                   @Query("tags") String tags);

    @GET("recipes/complexSearch")
    LiveData<ApiResponse<RecipesResults>> getRecipesByQuery(@Query("number") int number ,
                                                            @Query("query") String query,
                                                            @Query("addRecipeInformation") boolean addData,
                                                            @Query("fillIngredients") boolean addIngredients,
                                                            @Query("diet") String diets,
                                                            @Query("intolerances") String intolerances,
                                                            @Query("cuisine") String cuisines,
                                                            @Query("type") String mealTypes,
                                                            @Query("sort") String sort);

    @GET("recipes/{id}/information")
    LiveData<ApiResponse<Recipes.Recipe>> getRecipeById(@Path("id") long movieId);
}
