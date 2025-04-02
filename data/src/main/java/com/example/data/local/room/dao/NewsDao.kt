package com.example.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.room.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNews()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(items: List<NewsEntity>)

    @Query("SELECT * FROM news_table")
    fun getAllNews(): Flow<List<NewsEntity>>

}
