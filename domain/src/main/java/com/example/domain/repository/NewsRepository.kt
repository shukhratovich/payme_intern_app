package com.example.domain.repository

import com.example.domain.entities.NewsUIData
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNewsBySearch(q: String, from: String): Flow<Result<NewsUIData>>
}