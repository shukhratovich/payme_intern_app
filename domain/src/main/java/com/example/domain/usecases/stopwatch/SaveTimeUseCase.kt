package com.example.domain.usecases.stopwatch

interface SaveTimeUseCase {
    suspend operator fun invoke(time: Long)
}