package com.example.domain.usecases.news

import com.example.domain.model.ui.NewsSourcesUIData
import kotlinx.coroutines.flow.Flow

interface GetAllSourceUseCase {
    operator fun invoke(): Flow<Result<List<NewsSourcesUIData>>>
}