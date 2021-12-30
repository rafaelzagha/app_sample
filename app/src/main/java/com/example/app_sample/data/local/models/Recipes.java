package com.example.app_sample.data.local.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Recipes implements Serializable {

    @SerializedName("recipes")
    private List<Recipe> recipes;

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public class Recipe {

        @SerializedName("vegetarian")
        private Boolean vegetarian;

        @SerializedName("vegan")
        private Boolean vegan;

        @SerializedName("glutenFree")
        private Boolean glutenFree;

        @SerializedName("dairyFree")
        private Boolean dairyFree;

        @SerializedName("veryHealthy")
        private Boolean veryHealthy;

        @SerializedName("cheap")
        private Boolean cheap;

        @SerializedName("extendedIngredients")
        private List<Ingredient> ingredients = null;

        @SerializedName("id")
        private Integer id;

        @SerializedName("title")
        private String title;

        @SerializedName("readyInMinutes")
        private Integer readyInMinutes;

        @SerializedName("servings")
        private Integer servings;

        @SerializedName("sourceUrl")
        private String sourceUrl;

        @SerializedName("image")
        private String image;

        @SerializedName("summary")
        private String summary;

        @SerializedName("cuisines")
        private List<Object> cuisines = null;

        @SerializedName("dishTypes")
        private List<String> dishTypes = null;

        @SerializedName("analyzedInstructions")
        private List<Steps> instructions = null;

        public Boolean getVegetarian() {
            return vegetarian;
        }

        public void setVegetarian(Boolean vegetarian) {
            this.vegetarian = vegetarian;
        }

        public Boolean getVegan() {
            return vegan;
        }

        public void setVegan(Boolean vegan) {
            this.vegan = vegan;
        }

        public Boolean getGlutenFree() {
            return glutenFree;
        }

        public void setGlutenFree(Boolean glutenFree) {
            this.glutenFree = glutenFree;
        }

        public Boolean getDairyFree() {
            return dairyFree;
        }

        public void setDairyFree(Boolean dairyFree) {
            this.dairyFree = dairyFree;
        }

        public Boolean getVeryHealthy() {
            return veryHealthy;
        }

        public void setVeryHealthy(Boolean veryHealthy) {
            this.veryHealthy = veryHealthy;
        }

        public Boolean getCheap() {
            return cheap;
        }

        public void setCheap(Boolean cheap) {
            this.cheap = cheap;
        }

        public List<Ingredient> getIngredients() {
            return ingredients;
        }

        public void setIngredients(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getReadyInMinutes() {
            return readyInMinutes;
        }

        public void setReadyInMinutes(Integer readyInMinutes) {
            this.readyInMinutes = readyInMinutes;
        }

        public Integer getServings() {
            return servings;
        }

        public void setServings(Integer servings) {
            this.servings = servings;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public List<Object> getCuisines() {
            return cuisines;
        }

        public void setCuisines(List<Object> cuisines) {
            this.cuisines = cuisines;
        }

        public List<String> getDishTypes() {
            return dishTypes;
        }

        public void setDishTypes(List<String> dishTypes) {
            this.dishTypes = dishTypes;
        }


        public List<Steps.Step> getInstructions() {
            return instructions.get(0).getSteps();
        }

        public void setInstructions(List<Steps.Step> instructions) {
            this.instructions.get(0).setSteps(instructions);

        }

    }
}

