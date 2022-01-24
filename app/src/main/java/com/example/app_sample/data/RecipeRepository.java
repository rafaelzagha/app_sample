package com.example.app_sample.data;

import android.app.Application;
import android.os.AsyncTask;

import com.example.app_sample.data.local.RecipeDatabase;
import com.example.app_sample.data.local.dao.RecipeDao;
import com.example.app_sample.data.local.models.Recipes;

public class RecipeRepository {

    private final RecipeDao recipeDao;

    public RecipeRepository(Application application) {
        RecipeDatabase db = RecipeDatabase.getDatabase(application);
        recipeDao = db.recipesDao();
    }

    public void insert(Recipes.Recipe recipe){
        new insertAsyncTask(recipeDao).execute(recipe);
    }

    private static class insertAsyncTask extends AsyncTask<Recipes.Recipe, Void, Void> {

        private RecipeDao mAsyncTaskDao;

        insertAsyncTask(RecipeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Recipes.Recipe... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
