package com.example.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.room.dao.NewsDao
import com.example.data.local.room.entity.FavoriteNewsEntity
import com.example.data.local.room.entity.NewsEntity
import com.example.data.local.room.entity.SourcesEntity

@Database(
    entities = [NewsEntity::class, FavoriteNewsEntity::class, SourcesEntity::class],
    version = 2
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}