package com.example.data.remote.response

import com.example.data.local.room.entity.NewsEntity
import com.example.domain.model.ui.ArticleUIData
import com.example.domain.model.ui.CategoryNews
import com.example.domain.model.ui.NewsSourceUIData
import com.example.domain.model.ui.NewsSourcesUIData
import com.example.domain.model.ui.NewsUIData
import com.example.domain.model.ui.SourceUIData
import com.google.gson.annotations.SerializedName
import kotlin.String

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

data class NewsSourceData(
    @SerializedName("status")
    val status: String?,
    @SerializedName("sources")
    val sources: List<NewsSourcesData>?
)

data class NewsSourcesData(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("category")
    val category: String?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("country")
    val country: String?
)

fun ArticlesData.toUIData(category: CategoryNews?): NewsUIData =
    NewsUIData(
        author = author,
        title = title,
        description = description,
        url = url ?: "",
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
        category = category,
        sourceId = source?.id,
        sourceName = source?.name
    )

fun SourceData.toUiData(): SourceUIData = SourceUIData(id = id, name = name)

fun NewsSourceData.toUIData(): NewsSourceUIData = NewsSourceUIData(
    status = status ?: "",
    sources = sources?.map { it.toUIData() } ?: emptyList()
)

fun NewsSourcesData.toUIData(): NewsSourcesUIData = NewsSourcesUIData(
    id = id ?: "",
    name = name ?: "",
    description = description ?: "",
    url = url ?: "",
    category = category ?: "",
    language = language ?: "",
    country = country ?: ""
)

fun ArticlesData.toRoomData(): NewsEntity = NewsEntity(
    url = this.url ?: "",
    author = this.author,
    title = this.title,
    description = this.description,
    urlToImage = this.urlToImage,
    publishedAt = this.publishedAt,
    content = this.content,
    category = null,
    sourceId = null,
    sourceName = null,
)