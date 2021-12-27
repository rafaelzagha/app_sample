package com.example.app_sample.data.remote.api;

import com.example.app_sample.utils.LiveDataCallAdapter;
import com.example.app_sample.utils.LiveDataCallAdapterFactory;
import com.example.app_sample.utils.Utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FoodService {

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
            .baseUrl(Utils.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(new LiveDataCallAdapterFactory())
            .client(provideOkHttp());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static FoodApi foodApi = retrofit.create(FoodApi.class);

    public static FoodApi getFoodApi(){
        return foodApi;
    }


    private static OkHttpClient provideOkHttp(){
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url().newBuilder().addQueryParameter("apiKey", Utils.API_KEY).build();
                        request = request.newBuilder().url(url).build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }


}
