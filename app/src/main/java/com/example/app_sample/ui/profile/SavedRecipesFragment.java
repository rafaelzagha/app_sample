package com.example.app_sample.ui.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aitsuki.swipe.SwipeMenuRecyclerView;
import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.utils.Constants;
import com.example.app_sample.utils.MyItemKeyProvider;
import com.example.app_sample.utils.MyItemLookup;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.Objects;

@SuppressWarnings("FieldCanBeLocal")
@SuppressLint("NotifyDataSetChanged")
public class SavedRecipesFragment extends Fragment {

    private ProfileViewModel viewModel;
    private SwipeMenuRecyclerView recyclerView;
    private SavedRecipesAdapter adapter;
    private CircularProgressIndicator indicator;
    private LinearLayout card;
    private MaterialButton swipe;


    public SavedRecipesFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved_recipes, container, false);

        viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        recyclerView = view.findViewById(R.id.saved_rv);
        indicator = view.findViewById(R.id.indicator);
        adapter = new SavedRecipesAdapter(this);
        card = view.findViewById(R.id.card);
        swipe = view.findViewById(R.id.swipe);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        indicator.setVisibility(View.VISIBLE);

        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> {
            indicator.setVisibility(View.INVISIBLE);
            adapter.setRecipes(recipes);
            card.setVisibility(recipes.isEmpty() ? View.VISIBLE : View.GONE);
        });

        swipe.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.homeFragment));

        return view;
    }


    public void goToRecipePage(Recipes.Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RECIPE_KEY, recipe);
        NavHostFragment.findNavController(this).navigate(R.id.global_to_recipeFragment_horizontal, bundle);
    }

    public void deleteRecipe(int id) {
        viewModel.deleteRecipe(id);
    }

    public void addToGroceries(Recipes.Recipe recipe) {
        viewModel.addToGroceries(recipe);
    }

    public void deleteFromGroceries(int id) {
        viewModel.deleteFromGroceries(id);
    }

    public LiveData<Boolean> isInGroceries(int id) {
        return viewModel.isInGroceries(id);
    }


}