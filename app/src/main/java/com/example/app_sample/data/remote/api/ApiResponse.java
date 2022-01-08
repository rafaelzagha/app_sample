package com.example.app_sample.data.remote.api;

import androidx.annotation.Nullable;

import com.example.app_sample.data.local.models.Recipes;
import com.example.app_sample.data.local.models.RecipesResults;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
public class ApiResponse<T> {

    public final int code;
    @Nullable
    public final T body;
    @Nullable
    public final Throwable error;


    public ApiResponse(@Nullable Throwable error) {
        code = 500;
        body = null;
        this.error = error;
    }

    public ApiResponse(Response<T> response) {
        code = response.code();
        if (response.isSuccessful()) {
            body = response.body();
            error = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException ignored) {
                    //Timber.e(ignored, "error while parsing response");
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            error = new IOException(message);
            body = null;
        }
    }

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }


    public int getCode() {
        return code;
    }

    @Nullable
    public T getBody() {
        return body;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    public static ApiResponse<RecipesResults> joinResponses(ApiResponse<RecipesResults> r1, ApiResponse<RecipesResults> r2){
        List<Recipes.Recipe> list = r1.getBody().getRecipes();
        list.addAll(r2.getBody().getRecipes());
        r1.getBody().setRecipes(list);
        return r1;
    }
}