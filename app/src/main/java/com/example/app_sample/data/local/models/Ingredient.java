package com.example.app_sample.data.local.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ingredient implements Serializable {

    @SerializedName("image")
    private String image;

    @SerializedName("name")
    private String name;

    @SerializedName("amount")
    private Double amount;

    @SerializedName("unit")
    private String unit;

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

}
