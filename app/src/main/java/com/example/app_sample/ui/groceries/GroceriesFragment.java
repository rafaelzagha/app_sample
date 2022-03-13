package com.example.app_sample.ui.groceries;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.GroceryList;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.utils.Constants;

public class GroceriesFragment extends Fragment {


    RecyclerView recyclerView;
    GroceriesAdapter adapter;
    GroceriesViewModel viewModel;

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
        adapter = new GroceriesAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        viewModel = ViewModelProviders.of(getActivity()).get(GroceriesViewModel.class);



        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> adapter.setRecipes(recipes));
    }

    public LiveData<GroceryList> getGroceryList(int id){
        return viewModel.getGroceryList(id);
    }

    public void deleteGroceryList(int id){
        viewModel.deleteGroceryList(id);
    }

    public void updateGroceriesList(GroceryList gl){
        viewModel.updateGroceriesList(gl);
    }

    public void goToRecipePage(Recipes.Recipe recipe){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RECIPE_KEY, recipe);
        NavHostFragment.findNavController(this).navigate(R.id.action_shoppingFragment_to_recipeFragment, bundle);
    }

    public void updateGroceryServings(int id, int servings){
        viewModel.updateGroceryServings(id, servings);
    }
}