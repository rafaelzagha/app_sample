package com.example.app_sample.data.local.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

@Entity(tableName = "groceries")
public class GroceryList {

    @PrimaryKey
    private int id;

    private ArrayList<Boolean> list;

    @Ignore
    private String push;

    public GroceryList(int id, int size) {
        this.id = id;
        list = new ArrayList<>(Collections.nCopies(size, Boolean.FALSE));
    }

    public GroceryList(int id, ArrayList<Boolean> list) {
        this.id = id;
        this.list = list;
    }

    public GroceryList() {
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
}
