package com.example.pixeltest.data.remote

import com.example.pixeltest.data.entity.ProductData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("products")
    suspend fun getProducts():Response<List<ProductData>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<ProductData>
}