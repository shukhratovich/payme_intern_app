package com.example.domain.usecases.news

import com.example.domain.model.ui.ArticleWithFavouriteUIData
import kotlinx.coroutines.flow.Flow

interface GetAllFavoriteUseCase {
    operator fun invoke(): Flow<List<ArticleWithFavouriteUIData>>
}