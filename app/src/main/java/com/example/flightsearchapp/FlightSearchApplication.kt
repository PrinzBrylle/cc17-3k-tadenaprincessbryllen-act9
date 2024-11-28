package com.example.flightsearchapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.flightsearchapp.data.AppContainer
import com.example.flightsearchapp.data.AppDataContainer
import com.example.flightsearchapp.data.local.UserPreferencesRepository

private const val PREFERENCE_SEARCH_TEXT = "preferences_search_text"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCE_SEARCH_TEXT
)

class FlightSearchApplication : Application() {

    lateinit var container: AppContainer
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}