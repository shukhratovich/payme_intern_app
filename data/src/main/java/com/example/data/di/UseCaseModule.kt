package com.example.data.di

import com.example.data.usecase.impl.news.GetNewsBySearchUseCaseImpl
import com.example.data.usecase.impl.news.GetNewsCategoryUseCaseImpl
import com.example.data.usecase.impl.stopwatch.GetSavedTimeUseCaseImpl
import com.example.data.usecase.impl.stopwatch.SaveTimeUseCaseImpl
import com.example.data.usecase.impl.weather.GetCurrentWeatherUseCaseImpl
import com.example.domain.usecases.news.GetNewsBySearchUseCase
import com.example.domain.usecases.news.GetNewsCategoryUseCase
import com.example.domain.usecases.stopwatch.GetSavedTimeUseCase
import com.example.domain.usecases.stopwatch.SaveTimeUseCase
import com.example.domain.usecases.weather.GetCurrentWeatherUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {
    @[Binds ViewModelScoped]
    fun bindSaveTimeUC(impl: SaveTimeUseCaseImpl): SaveTimeUseCase

    @[Binds ViewModelScoped]
    fun bindGetSavedTimeUC(impl: GetSavedTimeUseCaseImpl): GetSavedTimeUseCase

    @[Binds ViewModelScoped]
    fun bindGetCurrentWeatherUC(impl: GetCurrentWeatherUseCaseImpl): GetCurrentWeatherUseCase

    @[Binds ViewModelScoped]
    fun bindGetNewsBySearchUC(impl: GetNewsBySearchUseCaseImpl): GetNewsBySearchUseCase

    @[Binds ViewModelScoped]
    fun bindGetNewsByCategoryUC(impl: GetNewsCategoryUseCaseImpl): GetNewsCategoryUseCase
}