package com.example.app_sample.data.local.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Equipment implements Serializable {

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
