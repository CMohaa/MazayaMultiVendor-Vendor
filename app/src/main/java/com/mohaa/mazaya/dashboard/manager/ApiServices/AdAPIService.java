package com.mohaa.mazaya.dashboard.manager.ApiServices;


import com.mohaa.mazaya.dashboard.manager.Call.Ads;
import com.mohaa.mazaya.dashboard.manager.Response.AdResponse;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AdAPIService {


    //creating new Ads
    @FormUrlEncoded
    @POST("createads")
    Call<AdResponse> createAds(
            @Field("admin_id") int admin_id,
            @Field("admin_name") String admin_name,
            @Field("merchant_id") int merchant_id,
            @Field("thumb_image") String thumb_image,
            @Field("cat_id") int cat_id,
            @Field("type") int type,
            @Field("created_at") long created_at,
            @Field("end_at") long end_at,
            @Field("count") int count


    );
    //updating adsviewCount
    @FormUrlEncoded
    @POST("adsviewcount/{id}")
    Call<AdResponse> updateAdsView(
            @Path("id") int id,
            @Field("count") int count

    );

    //getting ads
    @GET("getads/{end_at}")
    Call<Ads> getAds(@Path("end_at") int end_at);

}