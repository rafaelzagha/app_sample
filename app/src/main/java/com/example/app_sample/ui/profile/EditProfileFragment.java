package com.example.app_sample.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.utils.CustomProgressDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

@SuppressWarnings("FieldCanBeLocal")
public class EditProfileFragment extends Fragment {

    private ProfileViewModel viewModel;
    private Toolbar toolbar;
    private TextInputLayout usernameLayout;
    private TextInputEditText username, email;
    private Button save;
    private ImageView image;
    private ActivityResultLauncher<Intent> resultLauncher;

    public EditProfileFragment() {
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

        viewModel.getPicture().observe(getViewLifecycleOwner(), s -> {
            if (!s.equals("error"))
                RecipeRepository.loadImage(getContext(), s, image);
        });

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

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {


                        InputStream inputStream;
                        CustomProgressDialog dialog = new CustomProgressDialog();
                        dialog.show(getChildFragmentManager(), "dialog", "Updating photo");
                        try {
                            assert result.getData() != null;
                            inputStream = requireContext().getContentResolver().openInputStream(result.getData().getData());

                            viewModel.setProfilePicture(inputStream).addOnFailureListener(exception -> {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Failed to update profile picture", Toast.LENGTH_SHORT).show();
                            }).addOnSuccessListener(taskSnapshot -> {
                                dialog.dismiss();
                                Toast.makeText(getContext(), "Profile picture updated successfully", Toast.LENGTH_SHORT).show();
                                viewModel.fetchProfilePicture();
                            });

                        } catch (FileNotFoundException e) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Error retrieving image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        image.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            resultLauncher.launch(intent);
        });

        save.setOnClickListener(v -> viewModel.setUsername(Objects.requireNonNull(username.getText()).toString()).addOnCompleteListener(task -> snack()));


        return view;
    }

    protected void snack() {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        Snackbar.make(requireActivity().findViewById(android.R.id.content), "Username updated successfully", Snackbar.LENGTH_SHORT).setAnchorView(bottomNavigationView).show();
    }

}