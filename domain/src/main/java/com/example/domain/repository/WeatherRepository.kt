package com.example.domain.repository

import com.example.domain.entities.weather.WeatherUIData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getCurrentWeatherData(lon: String,lat: String): Flow<Result<WeatherUIData>>
}