package com.example.paymeinternapp.screens.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.weather.GetCurrentWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) : ViewModel(), WeatherContract.ViewModel {
    override val uiState = MutableStateFlow(WeatherContract.UiState())

    init {
        callWeatherInfo("41.26465", "69.21627")
    }

    private inline fun reduce(block: (WeatherContract.UiState) -> WeatherContract.UiState) {
        val old = uiState.value
        val new = block(old)
        uiState.value = new
    }


    override fun onEventDispatcher(intent: WeatherContract.Intent) {
        when (intent) {
            is WeatherContract.Intent.Refresh -> {
                reduce { it.copy(isLoading = true) }
                callWeatherInfo("41.26465", "69.21627")
            }
        }
    }

    private fun callWeatherInfo(lat: String, lon: String) {
        reduce { it.copy(isLoading = true) }
        val responseWeather = getCurrentWeatherUseCase.invoke(lat = lat, lon = lon)

        responseWeather.onEach { response ->
            delay(1000)
            response.onSuccess { success ->
                reduce {
                    it.copy(
                        cityName = success.cityName,
                        temp = success.temp,
                        icon = success.icon,
                        description = success.description,
                        humidity = success.humidity,
                        wind = success.wind,
                        mainDescription = getWeatherDescription(success.mainDescription),
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
            response.onFailure { error ->
                reduce { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }.launchIn(viewModelScope)
    }

    fun getWeatherDescription(weather: String): WeatherType {
        return when (weather) {
            "Clouds" -> WeatherType.CLOUDY
            "Clear" -> WeatherType.CLEAR
            "Atmosphere" -> WeatherType.ATMOSPHERE
            "Snow" -> WeatherType.SNOWY
            "Rain", "Drizzle" -> WeatherType.RAINY
            "Thunderstorm" -> WeatherType.THUNDERSTORM
            else -> {
                WeatherType.NON
            }
        }
    }
}