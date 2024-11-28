package com.example.flightsearchapp.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.Favorite

@DatabaseView(
    """SELECT  f.id AS favorite_id,
        a1.iata_code AS departure_iata,
        a1.name AS departure_name,
        a2.iata_code AS destination_iata,
        a2.name AS destination_name
                FROM favorite f 
                JOIN 
                airport a1 ON f.departure_code = a1.iata_code
                JOIN
                airport a2 ON f.destination_code = a2.iata_code"""
)
data class FavoriteFlights(
    @ColumnInfo(name = "favorite_id") val id: Int,
    @ColumnInfo(name = "departure_iata") val departureIata: String,
    @ColumnInfo(name = "departure_name") val departureName: String,
    @ColumnInfo(name = "destination_iata") val destinationIata: String,
    @ColumnInfo(name = "destination_name") val destinationName: String,
)

enum class FlightType {
    Departure, Destination
}


fun FavoriteFlights.toFavorite(): Favorite = Favorite(
    id = id,
    departureCode = departureIata,
    destinationCode = destinationIata
)

fun FavoriteFlights.toAirportType(type: FlightType): Airport = Airport(
    id = id,
    name = if (type == FlightType.Departure) departureName else destinationName,
    iata = if (type == FlightType.Departure) departureIata else destinationIata,
    passengers = 0
)