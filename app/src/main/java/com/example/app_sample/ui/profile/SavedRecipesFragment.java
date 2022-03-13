package com.example.app_sample.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aitsuki.swipe.SwipeMenuRecyclerView;
import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.utils.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class SavedRecipesFragment extends Fragment {

    private SavedRecipesViewModel viewModel;
    private SwipeMenuRecyclerView recyclerView;
    private SavedRecipesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private CircularProgressIndicator indicator;
    private CardView card;
    private MaterialButton swipe;


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
        card = view.findViewById(R.id.card);
        swipe = view.findViewById(R.id.swipe);
        layoutManager = new LinearLayoutManager(requireContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        indicator.setVisibility(View.VISIBLE);

        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> {
            indicator.setVisibility(View.INVISIBLE);
            if(!recipes.isEmpty()){
                recyclerView.setVisibility(View.VISIBLE);
                card.setVisibility(View.GONE);
                adapter.setRecipes(recipes);
            }
            else{
                recyclerView.setVisibility(View.GONE);
                card.setVisibility(View.VISIBLE);
            }

        });

        swipe.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.homeFragment));
    }


    public void goToRecipePage(Recipes.Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RECIPE_KEY, recipe);
        NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_recipeFragment, bundle);
    }

    public void deleteRecipe(int id){
        viewModel.deleteRecipe(id);
    }

    public void setRecipeColor(int id , int color){
        viewModel.setRecipeColor(id, color);
    }

    public void addToGroceries(Recipes.Recipe recipe){
        viewModel.addToGroceries(recipe);
    }

    public void deleteFromGroceries(int id){
        viewModel.deleteFromGroceries(id);
    }

    public LiveData<Boolean> isInGroceries(int id){
        return viewModel.isInGroceries(id);
    }
}