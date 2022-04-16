package com.example.app_sample.ui.profile.cookbooks;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Cookbook;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.ui.profile.ProfileViewModel;
import com.example.app_sample.utils.Constants;
import com.example.app_sample.utils.MyItemKeyProvider;
import com.example.app_sample.utils.MyItemLookup;

public class CookbookPageFragment extends Fragment {

    private String id;
    private Cookbook cookbook;
    private Toolbar toolbar;
    private EditText title;
    private RecyclerView recyclerView;
    private ProfileViewModel viewModel;
    private CookbookRecipesAdapter adapter;
    SelectionTracker<Long> selectionTracker;
    MyItemKeyProvider itemKeyProvider;
    ActionMode actionMode;
    ActionMode.Callback actionModeCallback;

    public CookbookPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cookbook_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        toolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new CookbookRecipesAdapter(this);
        title = view.findViewById(R.id.title);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        itemKeyProvider = new MyItemKeyProvider(ItemKeyProvider.SCOPE_CACHED);

        viewModel.getCookbook(id).observe(getViewLifecycleOwner(), new Observer<Cookbook>() {
            @Override
            public void onChanged(Cookbook cookbook) {
                if (cookbook != null) {
                    CookbookPageFragment.this.cookbook = cookbook;
                    title.setText(cookbook.getName());
                    adapter.setRecipes(cookbook.getObjects());
                    itemKeyProvider.setItemList(cookbook.getObjects());
                } else {
                    requireActivity().onBackPressed();
                }
            }
        });

        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                title.setBackgroundTintList(AppCompatResources.getColorStateList(requireContext(), hasFocus ? R.color.black : R.color.white));

            }
        });

        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    title.clearFocus();
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    if (!title.getText().toString().equals(cookbook.getName()))
                        viewModel.changeCookbookName(id, title.getText().toString());
                    return true;
                }
                return false;
            }
        });


        selectionTracker = new SelectionTracker.Builder<>(
                "selection",
                recyclerView,
                itemKeyProvider,
                new

                        MyItemLookup(recyclerView),
                StorageStrategy.createLongStorage()).

                build();

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
                        actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
                        adapter.notifyDataSetChanged();
                    }
                    actionMode.setTitle(String.valueOf(selectionTracker.getSelection().size() + " selected"));
                } else if (actionMode != null) {
                    actionMode.finish();
                }
            }
        });

        adapter.setSelectionTracker(selectionTracker);

        actionModeCallback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.groceries_actionmode_menu, menu);
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
                        int recipeId = aLong.intValue();
                        if (recipeId != -1)
                            viewModel.removeFromCookbook(id, recipeId);
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
                adapter.notifyDataSetChanged();
                actionMode = null;
            }
        }

        ;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.cookbook_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            if (adapter.getRecipes() != null && !adapter.getRecipes().isEmpty())
                selectionTracker.select(((CookbookRecipesAdapter.ViewHolder) recyclerView.findViewHolderForLayoutPosition(0)).getItemDetails().getSelectionKey());
            return true;
        } else if (item.getItemId() == R.id.share) {
            //todo - share cookbook with link
        } else if (item.getItemId() == R.id.delete) {
            viewModel.deleteCookbook(id);
            return true;
        }else if(item.getItemId() == android.R.id.home){
            Log.d("tag", "pressed");
            requireActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = (String) getArguments().getSerializable("id");
        } else requireActivity().onBackPressed();
    }

    public void goToRecipePage(Recipes.Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RECIPE_KEY, recipe);
        NavHostFragment.findNavController(this).navigate(R.id.action_cookbookPageFragment_to_recipeFragment, bundle);
    }

}