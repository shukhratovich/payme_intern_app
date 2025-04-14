package com.example.data.repository.impl

import com.example.data.local.room.dao.NewsDao
import com.example.data.local.room.entity.ArticleWithFavourite
import com.example.data.local.room.entity.FavoriteNewsEntity
import com.example.data.local.room.entity.SourcesEntity
import com.example.data.repository.LocalNewsRepository
import com.example.domain.model.ui.NewsUIData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalNewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao
) : LocalNewsRepository {
    override suspend fun addFavoriteNews(item: NewsUIData, isFavorite: Boolean) {
        newsDao.markAsOpposite(FavoriteNewsEntity(item.url, isFavorite))
    }

    override suspend fun addAllSources(items: List<SourcesEntity>) {
        withContext(Dispatchers.IO) {
            newsDao.addAllSources(items)
        }
    }

    override fun getAllSources(): Flow<List<SourcesEntity>> = newsDao.getAllSource()
    override fun getAllFavorite(): Flow<List<ArticleWithFavourite>> = newsDao.getAllFavourite()
    override fun getAllFavoriteNews(): Flow<List<ArticleWithFavourite>> =
        newsDao.getAllNewsFavorite()

}