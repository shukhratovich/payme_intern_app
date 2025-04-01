package com.example.data.remote.api

import com.example.data.remote.response.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything")
    suspend fun getNewsBySearch(
        @Query("q") q: String,
        @Query("from") from: String,
        @Query("apiKey") apiKey: String = NEWS_API_KEY
    ): Response<NewsResponse>

    @GET("top-headlines")
    suspend fun getNewsByCategory(
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = NEWS_API_KEY
    ): Response<NewsResponse>


    companion object {
        private const val NEWS_API_KEY: String = "ad11181837d14643b0792c23a724f8be"
    }
}