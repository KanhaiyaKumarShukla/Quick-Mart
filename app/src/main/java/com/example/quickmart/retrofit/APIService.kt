package com.example.quickmart.retrofit

import com.example.quickmart.data.model.productsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("products")
    fun getProducts():Call<List<productsItem>>

    @GET("products/category/{category}")
    fun getProductsByCategory(@Path("category") category: String): Call<List<productsItem>>

    @GET("products/category/{category}")
    fun getProductsByCategoryAtLimit(@Path("category") category: String, @Query("limit") limit: Int): Call<List<productsItem>>
}