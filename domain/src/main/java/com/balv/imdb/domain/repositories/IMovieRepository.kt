package com.balv.imdb.domain.repositories

import androidx.paging.PagingData
import com.balv.imdb.domain.models.ApiResult
import com.balv.imdb.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    suspend fun getMovieDetailLocal(id: Int): Flow<Movie?>
    suspend fun getDetailFromNetwork(id: Int): ApiResult<Movie?>
    suspend fun getNextRemoteDataPage(page: Int): ApiResult<*>
    suspend fun getDataRefreshDate(): Long
    suspend fun setDataRefreshDate(date: Long)
    fun allMoviesPaging(): Flow<PagingData<Movie>>
    fun getPopularMoviesFetchedDate(): Long
    fun getPopularMovies(): Flow<List<Movie>>
    suspend fun refreshPopularMovies()
}
