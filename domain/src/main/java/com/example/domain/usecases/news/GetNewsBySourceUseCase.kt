package com.example.domain.usecases.news

import com.example.domain.model.Network
import com.example.domain.model.ui.NewsUIData
import kotlinx.coroutines.flow.Flow

interface GetNewsBySourceUseCase {
    operator fun invoke(sourceId: String): Flow<Network<List<NewsUIData>>>
}