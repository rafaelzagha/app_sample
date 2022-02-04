package com.example.app_sample.data;

import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.example.app_sample.R;
import com.example.app_sample.data.local.RecipeDatabase;
import com.example.app_sample.data.local.dao.RecipeDao;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.local.models.RecipesResults;
import com.example.app_sample.data.remote.FirebaseManager;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;
import com.example.app_sample.utils.AppExecutors;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class RecipeRepository {

    private final RecipeDao recipeDao;
    private final AppExecutors appExecutors;
    private final RecipesRemoteDataSource recipesRemoteDataSource;
    private final FirebaseManager firebaseManager;

    public RecipeRepository(Application application) {
        firebaseManager = new FirebaseManager();
        recipesRemoteDataSource = RecipesRemoteDataSource.getInstance();
        recipeDao = RecipeDatabase.getDatabase(application).recipesDao();
        appExecutors = AppExecutors.getInstance();
    }

    public LiveData<Recipes.Recipe> getRecipe(int id){
        return recipeDao.getRecipe(id);
    }

    public void saveRecipe(Recipes.Recipe recipe){
        appExecutors.diskIO().execute(() -> recipeDao.insert(recipe));
        firebaseManager.saveRecipe(recipe.getId());
    }

    public void removeRecipe(int id){
        appExecutors.diskIO().execute(() -> recipeDao.deleteRecipe(id));
        firebaseManager.deleteRecipe(id);
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

        return recipesRemoteDataSource.getRecipesByQuery( query, diet, intolerances, cuisine, type, sort, sortDirection, offset);
    }

    public void clearTable(){
        appExecutors.diskIO().execute(recipeDao::clearTable);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .error(R.drawable.example_no_image)
                .fitCenter()
                .into(imageView);
    }

    public LiveData<List<Recipes.Recipe>> getSavedRecipes(){
        MutableLiveData<List<Recipes.Recipe>> recipes = new MutableLiveData<>();
        firebaseManager.getFavorites().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Integer> list = new ArrayList<>();
                    for (DataSnapshot i : task.getResult().getChildren())
                        list.add(Integer.valueOf(i.getKey()));
                    //todo: retrieve saved recipes from database
                }
            }
        });
        return recipes;
    }


}
