package com.balv.imdb.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.balv.imdb.data.model.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MovieDao {
    @Query("SELECT * FROM movies")
    abstract fun getMainMovieList(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieEntities: List<MovieEntity>)

    @Upsert
    abstract suspend fun updateMovies(vararg movieEntities: MovieEntity)

    @Query("SELECT * FROM movies WHERE id = :id")
    abstract fun getMovieDetailLocal(id: Int): Flow<MovieEntity?>

    @Delete
    abstract suspend fun deleteItem(vararg ids: MovieEntity?)

    @Query("SELECT COUNT(*) FROM movies")
    abstract suspend fun count(): Int

    @Query("DELETE FROM movies")
    abstract fun clearAll()

    @Query("SELECT * FROM movies")
    abstract fun pagingSource(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies ORDER BY popularity DESC LIMIT 20")
    abstract fun getTop20PopularMovies(): Flow<List<MovieEntity>>
}
