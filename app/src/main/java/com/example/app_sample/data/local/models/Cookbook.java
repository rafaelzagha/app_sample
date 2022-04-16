package com.example.app_sample.data.local.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cookbook {

    private String name;
    private HashMap<String, String> recipes;
    private String id;
    private List<Recipes.Recipe> objects;

    public Cookbook(String name, String id) {
        this.name = name;
        recipes = new HashMap<>();
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

    public HashMap<String, String> getRecipes() {
        return recipes;
    }

    public void setRecipes(HashMap<String, String> recipes) {
        this.recipes = recipes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Recipes.Recipe> getObjects() {
        if(objects == null)
            objects = new ArrayList<>();
        return objects;
    }

    public void setObjects(List<Recipes.Recipe> objects) {
        this.objects = objects;
    }
}
