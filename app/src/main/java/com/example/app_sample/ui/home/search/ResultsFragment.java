package com.example.app_sample.ui.home.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.local.models.RecipesResults;
import com.example.app_sample.data.remote.api.ApiResponse;
import com.example.app_sample.utils.Utils;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;


public class ResultsFragment extends Fragment {


    public ResultsFragment() {
        // Required empty public constructor
    }

    ResultsAdapter adapter;
    RecyclerView recyclerView;
    SearchViewModel viewModel;
    LinearLayoutManager layoutManager;
    CircularProgressIndicator indicator;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);
        recyclerView = view.findViewById(R.id.recycler_view);
        indicator = view.findViewById(R.id.indicator);
        adapter = new ResultsAdapter(requireContext());
        layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        viewModel.getMutable().observe(getViewLifecycleOwner(), new Observer<ApiResponse<RecipesResults>>() {
            @Override
            public void onChanged(ApiResponse<RecipesResults> recipesResultsApiResponse) {
                if (recipesResultsApiResponse != null) {
                    adapter.setRecipes(recipesResultsApiResponse.body.getRecipes());
                }
            }
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean value) {
                if (value != null) {
                    indicator.setVisibility(value ? View.VISIBLE : View.INVISIBLE);
                    recyclerView.setVisibility(value? View.INVISIBLE : View.VISIBLE);

                }
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results, container, false);
    }


}