package com.example.app_sample.data.local.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;
import com.google.gson.Gson;

import java.util.ArrayList;

@Entity(tableName = "groceries")
public class GroceriesList {

    @PrimaryKey
    private int id;
    private String stringList;

    @Exclude
    @Ignore
    private static Gson gson = new Gson();

    public GroceriesList(int id, int size) {
        this.id = id;
        ArrayList<Boolean> list = new ArrayList<>(size);
        stringList = gson.toJson(list);
    }

    public GroceriesList(int id, String stringList) {
        this.id = id;
        this.stringList = stringList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStringList() {
        return stringList;
    }

    public void setStringList(String stringList) {
        this.stringList = stringList;
    }

    public ArrayList<Boolean> getList(){
        return gson.fromJson(stringList, ArrayList.class);
    }

    public void updateList(ArrayList<Boolean> list){
        stringList = gson.toJson(list);
    }
}
