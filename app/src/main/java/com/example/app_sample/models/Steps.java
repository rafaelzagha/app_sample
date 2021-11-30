package com.example.app_sample.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Steps {

    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public class Step {

        @SerializedName("number")
        @Expose
        private Integer number;

        @SerializedName("step")
        @Expose
        private String step;

        @SerializedName("ingredients")
        @Expose
        List<Ingredient> ingredients;

        public List<Ingredient> getIngredients() {
            return ingredients;
        }

        public void setIngredients(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }

    }
}
