package com.example.domain.model.ui

data class WeatherUIData(
    val temp: Int,
    val cityName: String,
    val icon: String,
    val wind: String,
    val humidity: String,
    val description: String,
    val mainDescription: String,
)
