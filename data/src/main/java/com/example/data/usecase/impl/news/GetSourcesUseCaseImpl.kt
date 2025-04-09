package com.example.data.usecase.impl.news

import com.example.data.repository.RemoteNewsRepository
import com.example.domain.usecases.news.GetSourcesUseCase
import javax.inject.Inject

class GetSourcesUseCaseImpl @Inject constructor(
    private val remoteNewsRepository: RemoteNewsRepository
) : GetSourcesUseCase {
    override suspend fun invoke(): Result<List<Pair<String, String>>> {
        return remoteNewsRepository.getSource()
    }
}