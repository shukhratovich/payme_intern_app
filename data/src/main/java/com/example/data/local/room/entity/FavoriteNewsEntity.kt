package com.example.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.domain.model.ui.ArticleWithFavouriteUIData

@Entity(tableName = "favorites")
data class FavoriteNewsEntity(
    @PrimaryKey
    val url: String,
    @ColumnInfo(defaultValue = "false") val isFavourite: Boolean = false,
)


data class ArticleWithFavourite(
    @Embedded val article: NewsEntity,
    @Relation(
        parentColumn = "url",
        entityColumn = "url"
    )
    val isFavourite: FavoriteNewsEntity?
)
