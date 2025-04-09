package com.example.data.remote.api

import com.example.data.BuildConfig
import com.example.data.remote.response.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getCurrentWeatherData(
        @Query("lon") lon: String,
        @Query("lat") lat: String,
        @Query("appid") appid: String = BuildConfig.WEATHER_API_KEY
    ): Response<WeatherResponse>
}