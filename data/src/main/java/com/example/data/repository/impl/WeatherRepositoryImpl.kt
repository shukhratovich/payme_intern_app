package com.example.data.repository.impl

import android.util.Log
import com.example.data.remote.weather.api.WeatherApi
import com.example.data.remote.weather.response.toData
import com.example.domain.entities.weather.WeatherUIData
import com.example.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {
    override fun getCurrentWeatherData(lon: String, lat: String): Flow<Result<WeatherUIData>> = flow {
        val result = weatherApi.getCurrentWeatherData(lon = lon, lat = lat)

        if (result.isSuccessful) {
            Log.d("TTT", "getCurrentWeatherData: ${result.body()}")
            emit(Result.success(result.body()!!.toData()))
        }
    }.flowOn(Dispatchers.IO).catch { error -> emit(Result.failure(error))
        Log.d("TTT", "getCurrentWeatherData: ${error.message}")
    }
}
