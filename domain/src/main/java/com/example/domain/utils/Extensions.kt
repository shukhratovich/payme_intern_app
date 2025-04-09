package com.example.domain.utils

import com.example.domain.model.ui.CategoryNews
import kotlin.text.uppercase

fun String.toNewsCategory(): CategoryNews {
    return when (this.uppercase()) {
        "BUSINESS" -> CategoryNews.BUSINESS
        "GENERAL" -> CategoryNews.GENERAL
        "ENTERTAINMENT" -> CategoryNews.ENTERTAINMENT
        "TECHNOLOGY" -> CategoryNews.TECHNOLOGY
        "HEALTH" -> CategoryNews.HEALTH
        "SCIENCE" -> CategoryNews.SCIENCE
        "SPORTS" -> CategoryNews.SPORTS
        else -> CategoryNews.NULL
    }
}