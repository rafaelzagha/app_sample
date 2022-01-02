package com.example.app_sample.ui.home.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app_sample.data.local.models.Filter;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.local.models.RecipesResults;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;
import com.example.app_sample.data.remote.api.ApiResponse;

import java.util.ArrayList;

public class SearchViewModel extends AndroidViewModel {

    MutableLiveData<ApiResponse<RecipesResults>> mutable;
    RecipesRemoteDataSource dataSource;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        dataSource = RecipesRemoteDataSource.getInstance();

        mutable = new MutableLiveData<>(null);

    }

    public LiveData<ApiResponse<RecipesResults>> getMutable() {
        return mutable;
    }

    public void setMutable(ApiResponse<RecipesResults> data) {
        if(data != null)
            mutable.setValue(data);
    }

    public LiveData<ApiResponse<RecipesResults>> newRequest(String query, Filter diet, ArrayList<Filter> intolerances, Filter cuisine, Filter type, Filter sort){
        String sdiet = diet == null?null : diet.tag();
        String sintolerances = intolerances == null?null : Filters.listToString(intolerances);
        String scuisine = cuisine == null?null : cuisine.tag();
        String stype = type == null?null : type.tag();

        return dataSource.getRecipesByQuery(20, query, sdiet, sintolerances, scuisine, stype, sort.tag());
    }


}
