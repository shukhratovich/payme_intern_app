package com.example.domain.model.ui

data class ArticleUIData(
    val source: SourceUIData?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
    val isFavorite: Boolean = false,
    val category: CategoryNews = CategoryNews.GENERAL
)


data class SourceUIData(
    val id: String?,
    val name: String?
)