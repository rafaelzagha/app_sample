package com.example.app_sample.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.app_sample.R;
import com.example.app_sample.data.remote.FirebaseManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    ImageView img;
    Handler handler;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        img = findViewById(R.id.logo);
        firebaseAuth = FirebaseAuth.getInstance();
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        img.startAnimation(fade);


        new FirebaseManager().isSaved(716231);

        handler = new Handler();
        handler.postDelayed(() -> {
            if(firebaseAuth.getCurrentUser() == null)
                startActivity(new Intent(SplashScreen.this, IntroActivity.class));
            else
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
        }, 1500);
    }

}