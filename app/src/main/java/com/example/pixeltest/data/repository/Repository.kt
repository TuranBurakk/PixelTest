package com.example.pixeltest.data.repository

import com.example.pixeltest.data.entity.ProductData
import com.example.pixeltest.data.local.LocalDataSource
import com.example.pixeltest.data.remote.RemoteDataSource
import com.example.pixeltest.utlis.performNetworkOperation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository
@Inject constructor(
    private var apiDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    fun getProducts() = performNetworkOperation {
        apiDataSource.getProduct()
    }

    fun getProductById(id: String) = performNetworkOperation {
        apiDataSource.getProductById(id)
    }

    fun getFavorites() = localDataSource.getFavorites()

    suspend fun addToFavorites(productData: ProductData) =
        localDataSource.addToFavorites(productData)

    suspend fun deleteFromFavorites(productData: ProductData) =
        localDataSource.removeFromFavorites(productData)

    fun getCart(): Flow<List<ProductData>> {
        return localDataSource.getCartItems()
    }

    fun getCartById(productId: String): ProductData? {
        return localDataSource.getCartById(productId)
    }

    suspend fun addToCart(product: ProductData) {
        localDataSource.addToCart(product)
    }

    suspend fun deleteFromCart(productData: ProductData) =
        localDataSource.removeFromCart(productData.id)

    suspend fun updateFromCart(productData: ProductData, quantity: Int) =
        localDataSource.updateProductQuantity(productData.id, quantity)

}