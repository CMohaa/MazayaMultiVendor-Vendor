package com.mohaa.mazaya.dashboard.manager.ApiServices;

import com.mohaa.mazaya.dashboard.manager.Call.Users;
import com.mohaa.mazaya.dashboard.manager.Response.UserResponse;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserAPIService {

    @FormUrlEncoded
    @POST("register")
    Call<UserResponse> createUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("gender") String gender,
            @Field("gcmtoken") String gcmtoken

    );

    @FormUrlEncoded
    @POST("login")
    Call<UserResponse> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );


    @GET("users")
    Call<Users> getUsers();

    //updating user
    @FormUrlEncoded
    @POST("update/{id}")
    Call<UserResponse> updateUser(
            @Path("id") int id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("gender") String gender
    );

    //updating user gcmToken
    @FormUrlEncoded
    @POST("storegcmtoken/{id}")
    Call<UserResponse> updateGcmToken(
            @Path("id") int id,
            @Field("gcmtoken") String gcmtoken
    );



}