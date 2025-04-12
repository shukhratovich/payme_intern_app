package com.example.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.ui.CategoryNews
import com.example.domain.model.ui.NewsUIData


@Entity(tableName = "news_table")
data class NewsEntity(
    @PrimaryKey
    val url: String,
    val author: String?,
    val title: String?,
    val description: String?,
    @ColumnInfo("url_to_image") val urlToImage: String?,
    @ColumnInfo("published_at") val publishedAt: String?,
    val content: String?,
    val category: CategoryNews?,
    val sourceId: String?,
    val sourceName: String?,
)

fun NewsEntity.toUIData(): NewsUIData = NewsUIData(
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content,
    category = category ?: CategoryNews.NULL,
    sourceId = sourceId,
    sourceName = sourceName
)
