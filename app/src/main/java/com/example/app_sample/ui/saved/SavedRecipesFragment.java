package com.example.app_sample.ui.saved;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.aitsuki.swipe.SwipeMenuRecyclerView;
import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.utils.Constants;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;

public class SavedRecipesFragment extends Fragment {

    private SavedRecipesViewModel viewModel;
    private SwipeMenuRecyclerView recyclerView;
    private SavedRecipesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private CircularProgressIndicator indicator;

    public SavedRecipesFragment(){ }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity()).get(SavedRecipesViewModel.class);
        recyclerView = view.findViewById(R.id.saved_rv);
        indicator = view.findViewById(R.id.indicator);
        adapter = new SavedRecipesAdapter(this);
        layoutManager = new LinearLayoutManager(requireContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);


        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> {
            indicator.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setRecipes(recipes);
        });
    }

    public void goToRecipePage(Recipes.Recipe recipe){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RECIPE_KEY, recipe);
        NavHostFragment.findNavController(this).navigate(R.id.action_savedFragment_to_recipeFragment, bundle);
    }

    public void deleteRecipe(int id){
        viewModel.deleteRecipe(id);
    }

    public void setRecipeColor(int id , int color){
        viewModel.setRecipeColor(id, color);
    }
}