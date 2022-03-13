package com.example.app_sample.data.local.models;

import java.util.ArrayList;
import java.util.Collections;


public class GroceryList {

    private int id;
    private ArrayList<Boolean> list;
    private String push;
    private int servings;

    public GroceryList() {
    }

    public GroceryList(int id, int servings, int size) {
        this.id = id;
        this.servings = servings;
        list = new ArrayList<>(Collections.nCopies(size, Boolean.FALSE));
    }

    public GroceryList(int id, int servings, ArrayList<Boolean> list) {
        this.id = id;
        this.servings = servings;
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Boolean> getList() {
        return list;
    }

    public void setList(ArrayList<Boolean> list) {
        this.list = list;
    }

    public String getPush() {
        return push;
    }

    public void setPush(String push) {
        this.push = push;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }
}
