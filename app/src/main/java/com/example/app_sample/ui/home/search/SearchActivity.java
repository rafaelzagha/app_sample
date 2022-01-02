package com.example.app_sample.ui.home.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Filter;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.local.models.RecipesResults;
import com.example.app_sample.data.remote.api.ApiResponse;
import com.example.app_sample.ui.MainActivity;
import com.example.app_sample.utils.Utils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    EditText searchBar;
    TextView cancel;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    Intent intent;
    Spinner spinner;
    ChipGroup filterChips;
    SearchFragmentAdapter fragmentAdapter;
    ArrayList<Filter> filters;
    SearchViewModel viewModel;
    MutableLiveData<ApiResponse<RecipesResults>> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        searchBar = findViewById(R.id.search_bar);
        cancel = findViewById(R.id.rewind);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        spinner = findViewById(R.id.spinner);
        filterChips = findViewById(R.id.chip_filters);
        recipes = new MutableLiveData<>(null);

        FragmentManager fm = getSupportFragmentManager();
        fragmentAdapter = new SearchFragmentAdapter(fm, getLifecycle());

        viewPager.setAdapter(fragmentAdapter);
        viewPager.setUserInputEnabled(false);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Filters.Sort.stringValues());
        spinner.setAdapter(spinnerAdapter);

        cancel.setOnClickListener(v -> {
            Intent i = new Intent(SearchActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
        });



        intent = getIntent();
        String query = intent.getStringExtra(Utils.QUERY_KEY);
        filters = (ArrayList<Filter>) intent.getSerializableExtra(Utils.FILTER_KEY);

        if (query == null) query = "";
        searchBar.setText(query);


        Filter diet = null, cuisine = null, mealType = null, sort;
        sort = Filters.Sort.valueOf(spinner.getSelectedItem().toString());

        ArrayList<Filter> intolerances = new ArrayList<>();

        if (filters != null) {
            setUpChipGroup(filterChips, filters);

            for (Filter i : filters) {
                switch (i.group()) {
                    case "diet":
                        diet = i;
                        break;
                    case "intolerances":
                        intolerances.add(i);
                        break;
                    case "cuisine":
                        cuisine = i;
                        break;
                    case "type":
                        mealType = i;
                }
            }


        }
        Log.d("tag", query);
        viewModel.newRequest(query, diet, intolerances, cuisine, mealType, sort).observe(this, new Observer<ApiResponse<RecipesResults>>() {
            @Override
            public void onChanged(ApiResponse<RecipesResults> recipesApiResponse) {
                viewModel.setMutable(recipesApiResponse);
            }
        });
        viewModel.getMutable().observe(this, new Observer<ApiResponse<RecipesResults>>() {
            @Override
            public void onChanged(ApiResponse<RecipesResults> recipesApiResponse) {
                if (recipesApiResponse != null && recipesApiResponse.body != null) {
                    recipes.setValue(recipesApiResponse);
                }
            }
        });


    }

    private void setUpChipGroup(ChipGroup group, ArrayList<Filter> filters) {

        for (Filter i : filters) {
            Chip chip = new Chip(this);

            chip.setText(i.name());
            chip.setCloseIconVisible(true);
            chip.setCheckable(false);
            chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
            chip.setCloseIconTint(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
            chip.setChipStrokeWidth(4);
            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
            chip.setCheckedIconVisible(false);
            group.addView(chip);

        }


    }


    private void updateSearch() {
        //get query again and research
    }
}