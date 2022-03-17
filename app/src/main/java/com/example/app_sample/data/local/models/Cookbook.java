package com.example.app_sample.data.local.models;

import java.util.ArrayList;
import java.util.List;

public class Cookbook {

    private String name;
    private List<Integer> recipes;
    private String id;

    public Cookbook(String name, String id) {
        this.name = name;
        recipes = new ArrayList<>();
        this.id = id;
    }

    public Cookbook() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Integer> recipes) {
        this.recipes = recipes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
