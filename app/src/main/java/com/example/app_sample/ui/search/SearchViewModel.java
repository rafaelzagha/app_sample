package com.example.app_sample.ui.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Filter;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.local.models.RecipesResults;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends AndroidViewModel {

    private MutableLiveData<RecipesResults> recipes;
    private MutableLiveData<String> error;
    private MutableLiveData<Boolean> loading;
    private RecipeRepository recipeRepository;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(getApplication());

        recipes = new MutableLiveData<>(null);
        loading = new MutableLiveData<>(null);
        error = new MutableLiveData<>();
    }

    public LiveData<RecipesResults> getRecipes() {
        return recipes;
    }

    private void setRecipes(RecipesResults data) {

        this.recipes.setValue(data);
        this.loading.setValue(false);

    }

    public void addToRecipes(RecipesResults data) {
        if (data != null && recipes.getValue() != null) {
            List<Recipes.Recipe> list = recipes.getValue().getRecipes();
            list.addAll(data.getRecipes());
            recipes.setValue(new RecipesResults(list));
        } else recipes.setValue(data);
        this.loading.setValue(false);
    }

    public void newRequest(boolean overwrite, String query, Filter diet, ArrayList<Filter> intolerances, Filter cuisine, Filter type, Filter sort) {
        String sdiet = diet == null ? null : diet.tag();
        String sintolerances = intolerances == null ? null : Filters.listToString(intolerances);
        String scuisine = cuisine == null ? null : cuisine.tag();
        String stype = type == null ? null : type.tag();
        String ssort = sort == null ? null : sort.tag();
        int offset;
        if (!overwrite)
            offset = recipes.getValue() != null ? recipes.getValue().getRecipes().size() : 0;
        else offset = 0;
        String sortDirection;
        if (sort == Filters.Sort.Popularity)
            sortDirection = "desc";
        else sortDirection = "asc";

        recipeRepository.loadRecipesByQuery(query, sdiet, sintolerances, scuisine, stype, ssort, sortDirection, offset).enqueue(new Callback<RecipesResults>() {
            @Override
            public void onResponse(@NonNull Call<RecipesResults> call, @NonNull Response<RecipesResults> response) {
                if (response.isSuccessful()) {
                    if (overwrite)
                        setRecipes(response.body());
                    else
                        addToRecipes(response.body());
                } else {
                    if (recipeRepository.changeApiKey())
                        recipeRepository.loadRecipesByQuery(query, sdiet, sintolerances, scuisine, stype, ssort, sortDirection, offset)
                                .enqueue(new Callback<RecipesResults>() {
                                    @Override
                                    public void onResponse(@NonNull Call<RecipesResults> call, @NonNull Response<RecipesResults> response) {
                                        if (response.isSuccessful()) {
                                            if (overwrite)
                                                setRecipes(response.body());
                                            else
                                                addToRecipes(response.body());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<RecipesResults> call, Throwable t) {
                                        error.setValue("Request Error  " + response.code());

                                    }
                                });
                    error.setValue("Request Error  " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipesResults> call, @NonNull Throwable t) {
                error.setValue("Request Error " + t.getMessage());
            }
        });
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }


    public void setLoading(boolean b) {
        loading.setValue(b);
    }
}
