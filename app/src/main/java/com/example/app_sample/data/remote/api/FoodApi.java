package com.example.app_sample.data.remote.api;

import com.example.app_sample.data.local.models.RecipeImage;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.local.models.RecipesResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FoodApi {

    @GET("recipes/random")
    Call<Recipes> getRandomRecipes(@Query("number") int number);

    @GET("recipes/complexSearch")
    Call<RecipesResults> getRecipesByQuery(@Query("number") int number ,
                                                            @Query("query") String query,
                                                            @Query("addRecipeInformation") boolean addData,
                                                            @Query("fillIngredients") boolean addIngredients,
                                                            @Query("diet") String diets,
                                                            @Query("intolerances") String intolerances,
                                                            @Query("cuisine") String cuisines,
                                                            @Query("type") String mealTypes,
                                                            @Query("sort") String sort,
                                                            @Query("offset") int offset,
                                                            @Query("sortDirection") String sortDirection);

    @GET("recipes/{id}/information")
    Call<Recipes.Recipe> getRecipeById(@Path("id") long id);

    @GET("recipes/{id}/card")
    Call<RecipeImage> getRecipeCard(@Path("id") long id);
}
