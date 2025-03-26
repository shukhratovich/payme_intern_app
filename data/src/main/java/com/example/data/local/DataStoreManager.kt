package com.example.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "timer_prefs")

class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        val TIME_KEY = longPreferencesKey("saved_times")
    }

    suspend fun saveData(time: Long) {
        context.dataStore.edit { preferences ->
            preferences[TIME_KEY] = time
        }
    }

    val timerFlow = context.dataStore.data.map { performance ->
        performance[TIME_KEY] ?: 0L
    }

//    fun getTimeSync(): Long = runBlocking {
//        context.dataStore.data.map { preferences ->
//            preferences[TIME_KEY] ?: 0
//        }.first()
//    }

}