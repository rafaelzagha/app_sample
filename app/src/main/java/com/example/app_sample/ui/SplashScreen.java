package com.example.app_sample.ui;

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

public class SplashScreen extends AppCompatActivity {

    ImageView img;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        img = findViewById(R.id.logo);
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        img.startAnimation(fade);


        Log.d("tag", "run");
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreen.this, IntroActivity.class));
            }
        }, 1500);
    }

}