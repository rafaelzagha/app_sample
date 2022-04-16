package com.example.app_sample.data;

import android.app.Application;
import android.content.Context;
import android.provider.ContactsContract;
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
import com.example.app_sample.data.local.models.Cookbook;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.GroceryList;
import com.example.app_sample.data.local.models.RecipeImage;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.local.models.RecipesResults;
import com.example.app_sample.data.remote.FirebaseManager;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;
import com.example.app_sample.utils.AppExecutors;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public LiveData<String> getUsername() {
        return firebaseManager.getUsername();
    }

    public Task<Void> saveRecipe(Recipes.Recipe recipe) {
        appExecutors.diskIO().execute(() -> recipeDao.insert(recipe));
        return firebaseManager.saveRecipe(recipe.getId());
    }

    public Task<Void> removeRecipe(int id) {
        firebaseManager.isInGroceries(id).addListenerForSingleValueEvent(new ValueEventListener() { //check if recipe in groceries
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                    firebaseManager.getCookbooks().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds : snapshot.getChildren()){
                                if(ds.child("recipes").hasChild(String.valueOf(id)))
                                    return;
                            }
                            appExecutors.diskIO().execute(() -> recipeDao.deleteRecipe(id));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return firebaseManager.deleteRecipe(id);
    }

    public Call<Recipes> loadRandomRecipes(int number) {
        return recipesRemoteDataSource.getRandomRecipes(number);
    }

    public Call<RecipesResults> loadRecipesByQuery(String query,
                                                   String diet,
                                                   String intolerances,
                                                   String cuisine,
                                                   String type,
                                                   String sort,
                                                   String sortDirection,
                                                   int offset) {

        return recipesRemoteDataSource.getRecipesByQuery(query, diet, intolerances, cuisine, type, sort, sortDirection, offset);
    }

    public LiveData<Recipes.Recipe> loadRecipe(int id){
        MutableLiveData<Recipes.Recipe> recipe = new MutableLiveData<>();
        recipesRemoteDataSource.getRecipeById(id).enqueue(new Callback<Recipes.Recipe>() {
            @Override
            public void onResponse(Call<Recipes.Recipe> call, Response<Recipes.Recipe> response) {
                if(response.isSuccessful())
                    recipe.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Recipes.Recipe> call, Throwable t) {

            }
        });
        return recipe;
    }

    public Call<RecipeImage> loadRecipeCard(long id) {
        return recipesRemoteDataSource.getRecipeCard(id);
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
    public static void loadCenterCrop(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .error(R.drawable.example_no_image)
                .centerCrop()
                .into(imageView);
    }

    public LiveData<List<Recipes.Recipe>> getSavedRecipes() {
        MutableLiveData<List<Recipes.Recipe>> recipes = new MutableLiveData<>();
        List<Recipes.Recipe> list = new ArrayList<>();
        recipes.setValue(list);
        firebaseManager.getFavorites().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                appExecutors.diskIO().execute(() -> {
                    Recipes.Recipe recipe = recipeDao.getRecipe(Integer.parseInt(Objects.requireNonNull(snapshot.getKey())));
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

                for (Recipes.Recipe i : list) {
                    if (i.getId().equals(Integer.valueOf(Objects.requireNonNull(snapshot.getKey())))) {
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
        appExecutors.diskIO().execute(() -> recipeDao.setRecipeColor(id, color));
    }

    public Task<Void> saveGroceryList(Recipes.Recipe recipe) {
        appExecutors.diskIO().execute(() -> recipeDao.insert(recipe));
        return firebaseManager.saveGroceryList(new GroceryList(recipe.getId(), recipe.getServings(), recipe.getIngredients().size()));
    }

    public void updateGroceryList(GroceryList gl) {
        firebaseManager.updateGroceryList(gl);
    }


    public Task<Void> deleteGroceryList(int id) {
        firebaseManager.isSaved(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                    firebaseManager.getCookbooks().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds : snapshot.getChildren()){
                                if(ds.child("recipes").hasChild(String.valueOf(id)))
                                    return;
                            }
                            appExecutors.diskIO().execute(() -> recipeDao.deleteRecipe(id));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    Recipes.Recipe recipe = recipeDao.getRecipe(Integer.parseInt(Objects.requireNonNull(snapshot.getKey())));
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

                for (Recipes.Recipe i : list) {
                    if (i.getId().equals(Integer.valueOf(Objects.requireNonNull(snapshot.getKey())))) {
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

    public LiveData<Boolean> isInGroceries(int id) {
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

    public UploadTask setProfilePicture(InputStream inputStream) {
        return firebaseManager.setProfilePicture(inputStream);
    }

    public StorageReference getProfilePicture() {
        return firebaseManager.getProfilePicture();
    }

    public void updateGroceryServings(int id, int servings) {
        firebaseManager.updateGroceryServings(id, servings);
    }

    public LiveData<List<Cookbook>> getCookbooks(){
        MutableLiveData<List<Cookbook>> cookbooks = new MutableLiveData<>();
        List<Cookbook> list = new ArrayList<>();
        firebaseManager.getCookbooks().addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                list.add(snapshot.getValue(Cookbook.class));
                cookbooks.setValue(list);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Cookbook cookbook = snapshot.getValue(Cookbook.class);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId().equals(cookbook.getId())) {
                        list.set(i, cookbook);
                        break;
                    }
                }
                cookbooks.setValue(list);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Cookbook cookbook = snapshot.getValue(Cookbook.class);
                if (!list.isEmpty())
                    for (Cookbook c : list) {
                        if (c.getId().equals(cookbook.getId())) {
                            list.remove(c);
                            break;
                        }
                    }
                cookbooks.setValue(list);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return cookbooks;
    }

    public Task<Void> addToCookbook(String id, Recipes.Recipe recipe){
        appExecutors.diskIO().execute(() -> recipeDao.insert(recipe));
        return firebaseManager.addToCookbook(id, recipe.getId());
    }

    public LiveData<List<String>> getCookbookImages(String id){
        MutableLiveData<List<String>> images = new MutableLiveData<>();
        List<String> list = new ArrayList<>();
        images.setValue(list);
        firebaseManager.getCookbook(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Cookbook cookbook= task.getResult().getValue(Cookbook.class);

                if(cookbook.getRecipes() != null)
                for(String id : cookbook.getRecipes().keySet()){
                    appExecutors.diskIO().execute(() -> {
                        Recipes.Recipe recipe = recipeDao.getRecipe(Integer.parseInt(id));
                        if (recipe != null)
                            list.add(recipe.getImage());
                        else {
                            try {
                                recipe = recipesRemoteDataSource.getRecipeById(Integer.parseInt(id)).execute().body();
                                recipeDao.insert(recipe);
                                list.add(recipe.getImage());
                            } catch (IOException e) {
                                Log.d("tag", "Couldn't retrieve saved recipes");
                            }
                        }
                        images.postValue(list);
                    });
                    if(list.size() == 3) break;
                }
            }
        });
        return images;
    }

    public MutableLiveData<Cookbook> getCookbook(String id){
        MutableLiveData<Cookbook> data = new MutableLiveData<>();
        Cookbook tmp = new Cookbook(null, id);
        List<Recipes.Recipe> recipes = new ArrayList<>();
        firebaseManager.getCookbook(id).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    tmp.setName(snapshot.getValue(String.class));
                    data.setValue(tmp);
                }
                else
                    data.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebaseManager.getCookbook(id).child("recipes").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                appExecutors.diskIO().execute(() -> {

                    Recipes.Recipe recipe = recipeDao.getRecipe(Integer.parseInt(Objects.requireNonNull(snapshot.getKey())));
                    if (recipe != null)
                        recipes.add(recipeDao.getRecipe(Integer.parseInt(snapshot.getKey())));
                    else {
                        try {
                            recipe = recipesRemoteDataSource.getRecipeById(Integer.parseInt(snapshot.getKey())).execute().body();
                            int x = new Random().nextInt(7);
                            int color = Filters.MealType.values()[x].color();
                            Log.d("tag", "x: " + x);
                            Log.d("tag", "color: " + color);

                            recipe.setColor(color);
                            recipeDao.insert(recipe);
                            recipes.add(recipe);
                        } catch (IOException e) {
                            Log.d("tag", "Couldn't retrieve saved recipes");
                        }
                    }
                    tmp.setObjects(recipes);
                    data.postValue(tmp);
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for (Recipes.Recipe i : recipes) {
                    if (i.getId().equals(Integer.valueOf(Objects.requireNonNull(snapshot.getKey())))) {
                        recipes.remove(i);
                        tmp.setObjects(recipes);
                        data.setValue(tmp);
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
        return data;
    }

    public Task<Void> removeFromCookbook(String bookId, int recipeId){

        firebaseManager.isSaved(recipeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    firebaseManager.isInGroceries(recipeId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.exists())
                                appExecutors.diskIO().execute(() -> recipeDao.deleteRecipe(recipeId));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return firebaseManager.removeFromCookbook(bookId, recipeId);

    }

    public void deleteCookbook(String id){
        firebaseManager.getCookbook(id).child("recipes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    removeFromCookbook(id, Integer.parseInt(ds.getKey()));
                }
                firebaseManager.deleteCookbook(id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void changeCookbookName(String id, String name){
        firebaseManager.changeCookbookName(id, name);
    }
}
