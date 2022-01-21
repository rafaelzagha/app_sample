package com.example.app_sample.data.remote;

import android.content.Context;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;

import com.bumptech.glide.Glide;
import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Filter;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.RecipesResults;
import com.example.app_sample.data.remote.api.ApiResponse;
import com.example.app_sample.data.remote.api.FoodApi;
import com.example.app_sample.data.remote.api.FoodService;
import com.example.app_sample.data.local.models.Recipes;

import java.util.ArrayList;
import java.util.List;

public class RecipesRemoteDataSource {

    private FoodApi foodApi;

    private static RecipesRemoteDataSource instance;

    private RecipesRemoteDataSource() {
        this.foodApi = FoodService.getFoodApi();
    }

    public static RecipesRemoteDataSource getInstance() {

        if (instance == null) {
            instance = new RecipesRemoteDataSource();
        }
        return instance;

    }

    public LiveData<ApiResponse<Recipes>> getRandomRecipes(int number) {
        return foodApi.getRandomRecipes(number);
    }

    public LiveData<ApiResponse<RecipesResults>> getRecipesByQuery(int number, String query,
                                                                   String diet,
                                                                   String intolerances,
                                                                   String cuisine,
                                                                   String type,
                                                                   String sort,
                                                                   String sortDirection,
                                                                   int offset) {

        return foodApi.getRecipesByQuery(20, query, true, true, diet, intolerances, cuisine, type, sort, offset, sortDirection);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .error(R.drawable.example_no_image)
                .fitCenter()
                .into(imageView);
    }
}
