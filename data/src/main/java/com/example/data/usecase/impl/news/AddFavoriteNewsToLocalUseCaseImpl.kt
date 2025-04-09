package com.example.data.usecase.impl.news

import com.example.data.repository.LocalNewsRepository
import com.example.domain.model.ui.NewsUIData
import com.example.domain.usecases.news.AddFavoriteNewsToLocalUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class AddFavoriteNewsToLocalUseCaseImpl @Inject constructor(
    private val addFavoriteNewsRepository: LocalNewsRepository
) : AddFavoriteNewsToLocalUseCase {
    override fun invoke(item: NewsUIData): Flow<Unit> =
        addFavoriteNewsRepository.addFavoriteNews(item)
}