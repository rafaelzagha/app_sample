package com.example.app_sample.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.ui.intro.IntroActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    ProfileAdapter adapter;
    ProfileViewModel viewModel;
    TextView recipesNum, CookbooksNum, name, email;
    ImageView logout, editProfile, picture;
    BottomSheetDialog dialog;
    LinearLayout editImage, editEmail, editUsername;
    MaterialAlertDialogBuilder alertDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        picture = view.findViewById(R.id.image);
        email = view.findViewById(R.id.email);
        name = view.findViewById(R.id.name);
        recipesNum = view.findViewById(R.id.recipes_num);
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tab_layout);
        logout = view.findViewById(R.id.logout);
        editProfile = view.findViewById(R.id.edit_profile);
        adapter = new ProfileAdapter(getChildFragmentManager(), getLifecycle());

        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewModel.getUsername().observe(getViewLifecycleOwner(), string -> name.setText(string));

        viewModel.getSaved().observe(getViewLifecycleOwner(), integer -> recipesNum.setText(String.valueOf(integer)));

        email.setText(viewModel.getEmail());

        viewModel.getPicture().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.equals("error"))
                    RecipeRepository.loadImage(getContext(), s, picture);
            }
        });

        setupDialog();
    }

    private void setupDialog() {
        dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(R.layout.dialog_profile);
        dialog.setCancelable(true);
        editProfile.setOnClickListener(v -> dialog.show());

        editImage = dialog.findViewById(R.id.edit_image);
        editEmail = dialog.findViewById(R.id.edit_email);
        editUsername = dialog.findViewById(R.id.edit_username);

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        alertDialog = new MaterialAlertDialogBuilder(requireContext()).setMessage(R.string.signout_confirmation)
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .setPositiveButton(getString(R.string.signout), (dialog, which) -> {
                    dialog.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(requireActivity(), IntroActivity.class));
                });

        logout.setOnClickListener(v -> {
            alertDialog.show();
        });

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