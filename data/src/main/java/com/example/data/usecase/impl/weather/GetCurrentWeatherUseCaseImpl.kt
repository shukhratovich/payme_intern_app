package com.example.data.usecase.impl.weather

import com.example.domain.entities.WeatherUIData
import com.example.domain.repository.WeatherRepository
import com.example.domain.usecases.weather.GetCurrentWeatherUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentWeatherUseCaseImpl @Inject constructor(
    private val weatherRepository: WeatherRepository
) : GetCurrentWeatherUseCase {

    override fun invoke(
        lon: String,
        lat: String
    ): Flow<Result<WeatherUIData>> = weatherRepository.getCurrentWeatherData(lon = lon, lat = lat)

}
