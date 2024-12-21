package com.example.pixeltest.data.local

import com.example.pixeltest.data.entity.ProductData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val favoriteProductDao: FavoriteProductDao,
    private val cartDao: CartProductDao
) {

    fun getFavorites(): Flow<List<ProductData>> {
        return favoriteProductDao.getFavoriteProducts()
    }

    suspend fun addToFavorites(product: ProductData) {
        val updatedProduct = product.copy(isFavorite = true)
        favoriteProductDao.insertProduct(updatedProduct)
    }

    suspend fun removeFromFavorites(product: ProductData) {
        favoriteProductDao.updateFavoriteStatus(product.id, false)
    }

    fun getCartItems(): Flow<List<ProductData>> {
        return cartDao.getCartProducts()
    }
    fun getCartById(productId: String): ProductData?{
        return cartDao.getCartById(productId)
    }

    suspend fun addToCart(product: ProductData) {
        cartDao.addProductToCart(product)
    }

    suspend fun removeFromCart(productId: String) {
        cartDao.removeProductFromCart(productId)
    }

    suspend fun updateProductQuantity(productId: String, quantity: Int) {
        cartDao.updateProductQuantity(productId, quantity)
    }
}
