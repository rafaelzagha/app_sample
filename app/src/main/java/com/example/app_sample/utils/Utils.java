package com.example.app_sample.utils;

import android.util.Log;

import com.example.app_sample.R;
import com.example.app_sample.api.FoodApi;
import com.example.app_sample.api.FoodService;
import com.example.app_sample.models.Category;
import com.example.app_sample.models.Recipes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {

    public final static String BASE_URL = "https://api.spoonacular.com/";
    public final static String API_KEY = "0140582cb9c243598bb4d22d159fda84";
    public final static String IMAGE_URL = "https://spoonacular.com/recipeImages/";

    public static void getRandomRecipes(int i) {
        FoodApi foodapi = FoodService.getFoodApi();
        Call<Recipes> recipesCall = foodapi.getPopularRecipes(i, null);
        recipesCall.enqueue(new Callback<Recipes>() {

            @Override
            public void onResponse(Call<Recipes> call, Response<Recipes> response) {
                if(response.code() == 200){
                    for (Recipes.Recipe r : response.body().getRecipes()) {
                        Log.d("tag", r.getTitle());

                    }
                }
            }



            @Override
            public void onFailure(Call<Recipes> call, Throwable t) {

            }
        });

    }

    public static ArrayList<Category> getCuisines(){
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(R.drawable.cui_thai, "Thai"));
        categories.add(new Category(R.drawable.cui_japanese, "Japanese"));
        categories.add(new Category(R.drawable.cui_italian, "Italian"));
        categories.add(new Category(R.drawable.cui_indian, "Indian"));
        categories.add(new Category(R.drawable.cui_turkish, "Turkish"));
        categories.add(new Category(R.drawable.cui_french, "French"));

        return categories;
    }

    public static ArrayList<Category> getCategories(){
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(R.drawable.cat_salad, "Salad"));
        categories.add(new Category(R.drawable.cat_soup, "Soup"));
        categories.add(new Category(R.drawable.cat_breakfast, "Breakfast"));
        categories.add(new Category(R.drawable.cat_dessert, "Dessert"));
        categories.add(new Category(R.drawable.cat_main_dish, "MainDish"));
        categories.add(new Category(R.drawable.cat_snack, "Snack"));
        categories.add(new Category(R.drawable.cat_beverage, "Beverage"));

        return categories;
    }

    public static ArrayList<String> getDishTypes(){
        ArrayList<String> list = new ArrayList<>();
        list.add("Salad");
        list.add("Soup");
        list.add("Breakfast");
        list.add("Dessert");
        list.add("Main Course");
        list.add("Snack");
        list.add("Beverage");
        list.add("Sauce");
        list.add("Appetizer");

        return list;
    }

    public static ArrayList<String> getDiets(){
        ArrayList<String> list = new ArrayList<>();
        list.add("Gluten Free");
        list.add("Vegetarian");
        list.add("Vegan");
        list.add("Pescatarian");

        return list;
    }

    public static ArrayList<String> getIntolerances(){
        ArrayList<String> list = new ArrayList<>();
        list.add("Dairy");
        list.add("Gluten");
        list.add("Soy");
        list.add("Egg");
        list.add("Grain");
        list.add("Wheat");

        return list;
    }




}
