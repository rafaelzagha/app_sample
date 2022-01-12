package com.example.app_sample.ui.search;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app_sample.data.local.models.Filter;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.RecipesResults;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;
import com.example.app_sample.data.remote.api.ApiResponse;

import java.util.ArrayList;

public class SearchViewModel extends AndroidViewModel {

    MutableLiveData<ApiResponse<RecipesResults>> recipes;
    MutableLiveData<Boolean> loading;
    RecipesRemoteDataSource dataSource;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        dataSource = RecipesRemoteDataSource.getInstance();

        recipes = new MutableLiveData<>(null);
        loading = new MutableLiveData<>(null);
    }

    public LiveData<ApiResponse<RecipesResults>> getRecipes() {
        return recipes;
    }

    public void setRecipes(ApiResponse<RecipesResults> data) {

        this.recipes.setValue(data);
        this.loading.setValue(false);

    }

    public void addToRecipes(ApiResponse<RecipesResults> data){
        if(data != null && recipes.getValue() != null){
            recipes.setValue(ApiResponse.joinResponses(recipes.getValue(), data));
        }

        else recipes.setValue(data);
        this.loading.setValue(false);
    }

    public LiveData<ApiResponse<RecipesResults>> newRequest(boolean overwrite, String query, Filter diet, ArrayList<Filter> intolerances, Filter cuisine, Filter type, Filter sort){
        String sdiet = diet == null?null : diet.tag();
        String sintolerances = intolerances == null?null : Filters.listToString(intolerances);
        String scuisine = cuisine == null?null : cuisine.tag();
        String stype = type == null?null : type.tag();
        String ssort = sort == null? null : sort.tag();
        int offset;
        if(!overwrite)
            offset= recipes.getValue() != null? recipes.getValue().getBody().getRecipes().size() : 0;
        else offset = 0;
        String sortDirection;
        if(sort == Filters.Sort.Popularity)
            sortDirection = "desc";
        else sortDirection = "asc";

        return dataSource.getRecipesByQuery(20, query, sdiet, sintolerances, scuisine, stype, ssort, sortDirection, offset);
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }


    public void setLoading(boolean b) {
        loading.setValue(b);
    }
}
