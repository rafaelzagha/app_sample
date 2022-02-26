package com.example.app_sample.ui.recipe;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.remote.RecipesRemoteDataSource;
import com.example.app_sample.ui.MainActivity;
import com.example.app_sample.utils.Constants;
import com.example.app_sample.utils.MyViewModelFactory;
import com.example.app_sample.utils.ZoomOutPageTransformer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;
import java.util.Random;

public class RecipeFragment extends Fragment {


    private Recipes.Recipe recipe;
    private Toolbar toolbar;
    private RecyclerView ingredientsRV;
    private ImageView recipeImage;
    private TextView recipeName, mealType, time, servings, shortInstructions;
    private IngredientsAdapter ingredientsAdapter;
    private InstructionsAdapter instructionsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ViewPager2 instructionsViewPager;
    private String typeString, timeString, servingsString;
    private RecipeViewModel viewModel;

    public RecipeFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        requireView().requestLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this, new MyViewModelFactory(getActivity().getApplication(), recipe)).get(RecipeViewModel.class);
        toolbar = view.findViewById(R.id.toolbar);
        ingredientsRV = view.findViewById(R.id.rv_ingredients);
        recipeImage = view.findViewById(R.id.recipe_image);
        recipeName = view.findViewById(R.id.recipe_name);
        mealType = view.findViewById(R.id.meal_type);
        time = view.findViewById(R.id.time);
        servings = view.findViewById(R.id.tv_servings);
        instructionsViewPager = view.findViewById(R.id.vp_instructions);
        shortInstructions = view.findViewById(R.id.short_instructions);


        recipeName.setText(recipe.getTitle());
        typeString = recipe.getDishTypes().isEmpty() ? "No Type" : toCaps(recipe.getDishTypes().get(0));
        mealType.setText(typeString);
        timeString = recipe.getReadyInMinutes() + " " + getResources().getString(R.string.time);
        time.setText(timeString);
        servingsString = recipe.getServings() + " " + getResources().getString(R.string.servings);
        servings.setText(servingsString);
        mealType.setBackgroundTintList(ColorStateList.valueOf(recipe.getColor()));
        RecipeRepository.loadImage(requireContext(), recipe.getImage(), recipeImage);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(V -> ((MainActivity) getActivity()).popStack());

        setupMenu();

        ingredientsAdapter = new IngredientsAdapter(recipe.getIngredients(), requireContext());
        layoutManager = new LinearLayoutManager(requireContext());
        ingredientsRV.setAdapter(ingredientsAdapter);
        ingredientsRV.setLayoutManager(layoutManager);

        if (recipe.getInstructions() != null && !recipe.getInstructions().isEmpty()) {

            instructionsAdapter = new InstructionsAdapter(requireContext(), recipe.getInstructions().get(0).getSteps(), recipe.getColor());
            instructionsViewPager.setAdapter(instructionsAdapter);
            instructionsViewPager.setPageTransformer(new ZoomOutPageTransformer());
        } else {
            instructionsViewPager.setVisibility(View.GONE);
            shortInstructions.setVisibility(View.VISIBLE);
            if (recipe.getShortInstructions() != null && !recipe.getShortInstructions().isEmpty())
                shortInstructions.setText(recipe.getShortInstructions());
            else
                shortInstructions.setText("No instructions");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setupMenu() {
        Drawable saved_filled = requireContext().getDrawable(R.drawable.ic_favorite_filled);
        Drawable saved_outlined = requireContext().getDrawable(R.drawable.ic_favorite);
        saved_filled.setTint(requireContext().getColor(R.color.white));
        saved_outlined.setTint(requireContext().getColor(R.color.white));

        Drawable grocery_filled = requireContext().getDrawable(R.drawable.ic_basket_filled);
        Drawable grocery_outlined = requireContext().getDrawable(R.drawable.ic_basket);
        grocery_filled.setTint(requireContext().getColor(R.color.white));
        grocery_outlined.setTint(requireContext().getColor(R.color.white));

        ActionMenuItemView save = toolbar.findViewById(R.id.favorite);
        ActionMenuItemView groceries = toolbar.findViewById(R.id.groceries);


        View.OnClickListener isSaved = v -> viewModel.removeRecipe(recipe.getId())
                .addOnCompleteListener(task -> Snackbar.make(getActivity().findViewById(android.R.id.content), "Recipe removed from saved", Snackbar.LENGTH_SHORT).show());

        View.OnClickListener notSaved = v ->viewModel.saveRecipe(recipe).addOnCompleteListener(task -> Snackbar.make(getActivity().findViewById(android.R.id.content),"Recipe saved successfully" , Snackbar.LENGTH_SHORT).show());

        View.OnClickListener isInGroceries = v -> viewModel.deleteFromGroceries().addOnCompleteListener(task -> Snackbar.make(getActivity().findViewById(android.R.id.content),"Recipe removed from Groceries" , Snackbar.LENGTH_SHORT).show());

        View.OnClickListener notInGroceries = v -> viewModel.saveToGroceries().addOnCompleteListener(task -> Snackbar.make(getActivity().findViewById(android.R.id.content), "Recipe added to Groceries", Snackbar.LENGTH_SHORT).show());

        viewModel.getIsSaved().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onChanged(Boolean saved) {

                //todo: add groceries option

                if (saved) {
                    save.setIcon(saved_filled);
                    save.setOnClickListener(isSaved);
                    groceries.setVisibility(View.VISIBLE);
                    viewModel.getIsInGroceries().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean grocery) {
                            if(grocery){
                                groceries.setIcon(grocery_filled);
                                groceries.setOnClickListener(isInGroceries);
                            }
                            else{
                                groceries.setIcon(grocery_outlined);
                                groceries.setOnClickListener(notInGroceries);
                            }

                        }
                    });
                    //todo: check if in groceries
                } else {
                    save.setIcon(saved_outlined);
                    save.setOnClickListener(notSaved);
                    groceries.setVisibility(View.INVISIBLE);

                }
            }
        });

    }

    private String toCaps(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = (Recipes.Recipe) getArguments().getSerializable(Constants.RECIPE_KEY);
        } else requireActivity().onBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }
}