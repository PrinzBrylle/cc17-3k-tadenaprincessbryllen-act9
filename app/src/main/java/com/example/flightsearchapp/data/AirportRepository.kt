package com.example.flightsearchapp.data

import kotlinx.coroutines.flow.Flow

interface AirportRepository {

    fun getAllAirports(): Flow<List<Airport>>

    fun getAirportsByIata(iata: String): Flow<List<Airport>>

    fun getAirportsStreamById(id: Int): Flow<List<Airport>>

    fun getAirportById(id: Int): Flow<Airport?>
}