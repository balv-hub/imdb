package com.balv.imdb.data.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.balv.imdb.data.Constant
import com.balv.imdb.data.local.AppDb
import com.balv.imdb.data.local.UserPreference
import com.balv.imdb.data.mapper.Mapper
import com.balv.imdb.data.mediator.PageKeyedRemoteMediator
import com.balv.imdb.data.model.MovieData
import com.balv.imdb.data.model.MovieEntity
import com.balv.imdb.data.model.MovieNetworkObject
import com.balv.imdb.data.model.SearchData
import com.balv.imdb.data.network.ApiService
import com.balv.imdb.domain.models.ApiResult
import com.balv.imdb.domain.models.ErrorResult
import com.balv.imdb.domain.models.Movie
import com.balv.imdb.domain.repositories.IMovieRepository
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject

@SuppressLint("CheckResult")
class AppRepo @Inject constructor(
    private val appDb: AppDb,
    private val apiService: ApiService,
    private val userPref: UserPreference
) : IMovieRepository {

    override suspend fun getMovieDetail(id: String): Flow<Movie?> {
        return appDb.movieDao().getMovieDetailSource(id)
            .map { input: MovieEntity? -> input?.let { Mapper.entityToDomain(it) } }
    }

    override suspend fun getDetailFromNetwork(id: String): ApiResult<Movie?> {
        return getRemoteResponse {
            apiService.getMovieDetail(Constant.API_KEY, id)?.let { result: MovieData? ->
                result?.let {
                    Mapper.detailToEntity(it)
                    appDb.movieDao().updateMovies()
                    Mapper.networkToDomain(it)
                }
            }
        }
    }

    override suspend fun getNextRemoteDataPage(page: Int): ApiResult<SearchData> {
        return getRemoteResponse {
            apiService.getMoviesList(Constant.API_KEY, Constant.MOVIE_KEYWORD, page)
                .let { result: SearchData ->
                    Log.i(TAG, "getRemoteData: listSize=" + result.list?.size)
                    result.list?.map { networkObject: MovieNetworkObject ->
                        Mapper.networkToEntity(networkObject)
                    }?.also {
                        appDb.movieDao().insertAll(it)
                    }
                    result
                }
        }
    }

    private suspend fun <T> getRemoteResponse(block: suspend () -> T): ApiResult<T> {
        return kotlin.runCatching {
            ApiResult.success(block())
        }.getOrElse { e ->
            if (e is HttpException && e.code() == Constant.HTTP_ERROR_SESSION_EXPIRED) {
                ApiResult.error(ErrorResult(401, e.message.orEmpty()))
            } else {
                ApiResult.error(ErrorResult(Constant.NETWORK_ERROR, e.message.orEmpty()))
            }
        }
    }


    override suspend fun getDataRefreshDate(): Long {
        return userPref.getLongValue(Constant.DATA_REFRESH)
    }

    override suspend fun setDataRefreshDate(date: Long) {
        userPref.saveLongValue(Constant.DATA_REFRESH, date)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun allMoviesPaging(): Flow<PagingData<Movie>> {
        val config = PagingConfig(
            10, 10,
            true, 10, 200
        )
        val pager = Pager(
            config, 1,
            PageKeyedRemoteMediator(this, appDb, apiService)
        ) { appDb.movieDao().pagingSource() }

        return pager.flow
            .map { pagingData ->
                pagingData.map { input -> input.let { Mapper.entityToDomain(it) } }
            }
    }

    companion object {
        private const val TAG = "AppRepo"
    }
}
