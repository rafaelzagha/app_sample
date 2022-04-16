package com.example.app_sample.data.remote;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app_sample.data.local.models.Cookbook;
import com.example.app_sample.data.local.models.GroceryList;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;

public class FirebaseManager {

    DatabaseReference database;
    FirebaseAuth auth;
    StorageReference storage;

    public FirebaseManager() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("UserData").child(auth.getCurrentUser().getUid());
        storage = FirebaseStorage.getInstance().getReference().child("profile_pictures");
    }

    public LiveData<String> getUsername() {
        MutableLiveData<String> username = new MutableLiveData<>();
        database.child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setValue(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return username;
    }

    public Task<Void> setUsername(String name) {
        return database.child("username").setValue(name);
    }

    public Task<Void> saveRecipe(int id) {
        return database.child("saved").child(String.valueOf(id)).setValue(
                database.push().getKey());
    }

    public Task<Void> deleteRecipe(int id) {
        return database.child("saved").child(String.valueOf(id)).removeValue();
    }

    public Query getFavorites() {
        return database.child("saved").orderByValue();
    }

    public Query isSaved(int id) {
        return database.child("saved").child(String.valueOf(id));
    }

    public Task<Void> saveGroceryList(GroceryList gl) {
        gl.setPush(database.push().getKey());
        return database.child("groceries").child(String.valueOf(gl.getId())).setValue(gl);
    }

    public void updateGroceryList(GroceryList gl) {
        database.child("groceries").child(String.valueOf(gl.getId())).child("list").setValue(gl.getList());
    }

    public DatabaseReference getGroceryList(int id) {
        return database.child("groceries").child(String.valueOf(id));
    }

    public Task<Void> deleteGroceryList(int id) {
        return database.child("groceries").child(String.valueOf(id)).removeValue();
    }

    public Query getGroceries() {
        return database.child("groceries").orderByChild("push");
    }

    public DatabaseReference isInGroceries(int id) {
        return database.child("groceries").child(String.valueOf(id));
    }

    public UploadTask setProfilePicture(InputStream inputStream) {
        return storage.child(auth.getUid() + ".jpg").putStream(inputStream);
    }

    public StorageReference getProfilePicture() {
        return storage.child(auth.getUid() + ".jpg");
    }

    public void updateGroceryServings(int id, int servings) {
        database.child("groceries").child(String.valueOf(id)).child("servings").setValue(servings);
    }

    public DatabaseReference getCookbooks() {
        return database.child("cookbooks");
    }

    public String createCookbook(String name) {
        String push = database.push().getKey();
        Cookbook cookbook = new Cookbook(name, push);
        database.child("cookbooks").child(push).setValue(cookbook);
        return cookbook.getId();
    }

    public void deleteCookbook(String id){
        database.child("cookbooks").child(id).removeValue();
    }

    public Task<Void> addToCookbook(String bookID, int id) {
        return database.child("cookbooks").child(bookID).child("recipes").child(String.valueOf(id)).setValue(database.push().getKey());
    }

    public Task<Void> removeFromCookbook(String bookId, int recipeId){
        return database.child("cookbooks").child(bookId).child("recipes").child(String.valueOf(recipeId)).removeValue();
    }

    public DatabaseReference getCookbook(String id) {
        return database.child("cookbooks").child(id);
    }

    public void changeCookbookName(String id, String name){
        database.child("cookbooks").child(id).child("name").setValue(name);
    }
}