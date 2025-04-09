package com.example.data.repository.impl

import com.example.data.local.room.entity.NewsEntity
import com.example.data.remote.api.NewsApi
import com.example.data.remote.response.toRoomData
import com.example.data.remote.response.toUIData
import com.example.domain.model.ui.NewsSourceUIData
import com.example.domain.model.ui.NewsUIData
import com.example.data.repository.RemoteNewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteNewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi
) : RemoteNewsRepository {
    private inline fun <T, K> fetchNewsData(
        crossinline call: suspend () -> Response<T>,
        crossinline mapper: (T) -> K
    ): Flow<Result<K>> =
        flow {
            val result = call()
            if (result.isSuccessful && result.body() != null) {
                emit(Result.success(mapper(result.body()!!)))
            } else {
                emit(Result.failure(Throwable(result.errorBody().toString())))
            }
        }.flowOn(Dispatchers.IO).catch { error ->
            emit(Result.failure(error))
        }

    override fun getNewsBySearch(q: String, from: String): Flow<Result<List<NewsUIData>>> {
        return fetchNewsData(
            call = { newsApi.getNewsBySearch(q = q, from = from) },
            mapper = { news ->
                news.articles?.map { article -> article.toUIData(null) } ?: listOf()
            })
    }

    override fun getNewsByCategory(category: String): Flow<Result<List<NewsEntity>>> {
        return fetchNewsData(
            call = { newsApi.getNewsByCategory(category = category) },
            mapper = { newsResponse ->
                newsResponse.articles?.map { article -> article.toRoomData() } ?: listOf()
            }
        )
    }

    override fun getNewsBySources(sourceId: String?): Flow<Result<List<NewsEntity>>> {
        return fetchNewsData(
            call = { newsApi.getNewsBySource(sourceId) },
            mapper = { newsResponse ->
                newsResponse.articles?.map { article -> article.toRoomData() } ?: listOf()
            }
        )
    }

    override suspend fun getSource(): Result<List<Pair<String, String>>> {
        return try {
            val response = newsApi.getSources()
            if (response.isSuccessful && response.body() != null) {
                val resultList = response.body()!!.sources?.let { it.map { source ->
                    Pair(source.id, source.name) }
                } ?: listOf()
                Result.success(resultList)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
