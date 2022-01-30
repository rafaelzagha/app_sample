package com.example.app_sample.ui.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Filter;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.utils.Constants;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {
    Toolbar toolbar;
    ChipGroup dishTypes, diets, intolerances, cuisines;
    Button show_results;
    TextView clear_all;
    boolean visible;
    MutableLiveData<Integer> checked;
    ColorStateList chipColor;
    ColorStateList textColor;
    CompoundButton.OnCheckedChangeListener changeListener;
    ArrayList<Filter> filters;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        filters = (ArrayList<Filter>) intent.getSerializableExtra(Constants.FILTER_KEY);
        query = intent.getStringExtra(Constants.QUERY_KEY);
        if(filters == null) filters = new ArrayList<>();


        clear_all = findViewById(R.id.clear_all);
        show_results = findViewById(R.id.show_results);
        visible = false;

        checked = new MutableLiveData<>();
        checked.setValue(0);

        setupChipGroups();

        checked.observe(FilterActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                ArrayList<Filter> checked = getCheckedFilters();
                setVisibility(!filters.equals(checked) && checked.size()>0);
            }
        });

        clear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dishTypes.clearCheck();
                diets.clearCheck();
                intolerances.clearCheck();
                cuisines.clearCheck();
                checked.setValue(0);
            }
        });



        show_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filters = getCheckedFilters();
                setResult(Activity.RESULT_OK, new Intent().putExtra(Constants.QUERY_KEY, query).putExtra(Constants.FILTER_KEY, filters));
                finish();
            }
        });


    }

    private void setupChipGroups() {

        dishTypes = findViewById(R.id.chip_group_dish_type);
        diets = findViewById(R.id.chip_group_diets);
        intolerances = findViewById(R.id.chip_group_intolerances);
        cuisines = findViewById(R.id.chip_group_cuisines);

        chipColor = AppCompatResources.getColorStateList(this, R.color.chip_color);
        textColor = AppCompatResources.getColorStateList(this, R.color.chip_text_color);
        changeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checked.setValue(checked.getValue()+1);
                } else {
                    checked.setValue(checked.getValue()-1);

                }
            }
        };

        setUpChipGroup(dishTypes, Filters.MealType.values());
        setUpChipGroup(diets, Filters.Diet.values());
        setUpChipGroup(intolerances, Filters.Intolerance.values());
        setUpChipGroup(cuisines, Filters.Cuisine.values());



    }

    public void setVisibility(boolean set){
        if(set != visible){
            if(!visible) {
                show_results.setVisibility(View.VISIBLE);
                TranslateAnimation animate = new TranslateAnimation(
                        0,                 // fromXDelta
                        0,                 // toXDelta
                        show_results.getHeight(),  // fromYDelta
                        0);                // toYDelta
                animate.setDuration(100);
                animate.setFillAfter(true);
                show_results.startAnimation(animate);
            }
            else{
                TranslateAnimation animate = new TranslateAnimation(
                        0,                 // fromXDelta
                        0,                 // toXDelta
                        0,                 // fromYDelta
                        200); // toYDelta
                animate.setDuration(100);
                animate.setFillAfter(true);
                show_results.startAnimation(animate);
            }

            visible = !visible;
        }


    }

    public void setUpChipGroup(ChipGroup group, Filter[] list) {

        for (Filter i : list) {

            Chip chip = new Chip(this);
            chip.setText(i.name());
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setChipBackgroundColor(chipColor);
            chip.setTextColor(textColor);
            for(Filter f : filters){ //set before listener
                if(f.name().equals(i.name()))
                    chip.setChecked(true);
            }
            chip.setOnCheckedChangeListener(changeListener);
            group.addView(chip);
        }

    }

    public ArrayList<Filter> getCheckedFilters(){
        ArrayList<Filter> filters = new ArrayList<>();
        List<Integer> ids = dishTypes.getCheckedChipIds();
        for(int id : ids){
            Chip chip = dishTypes.findViewById(id);
            filters.add(Filters.MealType.valueOf(chip.getText().toString()));
        }

        ids = diets.getCheckedChipIds();
        for(int id : ids){
            Chip chip = diets.findViewById(id);
            filters.add(Filters.Diet.valueOf(chip.getText().toString()));
        }

        ids = intolerances.getCheckedChipIds();
        for(int id : ids){
            Chip chip = intolerances.findViewById(id);
            filters.add(Filters.Intolerance.valueOf(chip.getText().toString()));
        }

        ids = cuisines.getCheckedChipIds();
        for(int id : ids){
            Chip chip = cuisines.findViewById(id);
            filters.add(Filters.Cuisine.valueOf(chip.getText().toString()));
        }

        return filters;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

}