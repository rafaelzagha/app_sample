package com.example.app_sample.data.remote.api;

import androidx.lifecycle.LiveData;

import com.example.app_sample.models.Recipes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodApi {

    @GET("recipes/random")
    LiveData<ApiResponse<Recipes>> getRandomRecipes(@Query("number") int number,
                                                   @Query("tags") List<String> tags);

    @GET("recipes/complexSearch")
    LiveData<ApiResponse<Recipes>> getRecipesByQuery(@Query("number") int number ,
                                    @Query("query") String query,
                                    @Query("addRecipeInformation") boolean addData,
                                    @Query("diets") List<String> diets,
                                    @Query("intolerances") List<String> intolerances,
                                    @Query("cuisine") List<String> cuisines,
                                    @Query("type") List<String> mealTypes,
                                    @Query("sort") String sort);
}
