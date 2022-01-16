package com.example.app_sample.ui.home.discover;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.remote.api.ApiResponse;

public class DiscoverFragment extends Fragment {

    RecyclerView popular, categories;
    HorizontalSwipeAdapter ta, ta2;
    GridView grid;
    LinearLayoutManager l1, l2;
    DiscoverViewModel viewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(DiscoverViewModel.class);
        grid = view.findViewById(R.id.grid);
        popular = view.findViewById(R.id.rv_popular);
        categories = view.findViewById(R.id.rv_categories);

        setupViews();

        newRequest();

        viewModel.getRecipes().observe(getViewLifecycleOwner(), new Observer<ApiResponse<Recipes>>() {
            @Override
            public void onChanged(ApiResponse<Recipes> recipesApiResponse) {
                if(recipesApiResponse != null)
                    ta.setRecipes(recipesApiResponse.getBody().getRecipes());
            }
        });


    }

    public void newRequest(){
        viewModel.newRequest().observe(getViewLifecycleOwner(), recipesApiResponse -> {
            if (recipesApiResponse != null) {
                if (recipesApiResponse.getBody() != null)
                    viewModel.addToRecipes(recipesApiResponse);
                else{
                    Toast.makeText(getActivity(), "Request Error " + recipesApiResponse.getCode(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    private void setupViews() {
        l1 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        ta = new HorizontalSwipeAdapter(getContext(), HorizontalSwipeAdapter.LAYOUT_RECIPE, this);
        popular.setAdapter(ta);
        popular.setLayoutManager(l1);

        l2 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        ta2 =  new HorizontalSwipeAdapter(getContext(), HorizontalSwipeAdapter.LAYOUT_MEALTYPE, this);
        categories.setAdapter(ta2);
        categories.setLayoutManager(l2);

        GridAdapter ga = new GridAdapter(getContext(), this);
        grid.setAdapter(ga);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }
}