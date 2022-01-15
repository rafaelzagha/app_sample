package com.example.app_sample.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Filter;
import com.example.app_sample.ui.MainActivity;
import com.example.app_sample.ui.recipe.RecipeFragment;
import com.example.app_sample.ui.search.FilterActivity;
import com.example.app_sample.ui.search.SearchFragment;
import com.example.app_sample.utils.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    EditText search;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    HomeAdapter homeAdapter;
    AppBarLayout appBarLayout;
    CardView filter;
    ActivityResultLauncher<Intent> activityResultLaunch;

    public HomeFragment() {}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        search = view.findViewById(R.id.et_search);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        filter = view.findViewById(R.id.filter);

        homeAdapter = new HomeAdapter(getChildFragmentManager(), getLifecycle());

        viewPager.setAdapter(homeAdapter);
        viewPager.setUserInputEnabled(false);  //disable swiping

        activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getData() != null){
                            ArrayList<Filter> filters = (ArrayList<Filter>) result.getData().getSerializableExtra(Utils.FILTER_KEY);
                            SearchFragment resultsFragment = SearchFragment.newInstance("", filters);
                            ((MainActivity) requireActivity()).setFragment(resultsFragment, Utils.ANIMATE_SLIDE_VERTICAL);
                        }
                    }
                });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 50, 0);
            tab.requestLayout();
        }


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

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
                appBarLayout.setExpanded(true);
            }
        });


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //activityResultLaunch.launch(new Intent(requireContext(), FilterActivity.class));
                ((MainActivity)getActivity()).setFragment(new RecipeFragment(), Utils.ANIMATE_SLIDE_HORIZONTAL);
            }
        });


        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (search.getText().toString().length() >= 3) {
                        SearchFragment resultsFragment = SearchFragment.newInstance(search.getText().toString(), null);
                        ((MainActivity) getActivity()).setFragment(resultsFragment, Utils.ANIMATE_SLIDE_VERTICAL);
                        search.setText("");
                    }
                }
                return false;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

}