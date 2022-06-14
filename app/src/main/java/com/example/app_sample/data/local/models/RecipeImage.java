package com.example.app_sample.data.local.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecipeImage implements Serializable {

    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }
}
