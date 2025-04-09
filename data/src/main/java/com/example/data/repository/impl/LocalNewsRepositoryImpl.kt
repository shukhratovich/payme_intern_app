package com.example.data.repository.impl

import com.example.data.local.room.dao.NewsDao
import com.example.data.local.room.entity.FavoriteNewsEntity
import com.example.data.local.room.entity.toUIData
import com.example.domain.model.ui.ArticleUIData
import com.example.domain.model.ui.ArticleWithFavouriteUIData
import com.example.data.repository.LocalNewsRepository
import com.example.domain.model.ui.NewsUIData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalNewsRepositoryImpl @Inject constructor(private val newsDao: NewsDao) :
    LocalNewsRepository {
    override fun addFavoriteNews(item: NewsUIData): Flow<Unit> = flow {
        item.url.run {
            emit(newsDao.markAsOpposite(FavoriteNewsEntity(this, true)))
        }
    }.flowOn(Dispatchers.IO)
//
//    override fun getFavoriteNews(): Flow<List<ArticleWithFavouriteUIData>> {
//        return newsDao.getAllFavouriteNews()
//            .map { news -> news.map { it.toUIData() } }
//            .flowOn(Dispatchers.IO)
//    }
}