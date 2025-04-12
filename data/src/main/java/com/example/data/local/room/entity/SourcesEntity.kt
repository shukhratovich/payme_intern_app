package com.example.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.ui.NewsSourcesUIData

@Entity(tableName = "source_table")
data class SourcesEntity(
    @PrimaryKey
    val sourceId: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String
)

fun SourcesEntity.toUIData(): NewsSourcesUIData =
    NewsSourcesUIData(
        id = sourceId,
        name = name,
        description = description,
        url = url,
        category = category,
        language = language,
        country = country
    )