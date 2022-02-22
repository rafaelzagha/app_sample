package com.example.app_sample.utils;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_sample.ui.recipe.RecipeViewModel;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private int mParam;


    public MyViewModelFactory(Application application, int param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new RecipeViewModel(mApplication, mParam);
    }
}