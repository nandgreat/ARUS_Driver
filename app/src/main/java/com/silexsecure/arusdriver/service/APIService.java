package com.silexsecure.arusdriver.service;


import com.silexsecure.arusdriver.util.Config;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nandom
 * DateCreate: 15/06/2019
 */

public class APIService {

    public static Retrofit retrofit = null;
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL_MAIN)
                    .addConverterFactory(GsonConverterFactory.create());

    OkHttpClient client;

    private static OkHttpClient getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    private static OkHttpClient getDriverClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    public static <S> S createService(Class<S> serviceClass) {
        retrofit = builder.client(getClient()).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createDriverService(Class<S> serviceClass) {
        retrofit = builder.client(getDriverClient()).build();
        return retrofit.create(serviceClass);
    }

}


