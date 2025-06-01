package com.balv.imdb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.balv.imdb.data.local.dao.MovieDao
import com.balv.imdb.data.model.MovieEntity
import com.balv.imdb.data.model.MovieTypeConverter


@Database(entities = [MovieEntity::class], version = 1)
@TypeConverters(MovieTypeConverter::class)
abstract class AppDb : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
