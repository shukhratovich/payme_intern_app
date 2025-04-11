package com.example.data.repository

import com.example.data.local.room.entity.NewsEntity
import com.example.data.remote.response.NewsSourcesData
import com.example.domain.model.Network
import com.example.domain.model.ui.NewsUIData
import kotlinx.coroutines.flow.Flow

interface RemoteNewsRepository {
    fun getNewsBySearch(q: String, from: String): Flow<Network<List<NewsUIData>>>
    fun getNewsByCategory(category: String): Flow<Network<List<NewsEntity>>>
    fun getNewsBySources(sourceId: String?): Flow<Network<List<NewsEntity>>>
    suspend fun getSource(): Result<List<NewsSourcesData>>
}