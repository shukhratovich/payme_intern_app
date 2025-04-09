package com.example.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.local.room.entity.ArticleWithFavourite
import com.example.data.local.room.entity.FavoriteNewsEntity
import com.example.data.local.room.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNews(item: NewsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(items: List<NewsEntity>)

    @Query("SELECT * FROM news_table")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Transaction
    @Query("SELECT * FROM news_table")
    fun getAllNews2(): Flow<List<ArticleWithFavourite>>

    @Transaction
    @Query("SELECT * FROM favorites WHERE isFavourite = 1")
    fun getAllFavouriteNews(): Flow<List<ArticleWithFavourite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markAsOpposite(entity: FavoriteNewsEntity)

}
