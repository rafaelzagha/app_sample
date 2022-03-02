package com.example.app_sample.ui.profile;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app_sample.data.RecipeRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;

public class ProfileViewModel extends AndroidViewModel {

    private RecipeRepository repo;
    private LiveData<Integer> saved;
    private LiveData<String> username;
    private MutableLiveData<String> picture;
    private String email;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repo = new RecipeRepository(application);
        saved = repo.getSavedNum();
        username = repo.getUsername();
        picture = new MutableLiveData<>();
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        fetchProfilePicture();
    }


    public LiveData<Integer> getSaved() {
        return saved;
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public UploadTask setProfilePicture(InputStream inputStream){
        return repo.setProfilePicture(inputStream);
    }

    public LiveData<String> getPicture() {
        return picture;
    }

    public void fetchProfilePicture() {
        repo.getProfilePicture().getDownloadUrl()
                .addOnSuccessListener(uri -> picture.setValue(uri.toString()))
                .addOnFailureListener(e -> picture.setValue("error"));
    }
}
