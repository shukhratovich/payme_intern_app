package com.example.data.repository

import com.example.domain.model.ui.ArticleUIData
import com.example.domain.model.ui.ArticleWithFavouriteUIData
import com.example.domain.model.ui.NewsUIData
import kotlinx.coroutines.flow.Flow

interface LocalNewsRepository {
    fun addFavoriteNews(item: NewsUIData): Flow<Unit>
//    fun getFavoriteNews(): Flow<List<ArticleWithFavouriteUIData>>
}