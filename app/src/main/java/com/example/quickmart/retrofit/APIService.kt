package com.example.quickmart.retrofit

import com.example.quickmart.data.model.productsItem
import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    @GET("products")
    fun getProducts():Call<List<productsItem>>
}