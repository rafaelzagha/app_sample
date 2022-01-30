package com.example.app_sample.data.remote;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.lang.invoke.MutableCallSite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseManager {

    DatabaseReference database;
    FirebaseAuth auth;

    public FirebaseManager() {
        database = FirebaseDatabase.getInstance().getReference("UserData");
        auth = FirebaseAuth.getInstance();
    }

    public Task<Void> setUsername(String name){
        if(auth.getCurrentUser() != null){
            return database.child(auth.getCurrentUser().getUid()).child("username").setValue(name);
        }
        return null;
    }

    public void favoriteRecipe(int id){
        database.child(auth.getCurrentUser().getUid()).child("favorites").push().setValue(id);
    }

    public LiveData<List<Integer>> getFavorites(){
        MutableLiveData<List<Integer>> ids = new MutableLiveData<>();
        database.child(auth.getCurrentUser().getUid()).child("favorites").orderByKey().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                GenericTypeIndicator<HashMap<String, Integer>> genericTypeIndicator = new GenericTypeIndicator<HashMap<String, Integer>>() {};

                ids.setValue(new ArrayList<>(task.getResult().getValue(genericTypeIndicator).values()));
            }
        });
        return ids;
    }




}
