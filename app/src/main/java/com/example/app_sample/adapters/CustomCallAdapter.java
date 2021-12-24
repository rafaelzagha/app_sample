package com.example.app_sample.adapters;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class CustomCallAdapter<T> implements CallAdapter<T, T> {

    private Type returnType;

    public CustomCallAdapter(Type returnType) {
        this.returnType = returnType;
    }

    @Override
    public Type responseType() {
        return returnType;
    }

    @Override
    public T adapt(Call<T> call) {
        try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Factory extends CallAdapter.Factory {

        @Nullable
        @Override
        public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
            return new CustomCallAdapter<>(returnType);
        }
    }
}
