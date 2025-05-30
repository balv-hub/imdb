package com.balv.imdb.domain.repositories

import androidx.paging.PagingData
import com.balv.imdb.domain.models.ApiResult
import com.balv.imdb.domain.models.Movie
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {
    suspend fun getMovieDetail(id: String): Flow<Movie?>
    suspend fun getDetailFromNetwork(id: String): ApiResult<Movie?>
    suspend fun getNextRemoteDataPage(page: Int): ApiResult<*>
    suspend fun getDataRefreshDate(): Long
    suspend fun setDataRefreshDate(date: Long)
    fun allMoviesPaging(): Flow<PagingData<Movie>>
}
