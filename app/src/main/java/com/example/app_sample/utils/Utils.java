package com.example.app_sample.utils;

import com.example.app_sample.R;
import com.example.app_sample.data.local.models.Category;

import java.util.ArrayList;

public class Utils {

    public final static String BASE_URL = "https://api.spoonacular.com/";
    public final static String API_KEY = "b88c4d51dfeb4d70b7ff865ee305d725"; //0140582cb9c243598bb4d22d159fda84
    public final static String IMAGE_URL = "https://spoonacular.com/recipeImages/";


    public static ArrayList<Integer> getColors(){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.color.green);
        list.add(R.color.red);
        list.add(R.color.yellow);
        list.add(R.color.flat_blue);

        return list;
    }




}
