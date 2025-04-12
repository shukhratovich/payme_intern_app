package com.example.data.repository.impl

import com.example.data.local.room.entity.NewsEntity
import com.example.data.remote.api.NewsApi
import com.example.data.remote.response.NewsSourcesData
import com.example.data.remote.response.toRoomData
import com.example.data.remote.response.toUIData
import com.example.domain.model.ui.NewsUIData
import com.example.data.repository.RemoteNewsRepository
import com.example.domain.model.Network
import com.google.gson.Gson
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
    ): Flow<Network<K>> =
        flow {
            val result = call()
            if (result.isSuccessful && result.body() != null) {
                emit(Network.Success(mapper(result.body()!!)))
            } else {
                emit(Network.Error(Throwable(result.errorBody()?.string() ?: "Unknown error")))
            }
        }.flowOn(Dispatchers.IO).catch { error ->
            emit(Network.Error(error))
        }

    override fun getNewsBySearch(q: String, from: String): Flow<Network<List<NewsUIData>>> {
        return fetchNewsData(
            call = { newsApi.getNewsBySearch(q = q, from = from) },
            mapper = { news ->
                news.articles?.map { article -> article.toUIData(null) } ?: listOf()
            })
    }

    override fun getNewsByCategory(category: String): Flow<Network<List<NewsEntity>>> {
        return fetchNewsData(
            call = { newsApi.getNewsByCategory(category = category) },
            mapper = { newsResponse ->
                newsResponse.articles?.map { article -> article.toRoomData() } ?: listOf()
            }
        )
    }

    override fun getNewsBySources(sourceId: String?): Flow<Network<List<NewsEntity>>> {
        return fetchNewsData(
            call = { newsApi.getNewsBySource(sourceId) },
            mapper = { newsResponse ->
                newsResponse.articles?.map { article -> article.toRoomData() } ?: listOf()
            }
        )
    }

    override suspend fun getSource(): Result<List<NewsSourcesData>> {
        return try {
            val response = newsApi.getSources()
            if (response.isSuccessful && response.body() != null) {
                val resultList = response.body()!!.sources ?: listOf()
                Result.success(resultList)
            } else {
                val errorBody =
                    response.errorBody()?.toString() ?: "{\"status\": \"Unknown Message\"}"
                val error = Gson().fromJson(errorBody, Error::class.java)
                Result.failure(Throwable("${error.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
