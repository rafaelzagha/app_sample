package com.example.app_sample.ui;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.remote.FirebaseManager;
import com.example.app_sample.ui.home.HomeFragment;
import com.example.app_sample.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    NavController navController;
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = ((NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView)).getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        handleLink();
        bottomNavigationView.setOnItemReselectedListener(item -> {
            if (navController.getCurrentDestination().getId() != bottomNavigationView.getSelectedItemId())
                navController.navigate(item.getItemId());
        });

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                View v = getCurrentFocus();
                if (v != null)
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        setupReceiver();

    }

    private void setupReceiver() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Recipe image downloaded successfully", Snackbar.LENGTH_SHORT)
                        .setAnchorView(bottomNavigationView)
                        .setAction("Go to image", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent fileIntent = new Intent(Intent.ACTION_VIEW);
                                Uri mostRecentDownload = Uri.parse(intent.getStringExtra("uri"));
                                fileIntent.setDataAndType(mostRecentDownload, "image/*");
                                startActivity(fileIntent);

                            }
                        });
                snackbar.show();
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(Constants.DOWNLOAD_COMPLETE));
    }

    @Override
    public void onBackPressed() {
        if(navController.getCurrentDestination().getId() == R.id.homeFragment && link != null)
            super.onBackPressed();
        else
            navController.navigateUp();
    }

    private void handleLink() {
        link = getIntent().getStringExtra(Constants.URL_KEY);
        Pattern recipePattern = Pattern.compile("www.mealmatch.com/recipe/+\\d{6}$");
        Pattern cookbookPattern = Pattern.compile("www.mealmatch.com/cookbook/[a-zA-Z0-9]{1,128}/[a-zA-Z0-9-._]{20}$");

        if (link != null) {
            Matcher recipe = recipePattern.matcher(link);
            Matcher cookbook = cookbookPattern.matcher(link);

            if (recipe.find()) {
                String id = link.substring(link.lastIndexOf("/") + 1);
                Log.d("tag", id);
                new RecipeRepository(getApplication()).loadRecipe(Integer.parseInt(id)).observe(this, new Observer<Recipes.Recipe>() {
                    @Override
                    public void onChanged(Recipes.Recipe recipe) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.RECIPE_KEY, recipe);
                        navController.navigate(R.id.recipeFragment, bundle);
                    }
                });
            } else if (cookbook.find()) {

                Pattern uidPattern = Pattern.compile("(?<=cookbook/)[a-zA-Z0-9]{1,128}(?=/[a-zA-Z0-9-._])");
                Pattern cookbookIdPattern = Pattern.compile("(?<=cookbook/[a-zA-Z0-9]{1,128}/)[a-zA-Z0-9-._]{20}$");
                Matcher uidMatcher = uidPattern.matcher(link);
                Matcher cookbookIdMatcher = cookbookIdPattern.matcher(link);

                String uid = null, cookbookId = null;
                if (uidMatcher.find())
                    uid = uidMatcher.group();

                if (cookbookIdMatcher.find())
                    cookbookId = cookbookIdMatcher.group();

                Log.d("tag", uid);
                Log.d("tag", cookbookId);

                Bundle bundle = new Bundle();
                bundle.putString("uid", uid);
                bundle.putString("id", cookbookId);
                navController.navigate(R.id.cookbookPageFragment, bundle);

            }
        }
    }
}