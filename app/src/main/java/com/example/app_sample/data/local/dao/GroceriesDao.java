package com.example.app_sample.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.app_sample.data.local.models.GroceryList;

@Dao
public interface GroceriesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(GroceryList groceryList);

    @Query("DELETE FROM groceries where id = :id")
    void deleteGroceriesList(int id);

    @Query("SELECT * FROM groceries WHERE id=:id LIMIT 1")
    GroceryList getGroceriesList(int id);

    @Update()
    void updateGroceriesList(GroceryList groceryList);

    @Query("DELETE FROM groceries")
    void clearTable();

}
