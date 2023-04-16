package com.mohaa.mazaya.dashboard.manager.ApiServices;




import com.mohaa.mazaya.dashboard.manager.Response.Pdt_ImageResponse;
import com.mohaa.mazaya.dashboard.models.Pdt_Image;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Pdt_ImagesAPIService {


    //creating pdt_image
    @FormUrlEncoded
    @POST("createpdtimage")
    Call<Pdt_ImageResponse> createPdt_Image(
            @Field("pdt_id") int pdt_id,
            @Field("pd_id") int pd_id,
            @Field("image_url") String image_url
    );


    //getting pdt_image
    @GET("pdt_image/{id}")
    Call<Pdt_Image> getPdt_Image(@Path("id") int id);


    @Multipart
    @POST("UploadImage.php")
    Call<Pdt_ImageResponse> uploadMultiple(
            @Part("description") RequestBody description,
            @Part("name") RequestBody name,
            @Part("size") RequestBody size,
            @Part("count") RequestBody count,
            @Part List<MultipartBody.Part> files);

}