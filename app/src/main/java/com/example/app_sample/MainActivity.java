package com.example.app_sample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.app_sample.api.FoodApi;
import com.example.app_sample.api.FoodService;
import com.example.app_sample.models.Recipes;
import com.example.app_sample.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new HomeFragment()).commit();
                return true;

            case R.id.explore_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new ExploreFragment()).commit();
                return true;

            case R.id.shopping_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new ShoppingFragment()).commit();
                return true;
        }
        return false;
    }

}