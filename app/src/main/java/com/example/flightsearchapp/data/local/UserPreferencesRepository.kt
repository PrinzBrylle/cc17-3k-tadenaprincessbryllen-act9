package com.example.flightsearchapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private val searchKey = stringPreferencesKey("search_text")

    suspend fun saveSearchText(text: String) {
        dataStore.edit { preferences ->
            preferences[searchKey] = text
        }
    }

    val searchTextFlow: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[searchKey] ?: ""
        }
}