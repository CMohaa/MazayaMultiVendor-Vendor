package com.mohaa.mazaya.dashboard.manager.ApiServices;






import com.mohaa.mazaya.dashboard.manager.Call.Addresss;
import com.mohaa.mazaya.dashboard.manager.Response.AddressResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AddressAPIService {



    @GET("address")
    Call<Addresss> getAddress();

    //creating new address
    @FormUrlEncoded
    @POST("createaddress")
    Call<AddressResponse> createAdress(
            @Field("user_id") int user_id,
            @Field("name") String name,
            @Field("country") int country,
            @Field("government") int government,
            @Field("address") String address,
            @Field("mobile") String mobile,
            @Field("state") int state
    );

    //getting address
    @GET("addressg/{id}")
    Call<Addresss> getAddress(@Path("id") int id);



}