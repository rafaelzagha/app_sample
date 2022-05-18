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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Recipes;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class AddToCookbookDialog extends BottomSheetDialogFragment {

    private LinearLayout emptyCard;
    private ImageView add;
    private MaterialButton create;
    private RecyclerView recyclerView;
    private RecipeViewModel viewModel;
    private CookbooksAdapter adapter;
    protected final Recipes.Recipe recipe;

    public AddToCookbookDialog(Recipes.Recipe recipe) {
        this.recipe = recipe;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_to_cookbook, container, false);

        viewModel = ViewModelProviders.of(requireParentFragment()).get(RecipeViewModel.class);
        emptyCard = view.findViewById(R.id.empty_card);
        add = view.findViewById(R.id.add);
        create = view.findViewById(R.id.create);
        recyclerView = view.findViewById(R.id.recyclerview);

        adapter = new CookbooksAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getCookbooks().observe(getViewLifecycleOwner(), cookbooks -> {
            adapter.setCookbooks(cookbooks);
            emptyCard.setVisibility(cookbooks.isEmpty() ? View.VISIBLE : View.GONE);
        });

        create.setOnClickListener(v -> NavHostFragment.findNavController(requireParentFragment()).navigate(R.id.global_to_newCookbookFragment));
        add.setOnClickListener(v -> NavHostFragment.findNavController(requireParentFragment()).navigate(R.id.global_to_newCookbookFragment));

        return view;
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
