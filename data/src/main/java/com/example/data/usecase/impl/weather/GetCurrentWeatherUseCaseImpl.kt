package com.example.data.usecase.impl.weather

import com.example.domain.model.ui.WeatherUIData
import com.example.data.repository.WeatherRepository
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
