package com.example.app_sample.ui.search;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Filter;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.utils.Constants;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;

@SuppressWarnings({"FieldCanBeLocal", "unchecked"})
public class SearchFragment extends Fragment {

    private EditText searchBar;
    private Button clearText;
    private ChipGroup filterChips;
    private SearchViewModel viewModel;
    private ArrayList<Filter> filters;
    private Filter diet, cuisine, mealType, sort;
    private ArrayList<Filter> intolerances;
    private String query;
    private boolean firstSpinnerSelection;
    private Toolbar toolbar;
    private ActivityResultLauncher<Intent> activityResultLaunch;
    private ResultsAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CircularProgressIndicator indicator;
    private TextView tv_filters, tv_sorting;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;


    public SearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        tv_sorting = view.findViewById(R.id.tv_sorting);
        tv_filters = view.findViewById(R.id.tv_filters);
        toolbar = view.findViewById(R.id.toolbar);
        searchBar = view.findViewById(R.id.search_bar);
        filterChips = view.findViewById(R.id.chip_filters);
        recyclerView = view.findViewById(R.id.recycler_view);
        indicator = view.findViewById(R.id.indicator);
        clearText = view.findViewById(R.id.clear_text);
        adapter = new ResultsAdapter(requireContext(), this);
        spinner = view.findViewById(R.id.spinner);
        layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        firstSpinnerSelection = true;

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, Filters.Sort.stringValues());
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sort = Filters.Sort.valueOf(parent.getSelectedItem().toString());
                String str = sort.name() + ((Filters.Sort)sort).getOrder();
                tv_sorting.setText(str);
                if (firstSpinnerSelection)
                    firstSpinnerSelection = false;
                else
                    doSearch(true);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData() != null) {
                        filters = (ArrayList<Filter>) result.getData().getSerializableExtra(Constants.FILTER_KEY);
                        doSearch(true);
                        setUpChipGroup(filterChips);
                    }
                });

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        toolbar.setNavigationOnClickListener(V -> requireActivity().onBackPressed());

        searchBar.setText(query);

        tv_filters.setOnClickListener(v -> {
            Intent newIntent = new Intent(requireContext(), FilterActivity.class);
            newIntent.putExtra(Constants.FILTER_KEY, filters);
            newIntent.putExtra(Constants.QUERY_KEY, query);
            activityResultLaunch.launch(newIntent);

        });

        tv_sorting.setOnClickListener(v -> spinner.performClick());

        if (viewModel.getRecipes().getValue() == null)
            doSearch(true);

        setUpChipGroup(filterChips);

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

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                clearText.setVisibility(s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });

        clearText.setOnClickListener(v -> searchBar.setText(""));


        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipesResults -> {
            if (recipesResults != null) {
                adapter.setRecipes(recipesResults.getRecipes());
            }
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), value -> {
            if (value != null) {
                indicator.setVisibility(value ? View.VISIBLE : View.INVISIBLE);
                recyclerView.setVisibility(value ? View.INVISIBLE : View.VISIBLE);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), s -> {
            if (s != null) {
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
                indicator.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            query = getArguments().getString(Constants.QUERY_KEY);
        }
        filters = (ArrayList<Filter>) getArguments().getSerializable(Constants.FILTER_KEY);

        if (query == null) query = "";
        if (filters == null) filters = new ArrayList<>();
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
            chip.setOnCloseIconClickListener(listener);
            group.addView(chip);

        }


    }


    protected void doSearch(boolean overwrite) {

        if (overwrite) {
            diet = null;
            intolerances = new ArrayList<>();
            cuisine = null;
            mealType = null;

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
            viewModel.setLoading(true);
        }

        viewModel.newRequest(overwrite, query, diet, intolerances, cuisine, mealType, sort);

    }

    public void goToRecipePage(Recipes.Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RECIPE_KEY, recipe);
        NavHostFragment.findNavController(this).navigate(R.id.global_to_recipeFragment_horizontal, bundle);
    }

}