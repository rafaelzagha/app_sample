package com.example.app_sample.data.local.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RecipesResults implements Serializable {

    @SerializedName("results")
    private List<Recipes.Recipe> recipes;

    public RecipesResults(List<Recipes.Recipe> recipes) {
        this.recipes = recipes;
    }

    public List<Recipes.Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipes.Recipe> recipes) {
        this.recipes = recipes;
    }

}

