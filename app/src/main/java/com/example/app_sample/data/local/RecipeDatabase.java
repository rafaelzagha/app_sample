package com.example.app_sample.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.app_sample.data.local.dao.GroceriesDao;
import com.example.app_sample.data.local.dao.IngredientsConverter;
import com.example.app_sample.data.local.dao.RecipeDao;
import com.example.app_sample.data.local.dao.StepsConverter;
import com.example.app_sample.data.local.dao.StringListConverter;
import com.example.app_sample.data.local.models.GroceriesList;
import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.ui.groceries.GroceriesFragment;

@Database(entities = {Recipes.Recipe.class, GroceriesList.class}, version = 3, exportSchema = false)
@TypeConverters({StringListConverter.class, StepsConverter.class, IngredientsConverter.class})
public abstract class RecipeDatabase extends RoomDatabase {

    public abstract RecipeDao recipesDao();
    public abstract GroceriesDao groceriesDao();
    private static RecipeDatabase instance;

    public static RecipeDatabase getDatabase(final Context context){
        if(instance == null){
            synchronized (RecipeDatabase.class){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            RecipeDatabase.class, "database")

                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
