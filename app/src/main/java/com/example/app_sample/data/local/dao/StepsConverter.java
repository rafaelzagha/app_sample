package com.example.app_sample.data.local.dao;

import androidx.room.TypeConverter;

import com.example.app_sample.data.local.models.Steps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StepsConverter {

    @TypeConverter
    public String toString(List<Steps> list){
        if(list == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<Steps >>() {}.getType();
        return gson.toJson(list, type);
    }

    @TypeConverter
    public List<Steps> toSteps(String steps) {
        if (steps == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Steps>>() {}.getType();
        List<Steps> list = gson.fromJson(steps, type);
        return list;
    }
}
