package com.example.app_sample.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Cookbook;
import com.example.app_sample.data.local.models.Recipes;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class ProfileViewModel extends AndroidViewModel {

    private final RecipeRepository repo;
    private final LiveData<List<Recipes.Recipe>> recipes;
    private final LiveData<List<Cookbook>> cookbooks;
    private final LiveData<String> username;
    private final MutableLiveData<String> picture;
    private final String email;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repo = new RecipeRepository(application);
        username = repo.getUsername();
        email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        picture = new MutableLiveData<>();
        recipes = repo.getSavedRecipes();
        cookbooks = repo.getCookbooks();
        fetchProfilePicture();
    }

    public LiveData<Cookbook> getCookbook(String id) {
        return repo.getCookbook(id);
    }
    public LiveData<Cookbook> getPublicCookbook(String uid, String id){
        return repo.getPublicCookbook(uid, id);
    }

    public void savePublicCookbook(Cookbook cookbook){
        repo.savePublicCookbook(cookbook);
    }

    public LiveData<List<Recipes.Recipe>> getRecipes() {
        return recipes;
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public LiveData<String> getPublicUsername(String uid){
        return repo.getPublicUsername(uid);
    }

    public Task<Void> setUsername(String str){
        return repo.setUsername(str);
    }

    public String getEmail() {
        return email;
    }

    public LiveData<List<Cookbook>> getCookbooks() {
        return cookbooks;
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

    public LiveData<List<String>> getCookbookImages(String id) {
        return repo.getCookbookImages(id);
    }

    public void removeFromCookbook(String cbId, int recipeId) {
        repo.removeFromCookbook(cbId, recipeId);
    }

    public void deleteCookbook(String id) {
        repo.deleteCookbook(id);
    }

    public void changeCookbookName(String id, String name) {
        repo.changeCookbookName(id, name);
    }

    public void addToCookbook(String bookID, int recipeId){
        repo.addToCookbook(bookID, recipeId);
    }
}
