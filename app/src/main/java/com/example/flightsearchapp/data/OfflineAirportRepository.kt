package com.example.flightsearchapp.data

import kotlinx.coroutines.flow.Flow

class OfflineAirportRepository(private val airportDao: AirportDao) : AirportRepository {

    override fun getAllAirports(): Flow<List<Airport>> = airportDao.getAllAirports()

    override fun getAirportsByIata(iata: String): Flow<List<Airport>> =
        airportDao.getAirportsByIata(iata)

    override fun getAirportsStreamById(id: Int): Flow<List<Airport>> =
        airportDao.getAirportsStreamById(id)

    override fun getAirportById(id: Int): Flow<Airport?> = airportDao.getAirportById(id)
}