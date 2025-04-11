package com.example.data.usecase.impl.news

import com.example.data.repository.LocalNewsRepository
import com.example.domain.model.ui.NewsUIData
import com.example.domain.usecases.news.AddFavoriteNewsToLocalUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AddFavoriteNewsToLocalUseCaseImpl @Inject constructor(
    private val addFavoriteNewsRepository: LocalNewsRepository
) : AddFavoriteNewsToLocalUseCase {
    override fun invoke(item: NewsUIData, isFavorite: Boolean): Flow<Unit> = flow {
        withContext(Dispatchers.IO) {
            addFavoriteNewsRepository.addFavoriteNews(item = item, isFavorite = isFavorite)
        }
    }
}