package com.example.app_sample.ui;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.remote.FirebaseManager;
import com.example.app_sample.ui.home.HomeFragment;
import com.example.app_sample.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleLink();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.fragmentContainerView);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        bottomNavigationView.setOnItemReselectedListener(item -> {
            if(navController.getCurrentDestination().getId() != bottomNavigationView.getSelectedItemId())
                navController.navigate(item.getItemId());
        });

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                View v = getCurrentFocus();
                if(v != null)
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.homeFragment)
            startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        else
            navController.navigateUp();
    }

    private void handleLink() {
        String str = getIntent().getStringExtra(Constants.RECIPE_KEY);
        if(str != null){
            if(str.contains("recipe")){
                String id = str.substring(str.lastIndexOf("/") + 1);
                new RecipeRepository(getApplication()).loadRecipe(Integer.parseInt(id)).observe(this, new Observer<Recipes.Recipe>() {
                    @Override
                    public void onChanged(Recipes.Recipe recipe) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.RECIPE_KEY, recipe);
                        navController.navigate(R.id.recipeFragment, bundle);
                    }
                });
            }
        }
    }
}