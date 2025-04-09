package com.example.data.usecase.impl.news

import com.example.domain.model.ui.NewsUIData
import com.example.data.repository.RemoteNewsRepository
import com.example.domain.usecases.news.GetNewsBySearchUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetNewsBySearchUseCaseImpl @Inject constructor(private val remoteNewsRepository: RemoteNewsRepository) :
    GetNewsBySearchUseCase {
    override fun invoke(
        q: String,
        from: String
    ): Flow<Result<List<NewsUIData>>> = remoteNewsRepository.getNewsBySearch(q = q, from = from)
}