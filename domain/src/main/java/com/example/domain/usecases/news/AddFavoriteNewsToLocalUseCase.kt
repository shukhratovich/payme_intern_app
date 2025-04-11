package com.example.domain.usecases.news

import com.example.domain.model.ui.NewsUIData
import kotlinx.coroutines.flow.Flow

interface AddFavoriteNewsToLocalUseCase {
    operator fun invoke(item: NewsUIData,isFavorite: Boolean) : Flow<Unit>
}