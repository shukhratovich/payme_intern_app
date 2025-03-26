package com.example.domain.usecases.stopwatch

import kotlinx.coroutines.flow.Flow

interface GetSavedTimeUseCase {
    operator fun invoke(): Flow<Long>
}