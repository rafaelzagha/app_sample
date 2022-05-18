package com.example.app_sample.ui.groceries;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.GroceryList;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.utils.Constants;
import com.example.app_sample.utils.MyItemKeyProvider;
import com.example.app_sample.utils.MyItemLookup;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class GroceriesFragment extends Fragment {


    RecyclerView recyclerView;
    GroceriesAdapter adapter;
    GroceriesViewModel viewModel;
    LinearLayout empty;
    MaterialButton discover;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;
    AppBarLayout appbar;
    SelectionTracker<Long> selectionTracker;
    MyItemKeyProvider itemKeyProvider;
    ActionMode actionMode;
    ActionMode.Callback actionModeCallback;

    public GroceriesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_groceries, container, false);

        this.setHasOptionsMenu(true);
        viewModel = ViewModelProviders.of(requireActivity()).get(GroceriesViewModel.class);
        recyclerView = view.findViewById(R.id.recyclerview);
        empty = view.findViewById(R.id.empty);
        toolbar = view.findViewById(R.id.toolbar);
        discover = view.findViewById(R.id.discover);
        appbar = view.findViewById(R.id.appbar);
        adapter = new GroceriesAdapter(this);
        collapsingToolbar = view.findViewById(R.id.collapsingToolbarLayout);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);

        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> {
            if (!recipes.isEmpty()) {
                adapter.setRecipes(recipes);
                empty.setVisibility(View.GONE);
                itemKeyProvider.setItemList(recipes);
            } else
                empty.setVisibility(View.VISIBLE);
        });

        discover.setOnClickListener(v -> NavHostFragment.findNavController(GroceriesFragment.this).navigate(R.id.homeFragment));

        itemKeyProvider = new MyItemKeyProvider(ItemKeyProvider.SCOPE_CACHED);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);


        selectionTracker = new SelectionTracker.Builder<>(
                "selection",
                recyclerView,
                itemKeyProvider,
                new MyItemLookup(recyclerView),
                StorageStrategy.createLongStorage()).build();

        actionModeCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.groceries_actionmode_menu, menu);
                recyclerView.setNestedScrollingEnabled(false);
                appbar.setExpanded(false);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.select_all) {
                    boolean allSelected = selectionTracker.getSelection().size() == adapter.getRecipes().size();
                    selectionTracker.setItemsSelected(itemKeyProvider.getKeyIterable(), !allSelected);
                    return true;
                } else if (item.getItemId() == R.id.delete) {
                    for (Long aLong : (Iterable<Long>) selectionTracker.getSelection()) {
                        int id = aLong.intValue();
                        if (id != -1){
                            adapter.notifyItemRemoved(itemKeyProvider.getPosition(aLong));
                            viewModel.deleteGroceryList(id);
                        }
                    }
                    if (actionMode != null)
                        actionMode.finish();
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                selectionTracker.clearSelection();
                recyclerView.setNestedScrollingEnabled(true);
                appbar.setExpanded(true);
                adapter.reset();
                actionMode = null;
            }
        };

        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
            @Override
            public void onItemStateChanged(@NonNull Long key, boolean selected) {
                super.onItemStateChanged(key, selected);
            }

            @Override
            public void onSelectionChanged() {
                boolean hasSelection = selectionTracker.hasSelection();
                if (hasSelection) {
                    if (actionMode == null) {
                        actionMode = ((AppCompatActivity) requireActivity()).startSupportActionMode(actionModeCallback);
                        adapter.reset();
                    }
                    actionMode.setTitle(selectionTracker.getSelection().size() + " selected");
                } else if (actionMode != null) {
                    actionMode.finish();
                }
            }
        });
        adapter.setSelectionTracker(selectionTracker);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.groceries_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            if (adapter.getRecipes() != null && !adapter.getRecipes().isEmpty())
                selectionTracker.select(Objects.requireNonNull(((GroceriesAdapter.ViewHolder) Objects.requireNonNull(recyclerView.findViewHolderForLayoutPosition(0))).getItemDetails().getSelectionKey()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public LiveData<GroceryList> getGroceryList(int id) {
        return viewModel.getGroceryList(id);
    }

    public void updateGroceriesList(GroceryList gl) {
        viewModel.updateGroceriesList(gl);
    }

    public void goToRecipePage(Recipes.Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RECIPE_KEY, recipe);
        NavHostFragment.findNavController(this).navigate(R.id.global_to_recipeFragment_horizontal, bundle);
    }

    public void updateGroceryServings(int id, int servings) {
        viewModel.updateGroceryServings(id, servings);
    }

    @Override
    public void onDestroyView() {
        if (actionMode != null)
            actionMode.finish();
        super.onDestroyView();
    }
}