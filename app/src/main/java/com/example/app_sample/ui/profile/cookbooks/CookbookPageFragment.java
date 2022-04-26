package com.example.app_sample.ui.profile.cookbooks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Cookbook;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.ui.profile.ProfileViewModel;
import com.example.app_sample.utils.Constants;
import com.example.app_sample.utils.MyItemKeyProvider;
import com.example.app_sample.utils.MyItemLookup;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

public class CookbookPageFragment extends Fragment {

    private String uid, id;
    private Boolean external;
    private Cookbook cookbook;
    private Toolbar toolbar;
    private EditText title;
    private RecyclerView recyclerView;
    private ProfileViewModel viewModel;
    private CookbookRecipesAdapter adapter;
    private LinearLayout card;
    private MaterialButton addFromSaved;
    private SelectionTracker<Long> selectionTracker;
    private MyItemKeyProvider itemKeyProvider;
    private ActionMode actionMode;
    private ActionMode.Callback actionModeCallback;
    private MaterialAlertDialogBuilder aboutDialog;

    public CookbookPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = (String) getArguments().getSerializable("id");
            uid = (String) getArguments().getSerializable("uid");
            external = uid != null && !uid.equals(FirebaseAuth.getInstance().getUid());
        } else requireActivity().onBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStat) {

        View view = inflater.inflate(R.layout.fragment_cookbook_page, container, false);

        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        toolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new CookbookRecipesAdapter(this);
        title = view.findViewById(R.id.title);
        card = view.findViewById(R.id.card);
        addFromSaved = view.findViewById(R.id.swipe);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(null);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!external) {
            itemKeyProvider = new MyItemKeyProvider(ItemKeyProvider.SCOPE_CACHED);

            viewModel.getCookbook(id).observe(getViewLifecycleOwner(), new Observer<Cookbook>() {
                @Override
                public void onChanged(Cookbook cookbook) {
                    if (cookbook != null) {
                        CookbookPageFragment.this.cookbook = cookbook;
                        title.setText(cookbook.getName());
                        adapter.setRecipes(cookbook.getObjects());
                        itemKeyProvider.setItemList(cookbook.getObjects());
                        boolean hasItems = cookbook.getObjects() != null && !cookbook.getObjects().isEmpty();
                        card.setVisibility(hasItems ? View.GONE : View.VISIBLE);
                    } else {
                        requireActivity().onBackPressed();
                    }
                }
            });

            title.setOnFocusChangeListener((v, hasFocus) -> title.setBackgroundTintList(AppCompatResources.getColorStateList(requireContext(), hasFocus ? R.color.black : R.color.white)));

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            title.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    title.clearFocus();
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    if (!title.getText().toString().equals(cookbook.getName()))
                        viewModel.changeCookbookName(id, title.getText().toString());
                    return true;
                }
                return false;
            });


            selectionTracker = new SelectionTracker.Builder<>(
                    "selection",
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
                    if (hasSelection) {
                        if (actionMode == null) {
                            actionMode = ((AppCompatActivity) requireActivity()).startSupportActionMode(actionModeCallback);
                            adapter.notifyDataSetChanged();
                        }
                        actionMode.setTitle(selectionTracker.getSelection().size() + " selected");
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
            };

            addFromSaved.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("bookId", id);
                NavHostFragment.findNavController(CookbookPageFragment.this).navigate(R.id.action_cookbookPageFragment_to_addToCookbookFragment, bundle);
            });
        } else {
            card.setVisibility(View.GONE);
            viewModel.getPublicUsername(uid).observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    aboutDialog = new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("You are viewing a public cookbook")
                            .setMessage("This cookbook belongs to " + s +". You can save it to your own library if you wish to.")
                            .setPositiveButton("Save cookbook", (dialog, which) -> saveCookbook());
                }
            });

            viewModel.getPublicCookbook(uid, id).observe(getViewLifecycleOwner(), new Observer<Cookbook>() {
                @Override
                public void onChanged(Cookbook cookbook) {
                    if(cookbook == null){
                        Toast.makeText(getContext(), "Invalid link", Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed();
                        return;
                    }
                    CookbookPageFragment.this.cookbook = cookbook;
                    title.setText(cookbook.getName());
                    title.setFocusable(false);
                    title.setEnabled(false);
                    adapter.setRecipes(cookbook.getObjects());
                }
            });
        }

        return view;
    }

    private void saveCookbook() {
        viewModel.savePublicCookbook(cookbook);
        Toast.makeText(requireContext(), "Cookbook saved", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(external ? R.menu.cookbook_public_menu : R.menu.cookbook_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            if (adapter.getRecipes() != null && !adapter.getRecipes().isEmpty())
                selectionTracker.select(((CookbookRecipesAdapter.ViewHolder) recyclerView.findViewHolderForLayoutPosition(0)).getItemDetails().getSelectionKey());
            return true;
        } else if (item.getItemId() == R.id.share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing cookbook");
            String url = getString(R.string.host)+ "/cookbook/" + FirebaseAuth.getInstance().getUid() + "/" + id;
            i.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(Intent.createChooser(i, "Share cookbook"));
            return true;
        } else if (item.getItemId() == R.id.delete) {
            viewModel.deleteCookbook(id);
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            requireActivity().onBackPressed();
        } else if (item.getItemId() == R.id.add_from_saved) {
            addFromSaved.performClick();
        } else if(item.getItemId() == R.id.info){
            aboutDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToRecipePage(Recipes.Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RECIPE_KEY, recipe);
        NavHostFragment.findNavController(this).navigate(R.id.action_cookbookPageFragment_to_recipeFragment, bundle);
    }

}