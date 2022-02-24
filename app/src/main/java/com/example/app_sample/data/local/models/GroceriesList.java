package com.example.app_sample.data.local.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "groceries")
public class GroceriesList {

    @PrimaryKey
    private int id;

    private String stringList;

    public GroceriesList(int id) {
        this.id = id;
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
}
