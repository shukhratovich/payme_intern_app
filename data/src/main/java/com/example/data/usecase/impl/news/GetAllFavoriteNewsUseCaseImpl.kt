package com.example.data.usecase.impl.news

import com.example.data.local.room.entity.toUIData
import com.example.data.repository.LocalNewsRepository
import com.example.domain.model.ui.ArticleWithFavouriteUIData
import com.example.domain.usecases.news.GetAllFavoriteNewsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllFavoriteNewsUseCaseImpl @Inject constructor(
    private val localNewsRepository: LocalNewsRepository
) : GetAllFavoriteNewsUseCase {
    override fun invoke(): Flow<List<ArticleWithFavouriteUIData>> = flow{
        localNewsRepository.getAllFavorite().collect { local ->
            emit(local.map { it.toUIData() })
        }
    }
}