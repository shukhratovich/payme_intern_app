package com.example.domain.entities

data class NewsUIData(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleUIData>
)