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

        @ColumnInfo(name = "image")
        @SerializedName("image")
        private String image;

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
        private transient Integer color = 0;


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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

        public Integer getColor() {
            return color;
        }

        public void setColor(Integer color) {
            this.color = color;
        }
    }
}

