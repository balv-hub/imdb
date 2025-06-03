package com.balv.imdb.domain.repositories

import androidx.paging.PagingData
import com.balv.imdb.domain.models.ApiResult
import com.balv.imdb.domain.models.Movie
import com.balv.imdb.domain.models.MovieCredits
import com.balv.imdb.domain.models.MovieDetail
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    suspend fun getMovieDetailLocalFlow(id: Int): Flow<MovieDetail?>
    suspend fun getMovieDetailById(id: Int): MovieDetail?
    suspend fun getDetailFromNetwork(id: Int): ApiResult<MovieDetail?>
    suspend fun getNextRemoteDataPage(page: Int): ApiResult<*>
    suspend fun getDataRefreshDate(): Long
    suspend fun setDataRefreshDate(date: Long)
    fun allMoviesPaging(): Flow<PagingData<Movie>>
    fun getPopularMoviesFetchedDate(): Long
    fun getGenreFetchedDate(): Long
    fun getPopularMovies(): Flow<List<Movie>>
    suspend fun refreshPopularMovies()
    suspend fun refreshGenreList()
    suspend fun getGenresNameById(ids: List<Int>): List<String>
    suspend fun getMovieCredits(id: Int): MovieCredits
    suspend fun updateMovieDetail(detail: MovieDetail)
}
