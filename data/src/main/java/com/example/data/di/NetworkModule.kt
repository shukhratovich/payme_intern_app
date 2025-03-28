package com.example.data.di

import com.example.data.remote.api.NewsApi
import com.example.data.remote.api.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val weatherBaseUrl = "https://api.openweathermap.org/data/2.5/"
    private val newsBaseUrl = "https://newsapi.org/v2/"

    @[Provides Singleton]
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .build()

    @[Provides Singleton]
    fun provideWeatherApi(client: OkHttpClient): WeatherApi = Retrofit.Builder()
        .baseUrl(weatherBaseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    @[Provides Singleton]
    fun provideNewsApi(client: OkHttpClient): NewsApi = Retrofit.Builder()
        .baseUrl(newsBaseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsApi::class.java)
}