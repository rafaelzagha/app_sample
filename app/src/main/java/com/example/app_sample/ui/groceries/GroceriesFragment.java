package com.example.app_sample.ui.groceries;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Recipes;

import java.util.List;

public class GroceriesFragment extends Fragment {


    RecyclerView recyclerView;
    GroceriesAdapter adapter;

    public GroceriesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groceries, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new GroceriesAdapter(requireContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        new RecipeRepository(getActivity().getApplication()).getSavedRecipes().observe(getViewLifecycleOwner(), new Observer<List<Recipes.Recipe>>() {
            @Override
            public void onChanged(List<Recipes.Recipe> recipes) {
                adapter.setRecipes(recipes);
            }
        });
    }
}