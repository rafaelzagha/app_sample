package com.example.app_sample.ui.home.search;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Filter;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.ui.MainActivity;
import com.example.app_sample.utils.Utils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment {

    EditText searchBar;
    TextView cancel;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    Spinner spinner;
    ChipGroup filterChips;
    SearchFragmentAdapter fragmentAdapter;
    SearchViewModel viewModel;
    ArrayList<Filter> filters;
    Filter diet, cuisine, mealType, sort;
    ArrayList<Filter> intolerances;
    ImageView filterIcon;
    String query;
    Bundle args;
    ArrayAdapter<String> spinnerAdapter;
    ActivityResultLauncher<Intent> activityResultLaunch;


    public SearchFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity()).get(SearchViewModel.class);

        searchBar = view.findViewById(R.id.search_bar);
        cancel = view.findViewById(R.id.rewind);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        spinner = view.findViewById(R.id.spinner);
        filterChips = view.findViewById(R.id.chip_filters);
        filterIcon = view.findViewById(R.id.filter_icon);

        activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    filters = (ArrayList<Filter>) result.getData().getSerializableExtra(Utils.FILTER_KEY);
                    doSearch(true);
                    setUpChipGroup(filterChips);
                    Log.d("tag", Filters.listToString(filters));
                });

        fragmentAdapter = new SearchFragmentAdapter(getChildFragmentManager(), getLifecycle());

        viewPager.setAdapter(fragmentAdapter);
        viewPager.setUserInputEnabled(false);

        spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, Filters.Sort.stringValues());
        spinner.setAdapter(spinnerAdapter);

        cancel.setOnClickListener(v -> ((MainActivity)getActivity()).popStack());

        args = getArguments();
        query = args.getString(Utils.QUERY_KEY);
        filters = (ArrayList<Filter>) args.getSerializable(Utils.FILTER_KEY);

        if (query == null) query = "";
        searchBar.setText(query);

        if (filters == null) filters = new ArrayList<>();

        filterIcon.setOnClickListener(v -> {
            Intent newIntent = new Intent(requireContext(), FilterActivity.class);
            newIntent.putExtra(Utils.FILTER_KEY, filters);
            newIntent.putExtra(Utils.QUERY_KEY, query);
            activityResultLaunch.launch(newIntent);

        });

        doSearch(true);

        setUpChipGroup(filterChips);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sort = Filters.Sort.valueOf(parent.getItemAtPosition(position).toString());
                doSearch(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (searchBar.getText().toString().length() >= 3) {
                    if (!searchBar.getText().toString().equals(query)) {
                        query = searchBar.getText().toString();
                        filters.clear();
                        filterChips.removeAllViews();
                        doSearch(true);
                        return true;
                    } else
                        Toast.makeText(requireContext(), "same search keyword", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(requireContext(), "Search query should be at least 3 letters long", Toast.LENGTH_SHORT).show();
            }
            return false;
        });

        this.getView().setOnKeyListener((v, keyCode, event) -> {
            if(keyCode == KeyEvent.KEYCODE_BACK){
                ((MainActivity)getActivity()).popStack();
                return true;
            }
            return false;
        });
    }

    public static SearchFragment newInstance(String query, ArrayList<Filter> filters) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(Utils.QUERY_KEY, query);
        args.putSerializable(Utils.FILTER_KEY, filters);
        fragment.setArguments(args);
        return fragment;
    }


    private void setUpChipGroup(ChipGroup group) {

        View.OnClickListener listener = v -> {
            int index = group.indexOfChild(v);
            group.removeView(v);
            filters.remove(index);
            doSearch(true);
        };

        group.removeAllViews();

        for (Filter i : filters) {
            Chip chip = new Chip(requireContext());
            chip.setText(i.name());
            chip.setCloseIconVisible(true);
            chip.setCheckable(false);
            chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
            chip.setCloseIconTint(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
            chip.setChipStrokeWidth(4);
            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
            chip.setCheckedIconVisible(false);
            chip.setOnCloseIconClickListener(listener);
            group.addView(chip);

        }


    }

    private void doSearch(boolean overwrite) {
        //diff between refresh and load more
        diet = null;
        intolerances = new ArrayList<>();
        cuisine = null;
        mealType = null;
        sort = Filters.Sort.valueOf(spinner.getSelectedItem().toString());


        for (Filter i : filters) {
            switch (i.group()) {
                case "diet":
                    diet = i;
                    break;
                case "intolerances":
                    intolerances.add(i);
                    break;
                case "cuisine":
                    cuisine = i;
                    break;
                case "type":
                    mealType = i;
            }
        }

        viewModel.newRequest(query, diet, intolerances, cuisine, mealType, sort).observe(getViewLifecycleOwner(), recipesApiResponse -> {
            if (recipesApiResponse != null && recipesApiResponse.body != null) {
                if (overwrite) viewModel.setMutable(recipesApiResponse);

                else viewModel.addToMutable(recipesApiResponse);
            } else {
                Toast.makeText(requireContext(), "Request Error " + recipesApiResponse.getCode(), Toast.LENGTH_SHORT).show();
                viewModel.setLoading(false);
            }
        });
    }

}