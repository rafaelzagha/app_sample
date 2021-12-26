package com.example.app_sample.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.animation.ObjectAnimator;
import android.app.Notification;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_sample.R;
import com.example.app_sample.models.Category;
import com.example.app_sample.utils.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    ChipGroup dishTypes, diets, intolerances, cuisines;
    Button show_results;
    TextView clear_all;
    boolean visible;
    MutableLiveData<Integer> checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        clear_all = findViewById(R.id.clear_all);
        show_results = findViewById(R.id.show_results);
        visible = false;


        checked = new MutableLiveData<>();
        checked.setValue(0);

        setupChipGroups();

        checked.observe(FilterActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                setVisibility(integer>0);
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


    }

    private void setupChipGroups() {

        dishTypes = findViewById(R.id.chip_group_dish_type);
        diets = findViewById(R.id.chip_group_diets);
        intolerances = findViewById(R.id.chip_group_intolerances);
        cuisines = findViewById(R.id.chip_group_cuisines);

        ColorStateList color = AppCompatResources.getColorStateList(this, R.color.chip_color);
        ColorStateList textColor = AppCompatResources.getColorStateList(this, R.color.chip_text_color);
        CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checked.setValue(checked.getValue()+1);
                } else {
                    checked.setValue(checked.getValue()-1);

                }
            }
        };

        for (String i : Utils.getDishTypes()) {
            Chip chip = new Chip(this);
            chip.setText(i);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setTextColor(textColor);
            chip.setChipBackgroundColor(color);
            chip.setOnCheckedChangeListener(changeListener);
            dishTypes.addView(chip);
        }
        for (String i : Utils.getDiets()) {
            Chip chip = new Chip(this);
            chip.setText(i);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setTextColor(textColor);
            chip.setChipBackgroundColor(color);
            chip.setOnCheckedChangeListener(changeListener);
            diets.addView(chip);
        }
        for (String i : Utils.getIntolerances()) {
            Chip chip = new Chip(this);
            chip.setText(i);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setTextColor(textColor);
            chip.setChipBackgroundColor(color);
            chip.setOnCheckedChangeListener(changeListener);
            intolerances.addView(chip);
        }
        for (Category i : Utils.getCuisines()) {
            Chip chip = new Chip(this);
            chip.setText(i.getName());
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setTextColor(textColor);
            chip.setChipBackgroundColor(color);
            chip.setOnCheckedChangeListener(changeListener);
            cuisines.addView(chip);
        }

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}