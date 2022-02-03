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

import java.util.ArrayList;
import java.util.List;

public class FirebaseManager {

    DatabaseReference database;
    FirebaseAuth auth;

    public FirebaseManager() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("UserData").child(auth.getCurrentUser().getUid());

    }

    public DatabaseReference getUsername(){
        return database.child("username");
    }
    public Task<Void> setUsername(String name) {
        return database.child("username").setValue(name);
    }

    public void saveRecipe(int id) {
        database.child("saved").push().setValue(id);
    }

    public LiveData<List<Integer>> getFavorites() {
        MutableLiveData<List<Integer>> ids = new MutableLiveData<>();
        database.child("saved").orderByKey().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Integer> list = new ArrayList<>();
                    for (DataSnapshot i : task.getResult().getChildren())
                        list.add(i.getValue(Integer.class));
                    ids.setValue(list);
                }
            }
        });
        return ids;
    }

    public LiveData<Boolean> isSaved(int id) {
        MutableLiveData<Boolean> favorite = new MutableLiveData<>();
        database.child("saved").equalTo(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    favorite.setValue(task.getResult().exists());
                    Log.d("tag", id + " exists " + favorite.getValue());
                }

            }
        });
        return favorite;
    }
}