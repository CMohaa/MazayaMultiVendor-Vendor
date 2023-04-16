package com.mohaa.mazaya.dashboard.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {

    public static Retrofit retrofitWrite()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public static Retrofit retrofitRead()
    {

        return new Retrofit.Builder()
            .baseUrl(APIUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }
}
