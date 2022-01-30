package com.example.app_sample.data;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;

import com.bumptech.glide.Glide;
import com.example.app_sample.R;
import com.example.app_sample.data.local.RecipeDatabase;
import com.example.app_sample.data.local.dao.RecipeDao;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.local.models.RecipesResults;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;
import com.example.app_sample.utils.AppExecutors;

import retrofit2.Call;

public class RecipeRepository {

    private final RecipeDao recipeDao;
    private final AppExecutors appExecutors;
    private final RecipesRemoteDataSource recipesRemoteDataSource;

    public RecipeRepository(Application application) {
        recipesRemoteDataSource = RecipesRemoteDataSource.getInstance();
        recipeDao = RecipeDatabase.getDatabase(application).recipesDao();
        appExecutors = AppExecutors.getInstance();
    }

    public LiveData<Recipes.Recipe> getRecipe(int id){
        return recipeDao.getRecipe(id);
    }

    public void insertRecipe(Recipes.Recipe recipe){
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                recipeDao.insert(recipe);
            }
        });
    }

    public Call<Recipes> getRandomRecipes(int number) {
        return recipesRemoteDataSource.getRandomRecipes(number);
    }

    public Call<RecipesResults> getRecipesByQuery(int number, String query,
                                                  String diet,
                                                  String intolerances,
                                                  String cuisine,
                                                  String type,
                                                  String sort,
                                                  String sortDirection,
                                                  int offset) {

        return recipesRemoteDataSource.getRecipesByQuery( query, diet, intolerances, cuisine, type, sort, sortDirection, offset);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .error(R.drawable.example_no_image)
                .fitCenter()
                .into(imageView);
    }

}
