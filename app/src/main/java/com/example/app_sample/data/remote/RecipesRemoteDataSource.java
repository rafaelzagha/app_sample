package com.example.app_sample.data.remote;

import android.content.Context;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;

import com.bumptech.glide.Glide;
import com.example.app_sample.R;
import com.example.app_sample.data.remote.api.ApiResponse;
import com.example.app_sample.data.remote.api.FoodApi;
import com.example.app_sample.data.remote.api.FoodService;
import com.example.app_sample.data.local.models.Recipes;

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

    public static void loadImage(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .error(R.drawable.ic_no_image)
                .centerCrop()
                .into(imageView);
    }
}
