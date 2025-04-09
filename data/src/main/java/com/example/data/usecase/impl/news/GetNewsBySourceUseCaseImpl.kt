package com.example.data.usecase.impl.news

import com.example.data.local.room.dao.NewsDao
import com.example.data.local.room.entity.toUIData
import com.example.data.repository.RemoteNewsRepository
import com.example.domain.model.ui.NewsUIData
import com.example.domain.usecases.news.GetNewsBySourceUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetNewsBySourceUseCaseImpl @Inject constructor(
    private val remoteNewsRepository: RemoteNewsRepository,
    private val newsDao: NewsDao
) :
    GetNewsBySourceUseCase {
    override fun invoke(sourceId: String?): Flow<Result<List<NewsUIData>>> = flow {
        remoteNewsRepository.getNewsBySources(sourceId = sourceId)
            .catch { e ->
                emit(Result.failure(e))
            }.collect { remoteData ->
                if (remoteData.isSuccess) {
                    remoteData.onSuccess { remoteNews ->
                        val remoteNewsEntity =
                            remoteNews.map { newsEntity -> newsEntity.copy(sourceId = sourceId) }
                        newsDao.addAll(remoteNewsEntity)
                        newsDao.getAllNews().collect { localNews ->
                            emit(Result.success(localNews.filter { article -> article.sourceId == sourceId }
                                .map { article -> article.toUIData() }))
                        }
                    }
                }
            }
    }.flowOn(Dispatchers.IO).catch { e ->
        emit(Result.failure(e))
    }
}