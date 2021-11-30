package com.example.app_sample.utils;

import android.util.Log;

import com.example.app_sample.api.FoodApi;
import com.example.app_sample.api.FoodService;
import com.example.app_sample.models.Recipes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {

    public final static String BASE_URL = "https://api.spoonacular.com/";
    public final static String API_KEY = "0140582cb9c243598bb4d22d159fda84";

    public static void getRandomRecipes(int i) {
        FoodApi foodapi = FoodService.getFoodApi();
        Call<Recipes> recipesCall = foodapi.getPopularRecipes(i, null);
        recipesCall.enqueue(new Callback<Recipes>() {

            @Override
            public void onResponse(Call<Recipes> call, Response<Recipes> response) {
                if(response.code() == 200){
                    for (Recipes.Recipe r : response.body().getRecipes()) {
                        Log.d("tag", r.getTitle());
                    }
                }
            }



            @Override
            public void onFailure(Call<Recipes> call, Throwable t) {

            }
        });

    }
}
