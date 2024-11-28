package com.example.flightsearchapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearchapp.data.FavoriteRepository
import com.example.flightsearchapp.model.FavoriteFlights
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: FavoriteRepository) : ViewModel() {

    val favorites = repository.getAllFavorites()

    fun onFavoriteClick(favorite: FavoriteFlights) {
        // Handle favorite click logic here (e.g., navigate to a detail screen)
        // For example:
        // navigateToDetailScreen(favorite)
    }
}
