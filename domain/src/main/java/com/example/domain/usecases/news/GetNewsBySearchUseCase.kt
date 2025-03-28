package com.example.domain.usecases.news

import com.example.domain.entities.NewsUIData
import kotlinx.coroutines.flow.Flow

interface GetNewsBySearchUseCase {
    operator fun invoke(q: String, from: String): Flow<Result<NewsUIData>>
}
