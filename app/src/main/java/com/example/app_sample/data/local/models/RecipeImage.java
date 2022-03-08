package com.example.app_sample.data.local.models;

import com.google.gson.annotations.SerializedName;

public class RecipeImage {

    @SerializedName("url")
    private String url;

    @SerializedName("status")
    private String status;

    public RecipeImage(String url, String status) {
        this.url = url;
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
