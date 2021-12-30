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
import com.example.app_sample.data.remote.RecipesRemoteDataSource;
import com.example.app_sample.data.remote.api.ApiResponse;
import com.example.app_sample.data.remote.api.FoodService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SwipeViewModel extends AndroidViewModel{

    MutableLiveData<ApiResponse<Recipes>> mutable;
    RecipesRemoteDataSource dataSource;

    public SwipeViewModel(@NonNull Application application) {
        super(application);
        dataSource = RecipesRemoteDataSource.getInstance();

        mutable = new MutableLiveData<>(null);

    }

    public LiveData<ApiResponse<Recipes>> getMutable() {
        return mutable;
    }

    public void setMutable(ApiResponse<Recipes> data) {
        if(data != null)
            mutable.setValue(data);
    }

    public LiveData<ApiResponse<Recipes>> newRequest(){
        return dataSource.getRandomRecipes(20, null);
    }
}
