package com.example.app_sample.data;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.example.app_sample.R;
import com.example.app_sample.data.local.RecipeDatabase;
import com.example.app_sample.data.local.dao.GroceriesDao;
import com.example.app_sample.data.local.dao.RecipeDao;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.local.models.RecipesResults;
import com.example.app_sample.data.remote.FirebaseManager;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;
import com.example.app_sample.utils.AppExecutors;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class RecipeRepository {

    private final RecipeDao recipeDao;
    private final GroceriesDao groceriesDao;
    private final AppExecutors appExecutors;
    private final RecipeDatabase recipeDatabase;
    private final RecipesRemoteDataSource recipesRemoteDataSource;
    private final FirebaseManager firebaseManager;

    public RecipeRepository(Application application) {
        firebaseManager = new FirebaseManager();
        recipesRemoteDataSource = RecipesRemoteDataSource.getInstance();
        recipeDatabase = RecipeDatabase.getDatabase(application);
        recipeDao = recipeDatabase.recipesDao();
        groceriesDao = recipeDatabase.groceriesDao();
        appExecutors = AppExecutors.getInstance();
    }

    public LiveData<Recipes.Recipe> getRecipe(int id) {
        MutableLiveData<Recipes.Recipe> recipe = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> recipe.postValue(recipeDao.getRecipe(id)));
        return recipe;
    }

    public Task<Void> saveRecipe(Recipes.Recipe recipe) {
        appExecutors.diskIO().execute(() -> recipeDao.insert(recipe));
        return firebaseManager.saveRecipe(recipe.getId());
    }

    public Task<Void> removeRecipe(int id) {
        appExecutors.diskIO().execute(() -> recipeDao.deleteRecipe(id));
        return firebaseManager.deleteRecipe(id);
    }

    public Call<Recipes> loadRandomRecipes(int number) {
        return recipesRemoteDataSource.getRandomRecipes(number);
    }

    public Call<RecipesResults> loadRecipesByQuery(int number, String query,
                                                   String diet,
                                                   String intolerances,
                                                   String cuisine,
                                                   String type,
                                                   String sort,
                                                   String sortDirection,
                                                   int offset) {

        return recipesRemoteDataSource.getRecipesByQuery(query, diet, intolerances, cuisine, type, sort, sortDirection, offset);
    }

    public void clearTable() {
        appExecutors.diskIO().execute(recipeDao::clearTable);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .error(R.drawable.example_no_image)
                .fitCenter()
                .into(imageView);
    }

    public LiveData<List<Recipes.Recipe>> getSavedRecipes() {
        MutableLiveData<List<Recipes.Recipe>> recipes = new MutableLiveData<>();
        firebaseManager.getFavorites().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Recipes.Recipe> list = new ArrayList<>();
                appExecutors.diskIO().execute(() -> {
                    for (DataSnapshot i : snapshot.getChildren()) {
                        Recipes.Recipe recipe = recipeDao.getRecipe(Integer.parseInt(i.getKey()));
                        if(recipe != null)
                            list.add(recipeDao.getRecipe(Integer.parseInt(i.getKey())));
                        else {
                            try {
                                recipe = recipesRemoteDataSource.getRecipeById(Integer.parseInt(i.getKey())).execute().body();
                                recipeDao.insert(recipe);
                                list.add(recipe);
                            } catch (IOException e) {
                                Log.d("tag", "Couldn't retrieve saved recipes");
                            }
                        }
                    }
                    recipes.postValue(list);
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return recipes;
    }

    public LiveData<Boolean> isRecipeSaved(int id) {
        MutableLiveData<Boolean> bool = new MutableLiveData<>();
        firebaseManager.isSaved(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bool.setValue(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return bool;
    }

    public void setRecipeColor(int id, int color){
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                recipeDao.setRecipeColor(id, color);
            }
        });
    }




}
