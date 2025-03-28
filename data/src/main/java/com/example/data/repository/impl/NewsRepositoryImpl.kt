package com.example.data.repository.impl

import com.example.data.remote.api.NewsApi
import com.example.data.remote.response.toUIData
import com.example.domain.entities.NewsUIData
import com.example.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
) : NewsRepository {

    override fun getNewsBySearch(
        q: String,
        from: String
    ): Flow<Result<NewsUIData>> = flow {

        val result = newsApi.getNewsBySearch(q = q, from = from)
        if (result.isSuccessful && result.body() != null) {
            emit(Result.success(result.body()!!.toUIData()))
        } else {
            emit(Result.failure(Throwable(result.message().toString())))
        }


    }.flowOn(Dispatchers.IO).catch { error ->
        emit(Result.failure(error))
    }

}
