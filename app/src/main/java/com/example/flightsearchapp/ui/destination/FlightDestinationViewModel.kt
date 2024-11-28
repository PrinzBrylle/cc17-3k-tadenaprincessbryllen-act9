package com.example.flightsearchapp.ui.destination

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.AirportRepository
import com.example.flightsearchapp.data.Favorite
import com.example.flightsearchapp.data.FavoriteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FlightDestinationViewModel(
    savedStateHandle: SavedStateHandle,
    private val airportRepository: AirportRepository,
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    private val airportId: Int = checkNotNull(savedStateHandle[FlightDestinationRoute.AIRPORT_ID])

    val airport: StateFlow<Airport?> = airportRepository.getAirportById(airportId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = Airport(id = 0, name = "", iata = "", passengers = 0)
    )

    val airportList: StateFlow<List<Airport>> =
        airportRepository.getAirportsStreamById(airportId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    suspend fun saveFavoriteRoute(iataCodeDestination: String) {
        favoriteRepository.insert(
            favorite = Favorite(
                departureCode = airport.value!!.iata,
                destinationCode = iataCodeDestination
            )
        )
    }

}

data class FlightDestinationUiState(
    var airport: Airport? = null,
    var airportList: List<Airport> = emptyList(),
)