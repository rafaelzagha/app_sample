package com.example.app_sample.data.local.models;

import com.google.gson.annotations.SerializedName;

public class RecipeImage {

    @SerializedName("url")
    private final String url;

    public RecipeImage(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
