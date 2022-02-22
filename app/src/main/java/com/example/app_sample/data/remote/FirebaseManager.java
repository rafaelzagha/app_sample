package com.example.app_sample.data.remote;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseManager {

    DatabaseReference database;
    FirebaseAuth auth;

    public FirebaseManager() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("UserData").child(auth.getCurrentUser().getUid());
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

    public void saveRecipe(int id) {
        database.child("saved").child(String.valueOf(id)).setValue(
                database.push().getKey());
    }

    public void deleteRecipe(int id){
        database.child("saved").child(String.valueOf(id)).removeValue();
    }

    public Query getFavorites() {
        return database.child("saved").orderByValue();
    }

    public Query isSaved(int id) {
        return database.child("saved").child(String.valueOf(id));
    }
}