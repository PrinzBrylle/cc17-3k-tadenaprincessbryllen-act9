package com.example.flightsearchapp.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.AirportRepository
import com.example.flightsearchapp.data.Favorite
import com.example.flightsearchapp.data.FavoriteRepository
import com.example.flightsearchapp.data.local.UserPreferencesRepository
import com.example.flightsearchapp.model.FavoriteFlights
import com.example.flightsearchapp.ui.theme.CustomKorma
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val airportRepository: AirportRepository,
    private val favoriteRepository: FavoriteRepository,
    private val userPreference: UserPreferencesRepository,
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    var uiState by mutableStateOf(HomeUiState())
        private set

    var favoriteUiState by mutableStateOf(FavoriteListUiState())
        private set

    fun onAirportValueChange(search: String) {
        viewModelScope.launch {
            userPreference.saveSearchText(search)
            airportRepository.getAirportsByIata(search)
                .map { airports ->
                    HomeUiState(airport = airports, searchText = search)
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                    initialValue = HomeUiState()
                ).collect { newState ->
                    uiState = newState
                }
        }
    }

    fun updateSearchText(search: String) {
        uiState.searchText = search
    }

    fun loadFavoriteList() {
        viewModelScope.launch {
            favoriteRepository.getAllFavorites().map {
                FavoriteListUiState(airportsFavorite = it)
            }.collect { newState ->
                favoriteUiState = newState
            }
        }
    }

    fun removeFavoriteList(favorite: Favorite) {
        viewModelScope.launch {
            favoriteRepository.delete(favorite)
        }
    }

    fun loadPreferenceSearchText() = userPreference.searchTextFlow


    fun displayItemTitleList(title: String): String {
        return if (favoriteUiState.airportsFavorite.isNotEmpty()) {
            title
        } else {
            ""
        }
    }
}

data class HomeUiState(
    var airport: List<Airport> = listOf(),
    var searchText: String = "",
)

data class FavoriteListUiState(
    val airportsFavorite: List<FavoriteFlights> = emptyList(),
    val buttonColor: Color = CustomKorma,
)