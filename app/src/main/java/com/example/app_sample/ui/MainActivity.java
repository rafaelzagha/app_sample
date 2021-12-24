package com.example.app_sample.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.app_sample.R;
import com.example.app_sample.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new HomeFragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new HomeFragment()).commit();
                return true;

            case R.id.favorites_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new FavoritesFragment()).commit();
                return true;

            case R.id.shopping_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new ShoppingFragment()).commit();
                return true;

            case R.id.profile_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new ProfileFragment()).commit();
                return true;
        }
        return false;
    }

}