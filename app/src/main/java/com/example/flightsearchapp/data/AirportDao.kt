package com.example.flightsearchapp.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {

    @Query("SELECT * FROM airport ORDER BY name")
    fun getAllAirports(): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE iata_code or name like '%'|| :iataCode || '%' ORDER BY  passengers DESC")
    fun getAirportsByIata(iataCode: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE id != :id")
    fun getAirportsStreamById(id: Int): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE id = :id")
    fun getAirportById(id: Int): Flow<Airport>
}