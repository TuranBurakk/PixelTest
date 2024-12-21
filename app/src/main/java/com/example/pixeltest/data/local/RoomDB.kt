package com.example.pixeltest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pixeltest.data.entity.ProductData

@Database(entities = [ProductData::class], version = 4, exportSchema = false)
abstract class RoomDB : RoomDatabase() {
    abstract fun favoritesDao(): FavoriteProductDao
    abstract fun cartDao(): CartProductDao

}


