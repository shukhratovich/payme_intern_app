package com.example.data.usecase.impl.news

import com.example.data.local.room.entity.toUIData
import com.example.data.remote.response.toRoomData
import com.example.data.repository.LocalNewsRepository
import com.example.data.repository.RemoteNewsRepository
import com.example.domain.model.ui.NewsSourcesUIData
import com.example.domain.usecases.news.GetAllSourceUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllSourceUseCaseImpl @Inject constructor(
    private val remoteNewsRepository: RemoteNewsRepository,
    private val localNewsRepository: LocalNewsRepository
) : GetAllSourceUseCase {
    override fun invoke(): Flow<Result<List<NewsSourcesUIData>>> = flow {
        val response = remoteNewsRepository.getSource()
        response.fold(
            onSuccess = { result ->
                localNewsRepository.addAllSources(result.map { it.toRoomData() })
            },
            onFailure = {
                emit(Result.failure(it))
                return@flow
            }
        )
        localNewsRepository.getAllSources().collect { local ->
            emit(Result.success(local.map { it.toUIData() }))
        }
    }

}