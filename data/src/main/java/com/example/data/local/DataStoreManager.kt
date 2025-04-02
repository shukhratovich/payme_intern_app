package com.example.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        val TIME_KEY = longPreferencesKey("saved_times")
    }

    suspend fun saveData(time: Long) {
        dataStore.edit { preferences ->
            Log.d("TTT", "save_datastore: ${preferences[TIME_KEY]}")
            preferences[TIME_KEY] = time
        }
    }

    val timerFlow = dataStore.data.map { performance ->
        Log.d("TTT", "datastore: ${performance[TIME_KEY]}")
        performance[TIME_KEY] ?: 0L
    }

}