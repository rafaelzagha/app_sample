package com.example.app_sample.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.ui.intro.IntroActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ProfileAdapter adapter;
    private ProfileViewModel viewModel;
    private TextView recipesNum, cookbooksNum, name, email;
    private ImageView picture;
    private MaterialAlertDialogBuilder alertDialog;
    private Toolbar toolbar;
    private FloatingActionButton add;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        picture = view.findViewById(R.id.image);
        email = view.findViewById(R.id.email);
        name = view.findViewById(R.id.name);
        recipesNum = view.findViewById(R.id.recipes_num);
        cookbooksNum = view.findViewById(R.id.cookbook_num);
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tab_layout);
        add = view.findViewById(R.id.add);
        adapter = new ProfileAdapter(getChildFragmentManager(), getLifecycle());

        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                add.setVisibility(tab.getPosition()==0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewModel.getUsername().observe(getViewLifecycleOwner(), string -> name.setText(string));

        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> recipesNum.setText(String.valueOf(recipes.size())));

        viewModel.getCookbooks().observe(getViewLifecycleOwner(), cookbooks -> cookbooksNum.setText(String.valueOf(cookbooks.size())));

        email.setText(viewModel.getEmail());

        viewModel.getPicture().observe(getViewLifecycleOwner(), s -> {
            if (!s.equals("error"))
                RecipeRepository.loadImage(getContext(), s, picture);
        });

        alertDialog = new MaterialAlertDialogBuilder(requireContext()).setMessage(R.string.signout_confirmation)
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .setPositiveButton(getString(R.string.signout), (dialog, which) -> {
                    dialog.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    viewModel.clearTable();
                    startActivity(new Intent(requireActivity(), IntroActivity.class));
                });

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.edit_profile) {
                NavHostFragment.findNavController(ProfileFragment.this).navigate(R.id.global_to_editProfileFragment);
                return true;
            } else if (item.getItemId() == R.id.logout) {
                alertDialog.show();
                return true;
            }
            return false;

        });

        add.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.global_to_newCookbookFragment));

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        tabLayout.selectTab(tabLayout.getTabAt(viewPager.getCurrentItem()));
    }
}