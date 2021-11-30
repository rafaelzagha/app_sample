package com.example.app_sample.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipes {

    @SerializedName("recipes")
    @Expose
    private List<Recipe> recipes;

    public List<Recipe> getRecipes(){
        return recipes;
    }
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public class Recipe {

        @SerializedName("vegetarian")
        @Expose
        private Boolean vegetarian;
        @SerializedName("vegan")
        @Expose
        private Boolean vegan;
        @SerializedName("glutenFree")
        @Expose
        private Boolean glutenFree;
        @SerializedName("dairyFree")
        @Expose
        private Boolean dairyFree;
        @SerializedName("veryHealthy")
        @Expose
        private Boolean veryHealthy;
        @SerializedName("cheap")
        @Expose
        private Boolean cheap;
        @SerializedName("extendedIngredients")
        @Expose
        private List<ExtendedIngredient> extendedIngredients = null;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("readyInMinutes")
        @Expose
        private Integer readyInMinutes;
        @SerializedName("servings")
        @Expose
        private Integer servings;
        @SerializedName("sourceUrl")
        @Expose
        private String sourceUrl;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("summary")
        @Expose
        private String summary;
        @SerializedName("cuisines")
        @Expose
        private List<Object> cuisines = null;
        @SerializedName("dishTypes")
        @Expose
        private List<String> dishTypes = null;

        @SerializedName("analyzedInstructions")
        @Expose
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

        public List<ExtendedIngredient> getIngredients() {
            return extendedIngredients;
        }

        public void setIngredients(List<ExtendedIngredient> extendedIngredients) {
            this.extendedIngredients = extendedIngredients;
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

