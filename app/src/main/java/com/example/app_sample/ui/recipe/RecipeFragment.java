package com.example.app_sample.ui.recipe;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
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
import com.example.app_sample.utils.ZoomOutPageTransformer;

import java.util.Locale;
import java.util.Random;

public class RecipeFragment extends Fragment {


    private Recipes.Recipe recipe;
    Toolbar toolbar;
    RecyclerView ingredientsRV;
    ImageView recipeImage;
    TextView recipeName, mealType, time, servings, shortInstructions;
    IngredientsAdapter ingredientsAdapter;
    InstructionsAdapter instructionsAdapter;
    RecyclerView.LayoutManager layoutManager;
    ViewPager2 instructionsViewPager;
    String type;

    public RecipeFragment() {}

    @Override
    public void onResume() {
        super.onResume();
        requireView().requestLayout();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        type = recipe.getDishTypes().isEmpty()?"No Type":recipe.getDishTypes().get(0).substring(0,1).toUpperCase(Locale.ROOT) + recipe.getDishTypes().get(0).substring(1);
        mealType.setText(type);
        time.setText("/ " + recipe.getReadyInMinutes() + " " + getResources().getString(R.string.time));
        servings.setText(recipe.getServings() + " " + getResources().getString(R.string.servings));

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(V -> ((MainActivity)getActivity()).popStack());

        if(recipe.getColor() == 0){
            int x = new Random().nextInt(7);
            recipe.setColor(this.getResources().getColor(Filters.MealType.values()[x].color()));
        }
        mealType.setBackgroundTintList(ColorStateList.valueOf(recipe.getColor()));

        RecipeRepository.loadImage(requireContext(), recipe.getImage(), recipeImage);

        ingredientsAdapter = new IngredientsAdapter(recipe.getIngredients(), requireContext());
        layoutManager = new LinearLayoutManager(requireContext());
        ingredientsRV.setAdapter(ingredientsAdapter);
        ingredientsRV.setLayoutManager(layoutManager);

        if(recipe.getInstructions() != null && !recipe.getInstructions().isEmpty()){

            instructionsAdapter = new InstructionsAdapter(requireContext(), recipe.getInstructions().get(0).getSteps(), recipe.getColor());
            instructionsViewPager.setAdapter(instructionsAdapter);
            instructionsViewPager.setPageTransformer(new ZoomOutPageTransformer());
        }
        else {
            instructionsViewPager.setVisibility(View.GONE);
            shortInstructions.setVisibility(View.VISIBLE);
            if(recipe.getShortInstructions() != null && !recipe.getShortInstructions().isEmpty())
                shortInstructions.setText(recipe.getShortInstructions());
            else
                shortInstructions.setText("No instructions");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = (Recipes.Recipe) getArguments().getSerializable(Constants.RECIPE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }
}