package com.example.app_sample.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.ui.favorites.FavoritesFragment;
import com.example.app_sample.ui.groceries.GroceriesFragment;
import com.example.app_sample.ui.home.HomeFragment;
import com.example.app_sample.ui.profile.ProfileFragment;
import com.example.app_sample.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        fragmentManager = getSupportFragmentManager();


        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


    }

    public void setFragment(Fragment fragment, int animate) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (animate == Utils.ANIMATE_SLIDE_HORIZONTAL)
            fragmentTransaction.setCustomAnimations(R.anim.slide_right, R.anim.slide_left, R.anim.slide_right, R.anim.slide_left);

        else if (animate == Utils.ANIMATE_SLIDE_VERTICAL)
            fragmentTransaction.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, 0, R.anim.slide_down);

        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);

        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    public void popStack() {
        fragmentManager.popBackStack();
    }

}