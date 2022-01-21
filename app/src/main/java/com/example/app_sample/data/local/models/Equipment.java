package com.example.app_sample.data.local.models;

import com.google.gson.annotations.SerializedName;

public class Equipment {

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
