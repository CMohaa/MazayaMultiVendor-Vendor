package com.mohaa.mazaya.dashboard.manager.ApiServices;




import com.mohaa.mazaya.dashboard.manager.Call.Comments;
import com.mohaa.mazaya.dashboard.manager.Response.CommentResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentAPIService {



    @GET("comments")
    Call<Comments> getComments();

    //creating new comment
    @FormUrlEncoded
    @POST("createcomment")
    Call<CommentResponse> createComment(
            @Field("usr_id") int usr_id,
            @Field("usr_name") String usr_name,
            @Field("content") String content,
            @Field("pdt_id") int pdt_id,
            @Field("merchant_id") int merchant_id,
            @Field("rate") int rate,
            @Field("created_at") long created_at
    );

    //getting Comments
    @GET("comments/{id}{page_number}")
    Call<Comments> getComments(@Path("id") int id,
                               @Path("page_number") int page_number);

    //getting Comments
    @GET("allcomments/{id}{page_number}")
    Call<Comments> getAllComments(@Path("id") int id,
                                  @Path("page_number") int page_number);


}