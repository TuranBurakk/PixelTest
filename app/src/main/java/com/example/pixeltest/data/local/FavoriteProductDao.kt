package com.example.pixeltest.data.local

import androidx.room.*
import com.example.pixeltest.data.entity.ProductData
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductData)

    @Update
    suspend fun updateProduct(product: ProductData)

    @Query("SELECT * FROM product WHERE isFavorite = 1")
    fun getFavoriteProducts(): Flow<List<ProductData>>

    @Query("SELECT * FROM product")
    fun getAllProducts(): Flow<List<ProductData>>

    @Query("UPDATE product SET isFavorite = :isFavorite WHERE id = :productId")
    suspend fun updateFavoriteStatus(productId: String, isFavorite: Boolean)
}
