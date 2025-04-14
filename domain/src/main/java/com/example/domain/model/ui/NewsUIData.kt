package com.example.domain.model.ui

data class NewsUIData(
    val url: String,
    val author: String?,
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
    val category: CategoryNews?,
    val sourceId: String?,
    val sourceName: String?,
)

val NewsUIData.uiDate get() = "${author.orEmpty()} - ${publishedAt.orEmpty()}"