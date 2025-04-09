package com.example.data.usecase.impl.news

import com.example.data.local.room.dao.NewsDao
import com.example.data.local.room.entity.toUIData
import com.example.domain.model.ui.NewsUIData
import com.example.data.repository.RemoteNewsRepository
import com.example.domain.usecases.news.GetNewsCategoryUseCase
import com.example.domain.utils.toNewsCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetNewsCategoryUseCaseImpl @Inject constructor(
    private val remoteNewsRepository: RemoteNewsRepository,
    private val newsDao: NewsDao
) :
    GetNewsCategoryUseCase {
    override fun invoke(category: String): Flow<Result<List<NewsUIData>>> = flow {
        remoteNewsRepository.getNewsByCategory(category)
            .catch { e ->
                emit(Result.failure(e))
            }
            .collect { remoteData ->
                if (remoteData.isSuccess) {
                    remoteData.onSuccess { remoteNews ->
                        val remoteEntityList =
                            remoteNews.map { newsEntity -> newsEntity.copy(category = category.toNewsCategory()) }
                        newsDao.addAll(remoteEntityList)

                        newsDao.getAllNews().collect { localNews ->
                            emit(Result.success(localNews.filter { article -> article.category == category.toNewsCategory() }
                                .map { article -> article.toUIData() }))
                        }
                    }
                }
            }
    }.flowOn(Dispatchers.IO).catch { e ->
        emit(Result.failure(e))
    }
}