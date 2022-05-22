package com.example.app_sample.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = ((NavHostFragment) Objects.requireNonNull(getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView))).getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        handleLink();
        bottomNavigationView.setOnItemReselectedListener(item -> {
            if (Objects.requireNonNull(navController.getCurrentDestination()).getId() != bottomNavigationView.getSelectedItemId())
                navController.navigate(item.getItemId());
        });

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            View v = getCurrentFocus();
            if (v != null)
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            if (getSupportActionBar() != null)
                getSupportActionBar().hide();
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
                        .setAction("Go to image", v -> {
                            Intent fileIntent = new Intent(Intent.ACTION_VIEW);
                            Uri mostRecentDownload = Uri.parse(intent.getStringExtra("uri"));
                            fileIntent.setDataAndType(mostRecentDownload, "image/*");
                            startActivity(fileIntent);

                        });
                snackbar.show();
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(Constants.DOWNLOAD_COMPLETE));
    }

    @Override
    public void onBackPressed() {
        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.homeFragment && link != null)
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
                new RecipeRepository(getApplication()).loadRecipe(Integer.parseInt(id)).observe(this, recipe1 -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.RECIPE_KEY, recipe1);
                    navController.navigate(R.id.recipeFragment, bundle);
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

                Bundle bundle = new Bundle();
                bundle.putString("uid", uid);
                bundle.putString("id", cookbookId);
                navController.navigate(R.id.cookbookPageFragment, bundle);

            }
        }
    }
}