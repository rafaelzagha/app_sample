package com.example.app_sample.data.local.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Recipes implements Serializable {

    @SerializedName("recipes")
    private List<Recipe> recipes;

    public Recipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Entity(tableName = "recipes")
    public static class Recipe implements Serializable {

        @ColumnInfo(name = "vegetarian")
        @SerializedName("vegetarian")
        private Boolean vegetarian;

        @ColumnInfo(name = "vegan")
        @SerializedName("vegan")
        private Boolean vegan;

        @ColumnInfo(name = "glutenFree")
        @SerializedName("glutenFree")
        private Boolean glutenFree;

        @ColumnInfo(name = "dairyFree")
        @SerializedName("dairyFree")
        private Boolean dairyFree;

        @ColumnInfo(name = "veryHealthy")
        @SerializedName("veryHealthy")
        private Boolean veryHealthy;

        @ColumnInfo(name = "cheap")
        @SerializedName("cheap")
        private Boolean cheap;

        @ColumnInfo(name = "ingredients")
        @SerializedName("extendedIngredients")
        private List<Ingredient> ingredients = null;

        @PrimaryKey
        @ColumnInfo(name = "id")
        @SerializedName("id")
        private Integer id;

        @ColumnInfo(name = "title")
        @SerializedName("title")
        private String title;

        @ColumnInfo(name = "readyMinutes")
        @SerializedName("readyInMinutes")
        private Integer readyInMinutes;

        @ColumnInfo(name = "servings")
        @SerializedName("servings")
        private Integer servings;

        @ColumnInfo(name = "sourceUrl")
        @SerializedName("sourceUrl")
        private String sourceUrl;

        @ColumnInfo(name = "image")
        @SerializedName("image")
        private String image;

        @ColumnInfo(name = "summary")
        @SerializedName("summary")
        private String summary;

        @ColumnInfo(name = "cuisines")
        @SerializedName("cuisines")
        private List<String> cuisines = null;

        @ColumnInfo(name = "dishTypes")
        @SerializedName("dishTypes")
        private List<String> dishTypes = null;

        @ColumnInfo(name = "shortInstructions")
        @SerializedName("instructions")
        private String shortInstructions;

        @ColumnInfo(name = "instructions")
        @SerializedName("analyzedInstructions")
        private List<Steps> instructions = null;

        @ColumnInfo(name = "color")
        private transient int color = 0;

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

        public List<String> getCuisines() {
            return cuisines;
        }

        public void setCuisines(List<String> cuisines) {
            this.cuisines = cuisines;
        }

        public List<String> getDishTypes() {
            return dishTypes;
        }

        public void setDishTypes(List<String> dishTypes) {
            this.dishTypes = dishTypes;
        }

        public String getShortInstructions() {
            return shortInstructions;
        }

        public void setShortInstructions(String shortInstructions) {
            this.shortInstructions = shortInstructions;
        }

        public List<Steps> getInstructions() {
            return instructions;
        }

        public void setInstructions(List<Steps> instructions) {
            this.instructions = instructions;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}

