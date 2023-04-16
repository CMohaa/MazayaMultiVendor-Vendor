package com.mohaa.mazaya.dashboard.manager.ApiServices;




import com.mohaa.mazaya.dashboard.manager.Call.OrderProducts;
import com.mohaa.mazaya.dashboard.manager.Response.OrderProductResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderProductsAPIService {


    @GET("orderproducts")
    Call<OrderProducts> getOrderdProducts();


    //creating new pendingproduct
    @FormUrlEncoded
    @POST("createorderproduct")
    Call<OrderProductResponse> createPendingProduct(
            @Field("product_id") int product_id,
            @Field("merchant_id") int merchant_id,
            @Field("order_number") long order_number,
            @Field("owner_id") int owner_id,
            @Field("quantity") int quantity,
            @Field("price") double price,
            @Field("total_price") double total_price,
            @Field("barcode") long barcode,
            @Field("created_at") long created_at

    );



    //getting orderproducts for trader
    @GET("orderproductstrader/{merchant_id}{page_number}{sort_type}")
    Call<OrderProducts> getPendingProducts(
            @Path("merchant_id") int merchant_id,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_type
    );


}