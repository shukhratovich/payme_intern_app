package com.example.domain.usecases.weather

import com.example.domain.model.ui.WeatherUIData
import kotlinx.coroutines.flow.Flow

interface GetCurrentWeatherUseCase {
    operator fun invoke(lon: String, lat: String): Flow<Result<WeatherUIData>>
}