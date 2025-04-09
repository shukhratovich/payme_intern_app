package com.example.domain.usecases.news

import com.example.domain.model.ui.ArticleUIData
import com.example.domain.model.ui.CategoryNews
import com.example.domain.model.ui.NewsUIData
import kotlinx.coroutines.flow.Flow

interface GetNewsCategoryUseCase {
    operator fun invoke(category: String): Flow<Result<List<NewsUIData>>>
}