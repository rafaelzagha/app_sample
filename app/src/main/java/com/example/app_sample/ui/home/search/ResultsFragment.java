package com.example.app_sample.ui.home.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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

import java.util.ArrayList;


public class ResultsFragment extends Fragment {


    public ResultsFragment() {
        // Required empty public constructor
    }


    ResultsAdapter adapter;
    RecyclerView recyclerView;
    LiveData<ApiResponse<RecipesResults>> recipes;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new ResultsAdapter(requireContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        recipes = ((SearchActivity) getActivity()).recipes; //change to getter later

        recipes.observe(getViewLifecycleOwner(), new Observer<ApiResponse<RecipesResults>>() {
            @Override
            public void onChanged(ApiResponse<RecipesResults> recipesApiResponse) {
                if(recipesApiResponse != null && recipesApiResponse.getBody() != null){
                    adapter.setRecipes(recipesApiResponse.getBody().getRecipes());
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