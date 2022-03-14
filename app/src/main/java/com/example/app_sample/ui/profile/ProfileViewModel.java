package com.example.app_sample.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Recipes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.List;

public class ProfileViewModel extends AndroidViewModel {

    private RecipeRepository repo;
    private LiveData<List<Recipes.Recipe>> saved;
    private LiveData<String> username;
    private MutableLiveData<String> picture;
    private String email;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repo = new RecipeRepository(application);
        username = repo.getUsername();
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        picture = new MutableLiveData<>();
        saved = repo.getSavedRecipes();
        fetchProfilePicture();
    }

    public LiveData<List<Recipes.Recipe>> getRecipes() {
        return saved;
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public UploadTask setProfilePicture(InputStream inputStream) {
        return repo.setProfilePicture(inputStream);
    }

    public LiveData<String> getPicture() {
        return picture;
    }

    public void fetchProfilePicture() {
        repo.getProfilePicture().getDownloadUrl()
                .addOnSuccessListener(uri -> picture.setValue(uri.toString()))
                .addOnFailureListener(e -> picture.setValue("error"));
    }

    public void clearTable() {
        repo.clearTable();
    }

    public void deleteRecipe(int id) {
        repo.removeRecipe(id);
    }

    public void setRecipeColor(int id, int color) {
        repo.setRecipeColor(id, color);
    }

    public void addToGroceries(Recipes.Recipe recipe) {
        repo.saveGroceryList(recipe);
    }

    public void deleteFromGroceries(int id) {
        repo.deleteGroceryList(id);
    }

    public LiveData<Boolean> isInGroceries(int id) {
        return repo.isInGroceries(id);
    }
}
