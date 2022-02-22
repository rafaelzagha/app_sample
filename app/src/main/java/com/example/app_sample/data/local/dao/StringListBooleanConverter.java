package com.example.app_sample.data.local.dao;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StringListBooleanConverter {

    @TypeConverter
    public String toString(List<Boolean> list){
        if(list == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<Boolean>>() {}.getType();
        return gson.toJson(list, type);
    }

    @TypeConverter
    public List<Boolean> toStringList(String stringList) {
        if (stringList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Boolean>>() {}.getType();
        List<Boolean> list = gson.fromJson(stringList, type);
        return list;
    }
}
