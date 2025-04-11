package com.example.data.remote.api

import androidx.core.os.BuildCompat
import com.example.data.BuildConfig
import com.example.data.remote.response.NewsResponse
import com.example.data.remote.response.NewsSourceData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything")
    suspend fun getNewsBySearch(
        @Query("q") q: String,
        @Query("from") from: String,
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ): Response<NewsResponse>

    @GET("top-headlines")
    suspend fun getNewsByCategory(
        @Query("category") category: String?,
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ): Response<NewsResponse>

    @GET("top-headlines")
    suspend fun getNewsBySource(
        @Query("sources") source: String?,
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ): Response<NewsResponse>

    @GET("top-headlines/sources")
    suspend fun getSources(
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY
    ): Response<NewsSourceData>

}