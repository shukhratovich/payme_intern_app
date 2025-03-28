package com.example.paymeinternapp.screens.weather

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.StateFlow

interface WeatherContract {
    interface Intent {
        data object Refresh : Intent
    }

    data class UiState(
        val isLoading: Boolean = false,
        val backgroundColor: Color = Color(0xffffffff),
        val temp: Int = 0,
        val cityName: String = "",
        val icon: String = "",
        val wind: String = "",
        val humidity: String = "",
        val description: String = "",
        val errorMessage: String? = null
    )

    interface ViewModel {
        val uiState: StateFlow<UiState>
        fun onEventDispatcher(intent: Intent)
    }
}
