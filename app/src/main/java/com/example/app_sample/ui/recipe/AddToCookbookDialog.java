package com.example.app_sample.ui.recipe;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Cookbook;
import com.example.app_sample.data.local.models.Recipes;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AddToCookbookDialog extends BottomSheetDialogFragment {

    private LinearLayout emptyCard;
    private ImageView add;
    private MaterialButton create;
    private RecyclerView recyclerView;
    private RecipeViewModel viewModel;
    private CookbooksAdapter adapter;
    protected Recipes.Recipe recipe;

    public AddToCookbookDialog(Recipes.Recipe recipe) {
        this.recipe = recipe;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_add_to_cookbook, container, false);

        viewModel = ViewModelProviders.of(requireParentFragment()).get(RecipeViewModel.class);
        emptyCard = v.findViewById(R.id.empty_card);
        add = v.findViewById(R.id.add);
        create = v.findViewById(R.id.create);
        recyclerView = v.findViewById(R.id.recyclerview);

        adapter = new CookbooksAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getCookbooks().observe(getViewLifecycleOwner(), new Observer<List<Cookbook>>() {
            @Override
            public void onChanged(List<Cookbook> cookbooks) {
                adapter.setCookbooks(cookbooks);
                emptyCard.setVisibility(cookbooks.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });

        create.setOnClickListener(v1 -> NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_recipeFragment_to_newCookbookFragment));
        add.setOnClickListener(view -> NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_recipeFragment_to_newCookbookFragment));

        return v;
    }

    public LiveData<List<String>> getCookbookImages(String id) {
        return viewModel.getCookbookImages(id);
    }

    public Task<Void> addToCookbook(String id, Recipes.Recipe recipe){
        return viewModel.addToCookbook(id, recipe);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.getLayoutParams().height = 750;
        }
    }
}
