package com.example.data.usecase.impl.news

import com.example.domain.entities.NewsUIData
import com.example.domain.repository.NewsRepository
import com.example.domain.usecases.news.GetNewsCategoryUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsCategoryUseCaseImpl @Inject constructor(private val newsRepository: NewsRepository) :
    GetNewsCategoryUseCase {
    override fun invoke(category: String): Flow<Result<NewsUIData>> =
        newsRepository.getNewsByCategory(category)
}