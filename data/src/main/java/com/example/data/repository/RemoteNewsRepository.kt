package com.example.data.repository

import com.example.data.local.room.entity.NewsEntity
import com.example.data.remote.response.ArticlesData
import com.example.domain.model.ui.NewsSourceUIData
import com.example.domain.model.ui.NewsUIData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteNewsRepository {
    fun getNewsBySearch(q: String, from: String): Flow<Result<List<NewsUIData>>>
    fun getNewsByCategory(category: String): Flow<Result<List<NewsEntity>>>
    fun getNewsBySources(sourceId: String?): Flow<Result<List<NewsEntity>>>
    suspend fun getSource(): Result<List<Pair<String, String>>>
}