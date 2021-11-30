package com.example.app_sample.api;

import com.example.app_sample.models.Recipes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoodApi {

    @GET("recipes/random")
    Call<Recipes> getPopularRecipes(@Query("number") int number,
                                    @Query("tags") List<String> tags);


}
