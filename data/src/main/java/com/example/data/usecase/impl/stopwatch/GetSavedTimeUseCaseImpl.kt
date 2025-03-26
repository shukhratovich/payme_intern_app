package com.example.data.usecase.impl.stopwatch

import com.example.data.local.DataStoreManager
import com.example.domain.usecases.stopwatch.GetSavedTimeUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedTimeUseCaseImpl @Inject constructor(private val dataStoreManager: DataStoreManager) :
    GetSavedTimeUseCase {
    override fun invoke(): Flow<Long> = dataStoreManager.timerFlow
}