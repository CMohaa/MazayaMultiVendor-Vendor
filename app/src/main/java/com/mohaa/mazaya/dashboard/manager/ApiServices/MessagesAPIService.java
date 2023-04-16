package com.mohaa.mazaya.dashboard.manager.ApiServices;


import com.mohaa.mazaya.dashboard.manager.Call.Messages;
import com.mohaa.mazaya.dashboard.manager.Response.MessageResponse;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessagesAPIService {

    //sending message
    @FormUrlEncoded
    @POST("sendmessage")
    Call<MessageResponse> sendMessage(
            @Field("from") int from,
            @Field("to") int to,
            @Field("title") String title,
            @Field("message") String message);


    //getting messages
    @GET("messages/{id}")
    Call<Messages> getMessages(@Path("id") int id);


}