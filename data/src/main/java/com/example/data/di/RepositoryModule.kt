package com.example.data.di

import com.example.data.connectivity.impl.NetworkConnectivityImpl
import com.example.data.repository.impl.LocalNewsRepositoryImpl
import com.example.data.repository.impl.RemoteNewsRepositoryImpl
import com.example.data.repository.impl.WeatherRepositoryImpl
import com.example.domain.connectivity.NetworkConnectivity
import com.example.data.repository.LocalNewsRepository
import com.example.data.repository.RemoteNewsRepository
import com.example.data.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    fun bindNewsRepository(impl: RemoteNewsRepositoryImpl): RemoteNewsRepository

    @Binds
    fun bindLocalNewsRepository(impl: LocalNewsRepositoryImpl): LocalNewsRepository

    @Binds
    fun bindNetworkConnectivity(impl: NetworkConnectivityImpl): NetworkConnectivity
}
