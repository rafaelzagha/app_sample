package com.example.app_sample.ui.home.discover;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Filter;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.utils.Constants;

import java.util.ArrayList;

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

        if(viewModel.getRecipes().getValue() != null && viewModel.getRecipes().getValue().getRecipes() != null)
            ta.setRecipes(viewModel.getRecipes().getValue().getRecipes());

        else viewModel.newRequest();

        viewModel.getRecipes().observe(getViewLifecycleOwner(), new Observer<Recipes>() {
            @Override
            public void onChanged(Recipes recipes) {
                if(recipes != null)
                    ta.setRecipes(recipes.getRecipes());

            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), s -> {
            if(s != null){
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
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

    public void newRequest(){
        viewModel.newRequest();
    }

    public void goToRecipePage(Recipes.Recipe recipe){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RECIPE_KEY, recipe);
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_recipeFragment, bundle);
    }

    public void goToSearchScreen(String query, ArrayList<Filter> filters){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.QUERY_KEY, query);
        bundle.putSerializable(Constants.FILTER_KEY, filters);
        NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_searchFragment, bundle);
    }
}