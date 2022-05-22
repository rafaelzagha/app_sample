package com.example.app_sample.data.remote.api;

import android.util.Log;

import com.example.app_sample.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodService {

    private static final Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static FoodApi foodApi;

    public static FoodApi getFoodApi(){
        return foodApi;
    }

    private static OkHttpClient provideOkHttp(String key){
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    HttpUrl url = request.url().newBuilder().addQueryParameter("apiKey", key).build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                })
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static void changeApiKey(String key){
        foodApi = retrofitBuilder.client(provideOkHttp(key)).build().create(FoodApi.class);
    }



}
