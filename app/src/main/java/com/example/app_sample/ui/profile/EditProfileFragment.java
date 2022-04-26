package com.example.app_sample.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditProfileFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private ProfileViewModel viewModel;
    private Toolbar toolbar;
    private TextInputLayout usernameLayout;
    private TextInputEditText username, email;
    private Button save;
    private ImageView image;

    public EditProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        toolbar = view.findViewById(R.id.toolbar);
        usernameLayout = view.findViewById(R.id.username_layout);
        username = view.findViewById(R.id.username_et);
        email = view.findViewById(R.id.email_et);
        save = view.findViewById(R.id.save);
        image = view.findViewById(R.id.image);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());


        viewModel.getUsername().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                username.setText(s);
                viewModel.getUsername().removeObserver(this);
            }
        });

        viewModel.getPicture().observe(getViewLifecycleOwner(), s -> RecipeRepository.loadImage(getContext(), s, image));

        email.setText(viewModel.getEmail());

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                usernameLayout.setError(s.toString().isEmpty() ? getString(R.string.ui_required_field) : null);
                save.setEnabled(!s.toString().equals(viewModel.getUsername().getValue()) && !s.toString().isEmpty());
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        save.setOnClickListener(v -> viewModel.setUsername(username.getText().toString()).addOnCompleteListener(task -> snack("Username updated successfully")));


        return view;
    }

    protected void snack(String msg) {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        Snackbar.make(requireActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).setAnchorView(bottomNavigationView).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

            InputStream inputStream = null;
            try {
                inputStream = requireContext().getContentResolver().openInputStream(data.getData());

                viewModel.setProfilePicture(inputStream).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getContext(), "Failed to update profile picture", Toast.LENGTH_SHORT).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Profile picture updated successfully", Toast.LENGTH_SHORT).show();
                        viewModel.fetchProfilePicture();
                    }
                });

            } catch (FileNotFoundException e) {
                Toast.makeText(getContext(), "Error retrieving image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}