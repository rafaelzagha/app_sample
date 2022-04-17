package com.example.app_sample.ui.profile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
import com.example.app_sample.ui.groceries.GroceriesAdapter;
import com.example.app_sample.ui.profile.cookbooks.CookbookRecipesAdapter;
import com.example.app_sample.utils.Constants;
import com.example.app_sample.utils.MyItemKeyProvider;
import com.example.app_sample.utils.MyItemLookup;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class SavedRecipesFragment extends Fragment {

    private ProfileViewModel viewModel;
    private SwipeMenuRecyclerView recyclerView;
    private SavedRecipesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private CircularProgressIndicator indicator;
    private LinearLayout card;
    private MaterialButton swipe;
    private String bookId;
    private SelectionTracker<Long> selectionTracker;
    private MyItemKeyProvider itemKeyProvider;
    private androidx.appcompat.view.ActionMode actionMode;
    private ActionMode.Callback actionModeCallback;
    private ExtendedFloatingActionButton add;


    public SavedRecipesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookId = getArguments().getString("bookId");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved_recipes, container, false);

        viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        recyclerView = view.findViewById(R.id.saved_rv);
        indicator = view.findViewById(R.id.indicator);
        adapter = new SavedRecipesAdapter(this);
        card = view.findViewById(R.id.card);
        swipe = view.findViewById(R.id.swipe);
        layoutManager = new LinearLayoutManager(requireContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        indicator.setVisibility(View.VISIBLE);

        if (bookId != null) {
            add = view.findViewById(R.id.add);
            setupSelection();

            add.setOnClickListener(v -> {
                Log.d("tag", "click");
                for (Long aLong : (Iterable<Long>) selectionTracker.getSelection()) {
                    int recipeId = aLong.intValue();
                    Log.d("tag", "recipeId" + recipeId);
                    if (recipeId != -1)
                        viewModel.addToCookbook(bookId, recipeId);
                }
                actionMode.finish();
            });
        }

        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> {
            indicator.setVisibility(View.INVISIBLE);
            adapter.setRecipes(recipes);
            if (itemKeyProvider != null) {
                itemKeyProvider.setItemList(recipes);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selectionTracker.select(((SavedRecipesAdapter.ViewHolder) recyclerView.findViewHolderForLayoutPosition(0)).getItemDetails().getSelectionKey());
                    }
                }, 500);
            }
            card.setVisibility(recipes.isEmpty() ? View.VISIBLE : View.GONE);
        });

        swipe.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.homeFragment));

        return view;
    }


    public void goToRecipePage(Recipes.Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RECIPE_KEY, recipe);
        NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_recipeFragment, bundle);
    }

    public void deleteRecipe(int id) {
        viewModel.deleteRecipe(id);
    }

    public void setRecipeColor(int id, int color) {
        viewModel.setRecipeColor(id, color);
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
                Log.d("tag", "key "+ key);
                Log.d("tag", "selected"+ selected);

                super.onItemStateChanged(key, selected);
            }

            @Override
            public void onSelectionChanged() {
                Log.d("tag", "selection?" + selectionTracker.hasSelection());
                Log.d("tag", "selection?" + selectionTracker.getSelection().size());
                boolean hasSelection = selectionTracker.hasSelection();
                add.setVisibility(hasSelection ? View.VISIBLE : View.GONE);

                if (actionMode == null) {
                    actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
                    adapter.notifyDataSetChanged();
                    actionMode.setTitle("Select items");
                }

            }
        });

        adapter.setSelectionTracker(selectionTracker);

        actionModeCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                requireActivity().onBackPressed();
            }
        };


    }
}