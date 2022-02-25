package com.example.app_sample.utils;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.ui.recipe.RecipeViewModel;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private Recipes.Recipe recipe;


    public MyViewModelFactory(Application application, Recipes.Recipe recipe) {
        mApplication = application;
        this.recipe = recipe;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new RecipeViewModel(mApplication, recipe);
    }
}