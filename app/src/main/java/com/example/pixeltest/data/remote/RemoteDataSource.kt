package com.example.pixeltest.data.remote

import com.example.pixeltest.utlis.BaseDataSource

class RemoteDataSource(
    private val apiService: ApiService
) : BaseDataSource() {
    suspend fun getProduct() = getResult{
        apiService.getProducts()
    }

    suspend fun getProductById(id:String) = getResult{
        apiService.getProductById(id)
    }
}