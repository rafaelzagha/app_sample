package com.example.app_sample.data;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.example.app_sample.R;
import com.example.app_sample.data.local.RecipeDatabase;
import com.example.app_sample.data.local.dao.RecipeDao;
import com.example.app_sample.data.local.models.GroceryList;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.local.models.RecipesResults;
import com.example.app_sample.data.remote.FirebaseManager;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;
import com.example.app_sample.utils.AppExecutors;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class RecipeRepository {

    private final RecipeDao recipeDao;
    private final AppExecutors appExecutors;
    private final RecipeDatabase recipeDatabase;
    private final RecipesRemoteDataSource recipesRemoteDataSource;
    private final FirebaseManager firebaseManager;

    public RecipeRepository(Application application) {
        firebaseManager = new FirebaseManager();
        recipesRemoteDataSource = RecipesRemoteDataSource.getInstance();
        recipeDatabase = RecipeDatabase.getDatabase(application);
        recipeDao = recipeDatabase.recipesDao();
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
        List<Recipes.Recipe> list = new ArrayList<>();
        firebaseManager.getFavorites().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                appExecutors.diskIO().execute(() -> {
                    Recipes.Recipe recipe = recipeDao.getRecipe(Integer.parseInt(snapshot.getKey()));
                    if (recipe != null)
                        list.add(recipeDao.getRecipe(Integer.parseInt(snapshot.getKey())));
                    else {
                        try {
                            recipe = recipesRemoteDataSource.getRecipeById(Integer.parseInt(snapshot.getKey())).execute().body();
                            recipeDao.insert(recipe);
                            list.add(recipe);
                        } catch (IOException e) {
                            Log.d("tag", "Couldn't retrieve saved recipes");
                        }
                    }
                    recipes.postValue(list);
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                Log.d("tag", "removed" + snapshot.getKey());
                for(Recipes.Recipe i : list){
                    if(i.getId().equals(Integer.valueOf(snapshot.getKey()))){
                        list.remove(i);
                        appExecutors.diskIO().execute(() -> removeRecipe(i.getId()));
                        recipes.setValue(list);
                        break;
                    }
                }
            }


            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

    public void setRecipeColor(int id, int color) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                recipeDao.setRecipeColor(id, color);
            }
        });
    }

    public Task<Void> saveGroceryList(GroceryList gl) {
        return firebaseManager.saveGroceryList(gl);
    }

    public void updateGroceryList(GroceryList gl) {
        firebaseManager.updateGroceryList(gl);
    }


    public Task<Void> deleteGroceryList(int id) {
        return firebaseManager.deleteGroceryList(id);
    }

    public LiveData<GroceryList> getGroceryList(int id) {
        MutableLiveData<GroceryList> list = new MutableLiveData<>();
        firebaseManager.getGroceryList(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.setValue(snapshot.getValue(GroceryList.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return list;
    }

    public LiveData<List<Recipes.Recipe>> getGroceriesRecipes() {
        MutableLiveData<List<Recipes.Recipe>> recipes = new MutableLiveData<>();
        List<Recipes.Recipe> list = new ArrayList<>();
        firebaseManager.getGroceries().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                appExecutors.diskIO().execute(() -> {
                    Recipes.Recipe recipe = recipeDao.getRecipe(Integer.parseInt(snapshot.getKey()));
                    if (recipe != null)
                        list.add(recipeDao.getRecipe(Integer.parseInt(snapshot.getKey())));
                    else {
                        try {
                            recipe = recipesRemoteDataSource.getRecipeById(Integer.parseInt(snapshot.getKey())).execute().body();
                            recipeDao.insert(recipe);
                            list.add(recipe);
                        } catch (IOException e) {
                            Log.d("tag", "Couldn't retrieve saved recipes");
                        }
                    }
                    recipes.postValue(list);
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                for(Recipes.Recipe i : list){
                    if(i.getId().equals(Integer.valueOf(snapshot.getKey()))){
                        list.remove(i);
                        recipes.setValue(list);
                        break;
                    }
                }
            }


            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return recipes;
    }


    public LiveData<Boolean> isInGroceries(int id){
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        firebaseManager.isInGroceries(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.setValue(snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return data;
    }

}
