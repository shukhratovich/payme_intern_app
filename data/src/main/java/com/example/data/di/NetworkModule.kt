package com.example.data.di

import com.example.data.remote.weather.api.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val weatherBaseUrl = "https://api.openweathermap.org/data/2.5/"

    @[Provides Singleton]
    fun provideWeatherApi(): WeatherApi = Retrofit.Builder()
        .baseUrl(weatherBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

}