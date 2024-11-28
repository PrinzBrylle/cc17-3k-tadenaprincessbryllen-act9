package com.example.flightsearchapp.data

import com.example.flightsearchapp.model.FavoriteFlights
import kotlinx.coroutines.flow.Flow

class OfflineFavoriteRepository(private val favoriteDao: FavoriteDao) : FavoriteRepository {

    override fun getAllFavorites(): Flow<List<FavoriteFlights>> = favoriteDao.getAllFavorites()

    override suspend fun insert(favorite: Favorite) = favoriteDao.insert(favorite)

    override suspend fun delete(favorite: Favorite) = favoriteDao.delete(favorite)
}