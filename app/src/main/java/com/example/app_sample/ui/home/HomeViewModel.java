package com.example.app_sample.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.app_sample.data.RecipeRepository;

public class HomeViewModel extends AndroidViewModel {

    private LiveData<String> username;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        username = new RecipeRepository(application).getUsername();
    }

    public LiveData<String> getUsername() {
        return username;
    }
}
