package com.example.app_sample.ui.home.swipe;

import android.app.Application;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.local.models.RecipesResults;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;
import com.example.app_sample.data.remote.api.ApiResponse;
import com.example.app_sample.data.remote.api.FoodService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SwipeViewModel extends AndroidViewModel {

    MutableLiveData<ApiResponse<Recipes>> recipes;
    RecipesRemoteDataSource dataSource;
    int position;

    public SwipeViewModel(@NonNull Application application) {
        super(application);

        dataSource = RecipesRemoteDataSource.getInstance();
        recipes = new MutableLiveData<>(null);
        position = 0;
    }

    public LiveData<ApiResponse<Recipes>> getRecipes() {
        return recipes;
    }

    public void addToRecipes(ApiResponse<Recipes> data) {
        if (data != null && recipes.getValue() != null) {
            recipes.setValue(ApiResponse.joinResponses2(recipes.getValue(), data));
        } else recipes.setValue(data);
    }

    public void incrementPosition() {
        position++;
    }

    public void decreasePosition() {
        position--;
    }

    public LiveData<ApiResponse<Recipes>> newRequest() {
        return dataSource.getRandomRecipes(20);
    }


    public int getPosition() {
        return position;
    }
}
