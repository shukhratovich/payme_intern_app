package com.example.data.repository.impl

import android.util.Log
import com.example.data.remote.api.NewsApi
import com.example.data.remote.response.NewsResponse
import com.example.data.remote.response.toUIData
import com.example.domain.entities.NewsUIData
import com.example.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
) : NewsRepository {
    private inline fun fetchNewsData(crossinline call: suspend () -> Response<NewsResponse>): Flow<Result<NewsUIData>> =
        flow {
            val result = call()
            if (result.isSuccessful && result.body() != null) {
                emit(Result.success(result.body()!!.toUIData()))
            } else {
                emit(Result.failure(Throwable(result.errorBody().toString())))
                Log.d("TTT", "fetchNewsData1: ${result.body()}")
                Log.d("TTT", "fetchNewsData2: ${result.message()}")
                Log.d("TTT", "fetchNewsData3: ${result.errorBody()}")
            }
        }.flowOn(Dispatchers.IO).catch { error ->
            emit(Result.failure(error))
        }

    override fun getNewsBySearch(q: String, from: String): Flow<Result<NewsUIData>> {
        return fetchNewsData { newsApi.getNewsBySearch(q = q, from = from) }
    }

    override fun getNewsByCategory(category: String): Flow<Result<NewsUIData>> {
        return fetchNewsData { newsApi.getNewsByCategory(category = category) }
    }


}
