package com.example.app_sample.ui.home.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Filter;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.utils.Constants;

import java.util.ArrayList;

public class DiscoverFragment extends Fragment {

    private RecyclerView popular, categories, grid;
    private HorizontalSwipeAdapter ta, ta2;
    private DiscoverViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        viewModel = ViewModelProviders.of(requireActivity()).get(DiscoverViewModel.class);
        grid = view.findViewById(R.id.grid);
        popular = view.findViewById(R.id.rv_popular);
        categories = view.findViewById(R.id.rv_categories);

        setupViews();

        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> ta.setRecipes(recipes.getRecipes()));

        viewModel.getError().observe(getViewLifecycleOwner(), s -> {
            if(s != null){
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
            }

        });

        return view;
    }

    private void setupViews() {
        ta = new HorizontalSwipeAdapter(getContext(), HorizontalSwipeAdapter.LAYOUT_RECIPE, this);
        popular.setAdapter(ta);
        popular.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        ta2 =  new HorizontalSwipeAdapter(getContext(), HorizontalSwipeAdapter.LAYOUT_MEALTYPE, this);
        categories.setAdapter(ta2);
        categories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        GridAdapter ga = new GridAdapter(this);
        grid.setAdapter(ga);
        grid.setLayoutManager(new GridLayoutManager(getContext(), 3));
    }

    public void newRequest(){
        viewModel.newRequest();
    }

    public void goToRecipePage(Recipes.Recipe recipe){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RECIPE_KEY, recipe);
        NavHostFragment.findNavController(this).navigate(R.id.global_to_recipeFragment_vertical, bundle);
    }

    public void goToSearchScreen(String query, ArrayList<Filter> filters){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.QUERY_KEY, query);
        bundle.putSerializable(Constants.FILTER_KEY, filters);
        NavHostFragment.findNavController(this).navigate(R.id.global_to_searchFragment, bundle);
    }
}