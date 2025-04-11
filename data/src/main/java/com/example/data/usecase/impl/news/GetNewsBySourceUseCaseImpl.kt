package com.example.data.usecase.impl.news

import com.example.data.local.room.dao.NewsDao
import com.example.data.local.room.entity.NewsEntity
import com.example.data.local.room.entity.toUIData
import com.example.data.repository.RemoteNewsRepository
import com.example.domain.model.Network
import com.example.domain.model.ui.NewsUIData
import com.example.domain.usecases.news.GetNewsBySourceUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetNewsBySourceUseCaseImpl @Inject constructor(
    private val remoteNewsRepository: RemoteNewsRepository,
    private val newsDao: NewsDao
) :
    GetNewsBySourceUseCase {
    override fun invoke(sourceId: String): Flow<Network<List<NewsUIData>>> = flow {
        remoteNewsRepository.getNewsBySources(sourceId = sourceId)
            .collect { remoteData ->
                when (remoteData) {
                    is Network.Error -> emit(Network.Error(remoteData.message))
                    is Network.Success<List<NewsEntity>> -> {
                        newsDao.addAll(remoteData.data.map { it.copy(sourceId = sourceId) })
                    }
                }
            }
        val localNews = newsDao
            .getAllNewsBySource(sourceId)
            .map { localNews ->
                localNews.map { article -> article.toUIData() }
            }.first()

        emit(Network.Success(localNews))
    }.flowOn(Dispatchers.IO)
}