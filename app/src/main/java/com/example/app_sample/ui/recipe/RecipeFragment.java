package com.example.app_sample.ui.recipe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.ui.MainActivity;
import com.example.app_sample.utils.Utils;

public class RecipeFragment extends Fragment {


    private Recipes.Recipe recipe;
    Toolbar toolbar;

    public RecipeFragment() {}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(V -> ((MainActivity)getActivity()).popStack());
    }

    public static RecipeFragment newInstance(Recipes.Recipe recipe) {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putSerializable(Utils.RECIPE_KEY, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = (Recipes.Recipe) getArguments().getSerializable(Utils.RECIPE_KEY);
        }
//        else{
//            Toast.makeText(requireContext(), "Recipe error", Toast.LENGTH_SHORT).show();
//            ((MainActivity)getActivity()).popStack();
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }
}