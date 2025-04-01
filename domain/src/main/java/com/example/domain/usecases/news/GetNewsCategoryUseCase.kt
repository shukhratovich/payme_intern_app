package com.example.domain.usecases.news

import com.example.domain.entities.NewsUIData
import kotlinx.coroutines.flow.Flow

interface GetNewsCategoryUseCase {
    operator fun invoke(category: String): Flow<Result<NewsUIData>>
}