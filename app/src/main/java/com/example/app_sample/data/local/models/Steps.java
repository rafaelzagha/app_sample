package com.example.app_sample.data.local.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Steps implements Serializable {

    @SerializedName("steps")
    @Expose
    private List<Step> steps;

    public List<Step> getSteps() {
        return steps;
    }

    public static class Step implements Serializable{

        @SerializedName("number")
        private Integer number;

        @SerializedName("step")
        private String step;

        @SerializedName("equipment")
        private List<Equipment> equipment;


        public Integer getNumber() {
            return number;
        }

        public String getStep() {
            return step;
        }

        public List<Equipment> getEquipment() {
            return equipment;
        }

    }
}
