package com.example.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.domain.entities.CategoryNews


@Entity(tableName = "news_table")
data class NewsEntity(
    val url: String?,
    val author: String?,
    val title: String?,
    val description: String?,
    @ColumnInfo("url_to_image") val urlToImage: String?,
    @ColumnInfo("published_at") val publishedAt: String?,
    val content: String?,
    val category: CategoryNews
)
