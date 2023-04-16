package com.mohaa.mazaya.dashboard.manager.ApiServices;




import com.mohaa.mazaya.dashboard.manager.Call.Categories;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryAPIService {



    @GET("categories")
    Call<Categories> getCategories();

    //getting categories
    @GET("maincategories/{id}")
    Call<Categories> getMainCategories(@Path("id") int id);


}