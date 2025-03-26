package com.example.data.usecase.impl.stopwatch

import com.example.data.local.DataStoreManager
import com.example.domain.usecases.stopwatch.SaveTimeUseCase
import javax.inject.Inject

class SaveTimeUseCaseImpl @Inject constructor(private val dataStoreManager: DataStoreManager) :
    SaveTimeUseCase {

    override suspend fun invoke(time: Long) {
        dataStoreManager.saveData(time)
    }
}