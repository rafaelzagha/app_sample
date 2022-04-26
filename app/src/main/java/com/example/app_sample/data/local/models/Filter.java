package com.example.app_sample.data.local.models;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public interface Filter extends Serializable{
    String tag();
    String group();
    String name();

    static ArrayList<String> stringValues() {
        return null;
    }
}
