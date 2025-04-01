package com.example.data.remote.response

import com.example.domain.entities.ArticleUIData
import com.example.domain.entities.NewsUIData
import com.example.domain.entities.SourceUIData
import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("totalResults")
    val totalResults: Int?,
    @SerializedName("articles")
    val articles: List<ArticlesData>?
)

data class ArticlesData(
    @SerializedName("source")
    val source: SourceData?,
    @SerializedName("author")
    val author: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("urlToImage")
    val urlToImage: String?,
    @SerializedName("publishedAt")
    val publishedAt: String?,
    @SerializedName("content")
    val content: String?
)

data class SourceData(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?
)

fun NewsResponse.toUIData(): NewsUIData =
    NewsUIData(
        status = status ?: "",
        totalResults = totalResults ?: 0,
        articles = articles?.map { it.toUIData() } ?: emptyList()
    )

fun ArticlesData.toUIData(): ArticleUIData =
    ArticleUIData(
        source = (source ?: SourceData(id = null, name = null)).toUiData(),
        author = author ?: "",
        title = title ?: "",
        description = description ?: "",
        url = url ?: "",
        urlToImage = urlToImage ?: "",
        publishedAt = publishedAt ?: "",
        content = content ?: ""
    )

fun SourceData.toUiData(): SourceUIData = SourceUIData(id = id, name = name)