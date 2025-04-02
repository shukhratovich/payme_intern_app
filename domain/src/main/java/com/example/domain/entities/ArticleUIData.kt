package com.example.domain.entities

data class ArticleUIData(
    val source: SourceUIData?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)


data class SourceUIData(
    val id: String?,
    val name: String?
)