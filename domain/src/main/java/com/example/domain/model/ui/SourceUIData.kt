package com.example.domain.model.ui

data class NewsSourceUIData(
    val status: String,
    val sources: List<NewsSourcesUIData>
)

data class NewsSourcesUIData(
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String
)
