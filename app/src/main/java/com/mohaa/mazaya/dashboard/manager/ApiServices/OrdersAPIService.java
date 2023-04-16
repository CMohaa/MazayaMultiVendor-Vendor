package com.mohaa.mazaya.dashboard.manager.ApiServices;




import com.mohaa.mazaya.dashboard.manager.Call.Orders;
import com.mohaa.mazaya.dashboard.manager.Response.OrderResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrdersAPIService {


    @GET("orders")
    Call<Orders> getOrders();


    /*
     private long order_number;
    private int owner_id;
    private String owner_name;
    private String message;
    private int country;
    private int government;
    private String address;
    private String mobile;
    private int state;
    private long created_at;
    private int count;
    private double total_cost;
     */
    //creating new order
    @FormUrlEncoded
    @POST("createorder")
    Call<OrderResponse> createOrder(
            @Field("order_number") long order_number,
            @Field("owner_id") int owner_id,
            @Field("message") String message,
            @Field("country") int country,
            @Field("government") int government,
            @Field("address") String address,
            @Field("mobile") String mobile,
            @Field("state") int state,
            @Field("created_at") long created_at,
            @Field("count") int count,
            @Field("total_cost") double total_cost

    );



    //getting orderstrader for trader
    @GET("orderstrader/{merchant_id}{page_number}{sort_type}")
    Call<Orders> getOrders(
            @Path("merchant_id") int merchant_id,
            @Path("page_number") int page_number,
            @Path("sort_type") int sort_type
    );


}