package com.example.data.repository

import com.example.data.local.room.entity.ArticleWithFavourite
import com.example.data.local.room.entity.SourcesEntity
import com.example.domain.model.ui.NewsUIData
import kotlinx.coroutines.flow.Flow

interface LocalNewsRepository {
    suspend fun addFavoriteNews(item: NewsUIData,isFavorite: Boolean)
    suspend fun addAllSources(items: List<SourcesEntity>)
    fun getAllSources(): Flow<List<SourcesEntity>>
    fun getAllFavorite(): Flow<List<ArticleWithFavourite>>
//    fun getFavoriteNews(): Flow<List<ArticleWithFavouriteUIData>>
}