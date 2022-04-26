package com.example.app_sample.ui.recipe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.app_sample.R;
import com.example.app_sample.data.RecipeRepository;
import com.example.app_sample.data.local.models.Filters;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.utils.Constants;
import com.example.app_sample.utils.DownloadService;
import com.example.app_sample.utils.MyViewModelFactory;
import com.example.app_sample.utils.ZoomOutPageTransformer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

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
    private AddToCookbookDialog dialog;

    public RecipeFragment() { }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_recipe, container, false);

        ViewCompat.setTranslationZ(view, 10F);
        viewModel = ViewModelProviders.of(this, new MyViewModelFactory(requireActivity().getApplication(), recipe)).get(RecipeViewModel.class);
        toolbar = view.findViewById(R.id.toolbar);
        ingredientsRV = view.findViewById(R.id.rv_ingredients);
        recipeImage = view.findViewById(R.id.recipe_image);
        recipeName = view.findViewById(R.id.recipe_name);
        mealType = view.findViewById(R.id.meal_type);
        time = view.findViewById(R.id.time);
        servings = view.findViewById(R.id.tv_servings);
        instructionsViewPager = view.findViewById(R.id.vp_instructions);
        shortInstructions = view.findViewById(R.id.short_instructions);
        dialog = new AddToCookbookDialog(recipe);

        recipeName.setText(recipe.getTitle());
        typeString = recipe.getDishTypes().isEmpty() ? "No Type" : toCaps(recipe.getDishTypes().get(0));
        mealType.setText(typeString);
        timeString = recipe.getReadyInMinutes() + " " + getResources().getString(R.string.time);
        time.setText(timeString);
        servingsString = recipe.getServings() + " " + getResources().getString(R.string.servings);
        servings.setText(servingsString);
        if (recipe.getColor() == 0) {
            int x = new Random().nextInt(7);
            recipe.setColor(requireContext().getResources().getColor(Filters.MealType.values()[x].color()));
        }
        mealType.setBackgroundTintList(ColorStateList.valueOf(recipe.getColor()));
        RecipeRepository.loadImage(requireContext(), recipe.getImage(), recipeImage);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(V -> requireActivity().onBackPressed());

        setupMenu();

        ingredientsAdapter = new IngredientsAdapter(recipe.getIngredients(), requireContext());
        layoutManager = new LinearLayoutManager(requireContext());
        ingredientsRV.setAdapter(ingredientsAdapter);
        ingredientsRV.setLayoutManager(layoutManager);

        if (recipe.getInstructions() != null && !recipe.getInstructions().isEmpty()) {

            instructionsAdapter = new InstructionsAdapter(requireContext(), recipe.getInstructions().get(0).getSteps(), recipe.getColor());
            instructionsViewPager.setOffscreenPageLimit(instructionsAdapter.getItemCount());
            instructionsViewPager.setAdapter(instructionsAdapter);
            instructionsViewPager.setPageTransformer(new ZoomOutPageTransformer());
        } else {
            instructionsViewPager.setVisibility(View.GONE);
            shortInstructions.setVisibility(View.VISIBLE);
            if (recipe.getShortInstructions() != null && !recipe.getShortInstructions().isEmpty())
                shortInstructions.setText(recipe.getShortInstructions());
            else
                shortInstructions.setText(getString(R.string.no_instructions));
        }

        return view;
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setupMenu() {
        Drawable saved_filled = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_favorite_filled);
        Drawable saved_outlined = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_favorite);

        Drawable grocery_filled = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_basket_filled);
        Drawable grocery_outlined = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_basket);

        ActionMenuItemView save = toolbar.findViewById(R.id.save);
        ActionMenuItemView groceries = toolbar.findViewById(R.id.groceries);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.share) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
                    String url = getString(R.string.host)+ "/recipe/" + recipe.getId();
                    i.putExtra(Intent.EXTRA_TEXT, url);
                    startActivity(Intent.createChooser(i, "Share recipe URL"));
                    return true;
                } else if (item.getItemId() == R.id.download) {
                    Intent i = new Intent(getContext(), DownloadService.class).putExtra("id", recipe.getId());
                    getContext().startService(i);
                    return true;
                } else if (item.getItemId() == R.id.cookbook) {
                    openCookBooksDialog();
                }
                return false;
            }


        });

        View.OnClickListener isSaved = v -> viewModel.removeRecipe(recipe.getId()).addOnCompleteListener(task -> snack(requireActivity().getString(R.string.recipe_unsaved)));

        View.OnClickListener notSaved = v -> viewModel.saveRecipe(recipe).addOnCompleteListener(task -> snack(getString(R.string.recipe_saved)));

        View.OnClickListener isInGroceries = v -> viewModel.deleteFromGroceries().addOnCompleteListener(task -> snack(requireActivity().getString(R.string.grocery_removed)));

        View.OnClickListener notInGroceries = v -> viewModel.saveToGroceries().addOnCompleteListener(task -> snack(requireActivity().getString(R.string.grocery_added)));

        viewModel.getIsSaved().observe(getViewLifecycleOwner(), saved -> {
            save.setIcon(saved ? saved_filled : saved_outlined);
            save.setOnClickListener(saved ? isSaved : notSaved);
        });

        viewModel.getIsInGroceries().observe(getViewLifecycleOwner(), grocery -> {
            groceries.setIcon(grocery ? grocery_filled : grocery_outlined);
            groceries.setOnClickListener(grocery ? isInGroceries : notInGroceries);
        });

    }

    private void openCookBooksDialog() {
        dialog.show(getChildFragmentManager(), "dialog");
    }

    protected void snack(String msg) {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        Snackbar.make(requireActivity().findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT).setAnchorView(bottomNavigationView).show();
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


}