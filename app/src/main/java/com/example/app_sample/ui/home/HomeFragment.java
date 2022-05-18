package com.example.app_sample.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Filter;
import com.example.app_sample.ui.search.FilterActivity;
import com.example.app_sample.utils.Constants;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

@SuppressWarnings("FieldCanBeLocal")
public class HomeFragment extends Fragment {

    private EditText search;
    private TextView username;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private HomeAdapter homeAdapter;
    private AppBarLayout appBarLayout;
    private CardView filter;
    private ActivityResultLauncher<Intent> activityResultLaunch;
    private HomeViewModel viewModel;

    public HomeFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        viewModel = ViewModelProviders.of(requireActivity()).get(HomeViewModel.class);
        search = view.findViewById(R.id.et_search);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        appBarLayout = view.findViewById(R.id.appbar);
        username = view.findViewById(R.id.username);
        filter = view.findViewById(R.id.filter);
        homeAdapter = new HomeAdapter(getChildFragmentManager(), getLifecycle());

        viewModel.getUsername().observe(getViewLifecycleOwner(), str -> username.setText(String.format("Hi, %s", str)));

        viewPager.setAdapter(homeAdapter);
        viewPager.setUserInputEnabled(false);

        activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData() != null) {
                        @SuppressWarnings("unchecked")
                        ArrayList<Filter> filters = (ArrayList<Filter>) result.getData().getSerializableExtra(Constants.FILTER_KEY);
                        goToSearchScreen(null, filters);
                    }
                });


        View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0);
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
        p.setMargins(0, 0, 50, 0);
        tab.requestLayout();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                appBarLayout.setExpanded(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


        filter.setOnClickListener(v -> activityResultLaunch.launch(new Intent(requireContext(), FilterActivity.class)));


        search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (search.getText().toString().length() >= 3) {
                    goToSearchScreen(search.getText().toString(), null);
                    search.setText("");
                }
            }
            return false;
        });

        return view;
    }

    public void goToSearchScreen(String query, ArrayList<Filter> filters) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.QUERY_KEY, query);
        bundle.putSerializable(Constants.FILTER_KEY, filters);
        NavHostFragment.findNavController(this).navigate(R.id.global_to_searchFragment, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        tabLayout.selectTab(tabLayout.getTabAt(viewPager.getCurrentItem()));
    }
}