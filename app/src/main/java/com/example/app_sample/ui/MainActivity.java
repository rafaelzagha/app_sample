package com.example.app_sample.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    List<Recipes.Recipe> stack;
    List<Recipes.Recipe> list;

    int stackPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        fragmentManager = getSupportFragmentManager();


        NavController navController = Navigation.findNavController(this,  R.id.fragmentContainerView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }

    public void setFragment(Fragment fragment) {

        fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out).replace(R.id.fragmentContainerView, fragment).addToBackStack(null).commit();
    }

    public List<Recipes.Recipe> getStack() {
        return stack;
    }

    public void setStack(List<Recipes.Recipe> stack) {
        this.stack = stack;
    }

    public int getStackPosition() {
        return stackPosition;
    }

    public void setStackPosition(int stackPosition) {
        this.stackPosition = stackPosition;
    }

    public List<Recipes.Recipe> getList() {
        return list;
    }

    public void setList(List<Recipes.Recipe> list) {
        this.list = list;
    }

    public void popStack(){
        fragmentManager.popBackStack();
    }

}