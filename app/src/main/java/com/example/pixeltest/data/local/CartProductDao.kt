package com.example.pixeltest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pixeltest.data.entity.ProductData
import kotlinx.coroutines.flow.Flow

@Dao
interface CartProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProductToCart(product: ProductData)

    @Update
    suspend fun updateProductInCart(product: ProductData)

    @Query("SELECT * FROM product WHERE isInCart = 1")
    fun getCartProducts(): Flow<List<ProductData>>

    @Query("UPDATE product SET quantity = :quantity WHERE id = :productId")
    suspend fun updateProductQuantity(productId: String, quantity: Int)

    @Query("DELETE FROM product WHERE id = :productId")
    suspend fun removeProductFromCart(productId: String)

    @Query("SELECT * FROM product WHERE id= :productId")
    fun getCartById(productId: String):ProductData?
}
