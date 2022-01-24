package com.example.app_sample.ui;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.app_sample.R;
import com.example.app_sample.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.fragmentContainerView);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }

    public void popStack() {
        navController.popBackStack();
    }

    @Override
    public void onBackPressed() {
        if(navController.getCurrentDestination().getId() == R.id.homeFragment)
            finishAffinity();
        else
            navController.navigateUp();

    }
}