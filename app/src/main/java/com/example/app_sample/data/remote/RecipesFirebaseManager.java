package com.example.app_sample.data.remote;

import android.media.MediaPlayer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecipesFirebaseManager {

    DatabaseReference database;
    FirebaseAuth auth;

    public RecipesFirebaseManager() {
        database = FirebaseDatabase.getInstance().getReference("UserData");
        auth = FirebaseAuth.getInstance();
    }

    public void setUsername(String name){
        if(auth.getCurrentUser() != null){
            database.child(auth.getCurrentUser().getUid()).child("username").setValue(name);
        }
    }
}
