package com.balv.imdb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.balv.imdb.data.local.dao.GenreDao
import com.balv.imdb.data.local.dao.MovieDao
import com.balv.imdb.data.local.dao.MovieDetailDao
import com.balv.imdb.data.model.GenreEntity
import com.balv.imdb.data.model.ListStringConverter
import com.balv.imdb.data.model.MovieDetailEntity
import com.balv.imdb.data.model.MovieEntity
import com.balv.imdb.data.model.MovieTypeConverter
import com.balv.imdb.data.model.ProductionCompanyListConverter
import com.balv.imdb.data.model.ProductionCountryListConverter
import com.balv.imdb.data.model.SpokenLanguageListConverter


@Database(entities = [MovieEntity::class,GenreEntity::class, MovieDetailEntity::class], version = 1)
@TypeConverters(
    MovieTypeConverter::class,
    ListStringConverter::class,
    ProductionCompanyListConverter::class,
    ProductionCountryListConverter::class,
    SpokenLanguageListConverter::class
)
abstract class AppDb : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movideDetailDao(): MovieDetailDao
    abstract fun genreDao(): GenreDao
}
