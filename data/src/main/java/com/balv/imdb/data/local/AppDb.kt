package com.balv.imdb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.balv.imdb.data.local.dao.MovieDao
import com.balv.imdb.data.model.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
