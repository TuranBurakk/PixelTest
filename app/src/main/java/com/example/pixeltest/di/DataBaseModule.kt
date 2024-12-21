package com.example.pixeltest.di


import android.content.Context
import androidx.room.Room
import com.example.pixeltest.data.local.CartProductDao
import com.example.pixeltest.data.local.FavoriteProductDao
import com.example.pixeltest.data.local.LocalDataSource
import com.example.pixeltest.data.local.RoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun localDataSource(favoritesDao: FavoriteProductDao , cartDao:CartProductDao): LocalDataSource {
        return LocalDataSource(favoritesDao,cartDao)
    }

    @Provides
    fun provideRoomDB(@ApplicationContext context: Context): RoomDB {
        return Room
            .databaseBuilder(context,RoomDB::class.java,"LocalDB")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFavoritesDao(roomDB: RoomDB): FavoriteProductDao{
        return roomDB.favoritesDao()
    }

    @Provides
    fun provideCartDao(roomDB: RoomDB): CartProductDao{
        return roomDB.cartDao()
    }
}