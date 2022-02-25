package com.example.app_sample.data.local.dao;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StringListBooleanConverter {

    @TypeConverter
    public String toString(ArrayList<Boolean> list){
        if(list == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Boolean>>() {}.getType();
        return gson.toJson(list, type);
    }

    @TypeConverter
    public ArrayList<Boolean> toList(String stringList) {
        if (stringList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Boolean>>() {}.getType();
        ArrayList<Boolean> list = gson.fromJson(stringList, type);
        return list;
    }
}
