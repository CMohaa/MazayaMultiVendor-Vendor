package com.mohaa.mazaya.dashboard.manager.ApiServices;




import com.mohaa.mazaya.dashboard.manager.Call.VariantsProduct;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VariantsProductAPIService {


    //getting VariantsProduct
    @GET("variants_pdt/{id}")
    Call<VariantsProduct> getVariantsProduct(@Path("id") int id);



}