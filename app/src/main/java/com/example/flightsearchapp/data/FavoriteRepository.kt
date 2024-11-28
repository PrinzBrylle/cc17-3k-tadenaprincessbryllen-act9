package com.example.flightsearchapp.data

import com.example.flightsearchapp.model.FavoriteFlights
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    fun getAllFavorites(): Flow<List<FavoriteFlights>>

    suspend fun insert(favorite: Favorite)

    suspend fun delete(favorite: Favorite)
}