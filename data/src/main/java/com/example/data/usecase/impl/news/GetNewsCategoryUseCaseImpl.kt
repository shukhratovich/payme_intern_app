package com.example.data.usecase.impl.news

import android.util.Log
import com.example.data.local.room.dao.NewsDao
import com.example.data.local.room.entity.NewsEntity
import com.example.data.local.room.entity.toUIData
import com.example.domain.model.ui.NewsUIData
import com.example.data.repository.RemoteNewsRepository
import com.example.domain.model.Network
import com.example.domain.model.ui.CategoryNews
import com.example.domain.usecases.news.GetNewsCategoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetNewsCategoryUseCaseImpl @Inject constructor(
    private val remoteNewsRepository: RemoteNewsRepository,
    private val newsDao: NewsDao
) :
    GetNewsCategoryUseCase {
    override fun invoke(category: CategoryNews): Flow<Network<List<NewsUIData>>> = flow {
        val remoteData = remoteNewsRepository.getNewsByCategory(category.name)
        remoteData.collect { data ->
            when (data) {
                is Network.Error -> emit(Network.Error(data.message))
                is Network.Success<List<NewsEntity>> -> {
                    newsDao.addAll(data.data.map { it.copy(category = category) })
                }
            }
        }
        val localNews = newsDao
            .getNewsByCategory(category)
            .map { localNews ->
                localNews.map { article -> article.toUIData() }
            }.first()

        emit(Network.Success(localNews))
    }.flowOn(Dispatchers.IO)

}
