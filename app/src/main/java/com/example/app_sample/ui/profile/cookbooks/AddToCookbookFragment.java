package com.example.app_sample.ui.profile.cookbooks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.ui.profile.ProfileViewModel;
import com.example.app_sample.utils.Constants;
import com.example.app_sample.utils.MyItemKeyProvider;
import com.example.app_sample.utils.MyItemLookup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

@SuppressWarnings("FieldCanBeLocal")
public class AddToCookbookFragment extends Fragment {

    private ProfileViewModel viewModel;
    private RecyclerView recyclerView;
    private RecipesAdapter adapter;
    private Toolbar toolbar;
    private String bookId;
    private SelectionTracker<Long> selectionTracker;
    private MyItemKeyProvider itemKeyProvider;
    private androidx.appcompat.view.ActionMode actionMode;
    private ActionMode.Callback actionModeCallback;
    private ExtendedFloatingActionButton add;

    public AddToCookbookFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            bookId = getArguments().getString("bookId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_to_cookbook, container, false);

        viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        recyclerView = view.findViewById(R.id.saved_rv);
        toolbar = view.findViewById(R.id.toolbar);
        add = view.findViewById(R.id.add);
        adapter = new RecipesAdapter(getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        setupSelection();

        add.setOnClickListener(v -> {
            for (Long aLong : (Iterable<Long>) selectionTracker.getSelection()) {
                int recipeId = aLong.intValue();
                if (recipeId != -1)
                    viewModel.addToCookbook(bookId, recipeId);
            }
            actionMode.finish();
        });


        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> {
            adapter.setRecipes(recipes);
            itemKeyProvider.setItemList(recipes);
        });

        return view;
    }


    private void setupSelection() {
        itemKeyProvider = new MyItemKeyProvider(ItemKeyProvider.SCOPE_CACHED);

        selectionTracker = new SelectionTracker.Builder<>(
                "selection_saved_recipes",
                recyclerView,
                itemKeyProvider,
                new MyItemLookup(recyclerView),
                StorageStrategy.createLongStorage()).build();


        selectionTracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
            @Override
            public void onItemStateChanged(@NonNull Long key, boolean selected) {
                super.onItemStateChanged(key, selected);
            }

            @Override
            public void onSelectionChanged() {
                boolean hasSelection = selectionTracker.hasSelection();
                add.setVisibility(hasSelection ? View.VISIBLE : View.GONE);
                if(!hasSelection)
                    adapter.notifyDataSetChanged();

            }
        });

        adapter.setSelectionTracker(selectionTracker);

        actionModeCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.add_to_cookbook_menu, menu);
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
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                requireActivity().onBackPressed();
            }
        };

        actionMode = ((AppCompatActivity) requireActivity()).startSupportActionMode(actionModeCallback);
        if( actionMode != null)
            actionMode.setTitle("Select items");
    }

}