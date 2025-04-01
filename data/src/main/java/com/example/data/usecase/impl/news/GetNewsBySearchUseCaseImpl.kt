package com.example.data.usecase.impl.news

import com.example.domain.entities.NewsUIData
import com.example.domain.repository.NewsRepository
import com.example.domain.usecases.news.GetNewsBySearchUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetNewsBySearchUseCaseImpl @Inject constructor(private val newsRepository: NewsRepository) :
    GetNewsBySearchUseCase {
    override fun invoke(
        q: String,
        from: String
    ): Flow<Result<NewsUIData>> = newsRepository.getNewsBySearch(q = q, from = from)
}