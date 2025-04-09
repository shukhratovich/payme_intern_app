package com.example.data.repository

import com.example.domain.model.ui.WeatherUIData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getCurrentWeatherData(lon: String,lat: String): Flow<Result<WeatherUIData>>
}