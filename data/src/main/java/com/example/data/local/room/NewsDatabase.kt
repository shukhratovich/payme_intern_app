package com.example.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.room.dao.NewsDao
import com.example.data.local.room.entity.FavoriteNewsEntity
import com.example.data.local.room.entity.NewsEntity

@Database(entities = [NewsEntity::class, FavoriteNewsEntity::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}