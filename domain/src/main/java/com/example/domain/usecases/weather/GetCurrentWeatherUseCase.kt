package com.example.domain.usecases.weather

import com.example.domain.entities.weather.WeatherUIData
import kotlinx.coroutines.flow.Flow

interface GetCurrentWeatherUseCase {
    operator fun invoke(lon: String, lat: String): Flow<Result<WeatherUIData>>
}