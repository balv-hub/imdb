package com.balv.imdb.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.balv.imdb.data.model.entity.MovieDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MovieDetailDao {
    @Query("SELECT * FROM movie_details")
    abstract fun getMainMovieList(): Flow<List<MovieDetailEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(movieEntities: List<MovieDetailEntity>)

    @Upsert
    abstract suspend fun updateMovies(vararg movieEntities: MovieDetailEntity)

    @Query("SELECT * FROM movie_details WHERE id = :id")
    abstract fun getMovieDetailLocalFlow(id: Int): Flow<MovieDetailEntity?>

    @Query("SELECT * FROM movie_details WHERE id = :id")
    abstract fun getMovieDetailLocal(id: Int): MovieDetailEntity?


    @Delete
    abstract suspend fun deleteItem(vararg ids: MovieDetailEntity?)

    @Query("SELECT COUNT(*) FROM movie_details")
    abstract suspend fun count(): Int

    @Query("DELETE FROM movie_details")
    abstract fun clearAll()

    @Query("SELECT * FROM movie_details")
    abstract fun pagingSource(): PagingSource<Int, MovieDetailEntity>

    @Query("SELECT * FROM movie_details ORDER BY popularity DESC LIMIT 20")
    abstract fun getTop20PopularMovies(): Flow<List<MovieDetailEntity>>
}
