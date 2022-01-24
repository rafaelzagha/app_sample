package com.example.app_sample.data.local.models;

import com.example.app_sample.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Filters {

    public enum Diet implements Filter {
        Vegetarian,
        Vegan,
        Pescatarian;

        @Override
        public String group() {
            return "diet";
        }

        @Override
        public String tag() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public static ArrayList<String> stringValues() {
            ArrayList<String> list = new ArrayList<>();
            for(Filter i : values()){
                list.add(i.name());
            }
            return list;
        }

    }

    public enum Intolerance implements Filter {
        Dairy,
        Gluten,
        Soy,
        Egg,
        Grain,
        Wheat,
        Peanut;

        @Override
        public String group() {
            return "intolerances";
        }


        public String toString() {
            return super.toString();
        }

        @Override
        public String tag() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public static ArrayList<String> stringValues() {
            ArrayList<String> list = new ArrayList<>();
            for(Filter i : values()){
                list.add(i.name());
            }
            return list;
        }

    }

    public enum Cuisine implements Filter {
        Thai(R.drawable.cui_thai),
        Japanese(R.drawable.cui_japanese),
        Italian(R.drawable.cui_italian),
        Indian(R.drawable.cui_indian),
        Israeli(R.drawable.cui_israeli),
        French(R.drawable.cui_french),
        Chinese(R.drawable.cui_chinese),
        Mexican(R.drawable.cui_mexican),
        Spanish(R.drawable.cui_spanish);

        private final int img;

        Cuisine(int img) {
            this.img = img;
        }

        @Override
        public String group() {
            return "cuisine";
        }

        @Override
        public String tag() {
            if (this.name().equals("Israeli"))
                return "middle eastern";
            else
                return this.name().toLowerCase(Locale.ROOT);
        }

        public int img() {
            return img;
        }

        public static ArrayList<String> stringValues() {
            ArrayList<String> list = new ArrayList<>();
            for(Filter i : values()){
                list.add(i.name());
            }
            return list;
        }
    }

    public enum MealType implements Filter {
        Salad(R.drawable.cat_salad, R.color.sand),
        Soup(R.drawable.cat_soup, R.color.coral),
        Breakfast(R.drawable.cat_breakfast, R.color.watermelon),
        Dessert(R.drawable.cat_dessert, R.color.lime),
        MainCourse(R.drawable.cat_main_dish, R.color.sky),
        Snack(R.drawable.cat_snack, R.color.greek),
        Beverage(R.drawable.cat_beverage, R.color.peace);

        private final int img;
        private final int color;

        MealType(int img, int color) {
            this.img = img;
            this.color = color;
        }

        @Override
        public String group() {
            return "type";
        }

        @Override
        public String tag() {
            if (this.name().equals("MainCourse"))
                return "main course";
            return this.name().toLowerCase(Locale.ROOT);
        }

        public int img() {
            return img;
        }

        public int color() {
            return color;
        }

        public static ArrayList<String> stringValues() {
            ArrayList<String> list = new ArrayList<>();
            for(Filter i : values()){
                list.add(i.name());
            }
            return list;
        }


    }

    public enum Sort implements Filter {
        Popularity,
        Price,
        Time,
        Calories;


        @Override
        public String group() {
            return "sort";
        }

        @Override
        public String tag() {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public static ArrayList<String> stringValues() {
            ArrayList<String> list = new ArrayList<>();
            for(Filter i : values()){
                list.add(i.name());
            }
            return list;
        }

    }


    public static String listToString(ArrayList<Filter> list) {
        if(list!= null){
            String str = list.toString();
            str = str.substring(1,str.length()-1);
            str = str.replaceAll("\\s", "");
            return str;
        }
        else return "";

    }

    public static String listToString(Filter[] list) {
        return listToString(new ArrayList<>(Arrays.asList(list)));
    }

}




