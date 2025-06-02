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
import com.balv.imdb.data.model.SearchData
import com.balv.imdb.data.network.ApiService
import com.balv.imdb.domain.models.ApiResult
import com.balv.imdb.domain.models.ErrorResult
import com.balv.imdb.domain.models.Movie
import com.balv.imdb.domain.repositories.IMovieRepository
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

    override suspend fun getMovieDetailLocal(id: Int): Flow<Movie?> {
        return appDb.movieDao().getMovieDetailLocal(id)
            .map { input-> input?.let { Mapper.entityToDomain(it) } }
    }

    override suspend fun getDetailFromNetwork(id: Int): ApiResult<Movie?> {
        return getRemoteResponse {
            apiService.getMovieDetail(movieId = id).let { result->
                val localEntity = Mapper.remoteMovieToEntity(result).copy(
                    polledDate = System.currentTimeMillis()
                )
                appDb.movieDao().updateMovies(localEntity)
                Mapper.networkToDomain(result)
            }
        }
    }

    override suspend fun getNextRemoteDataPage(page: Int): ApiResult<SearchData> {
        return getRemoteResponse {
            apiService.discoverMovies(page = page)
                .let { result: SearchData ->
                    Log.i(TAG, "getRemoteData: listSize=" + result.results.size)
                    result.results.map { remote ->
                        Mapper.remoteMovieToEntity(remote)
                    }.also {
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
            10, 1,
            true, 10, 30
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

    override fun getPopularMoviesFetchedDate(): Long =
        userPref.getLongValue(PREF_POPULAR_FETCHED_DATE)

    override fun getPopularMovies(): Flow<List<Movie>> {
        return appDb.movieDao().getTop20PopularMovies().map {
            Log.i(TAG, "getPopularMovies: $it")
            it.map { movieEntity -> Mapper.entityToDomain(movieEntity) }
        }
    }

    override suspend fun refreshPopularMovies() {
        getRemoteResponse {
            apiService.getPopularMovies()
        }.data?.results?.let { listMv ->
            userPref.saveLongValue(PREF_POPULAR_FETCHED_DATE, System.currentTimeMillis())
            appDb.movieDao().insertAll(
                listMv.map {
                    Mapper.remoteMovieToEntity(it)
                }
            )
        }
    }

    companion object {
        private const val TAG = "AppRepo"
        private const val PREF_POPULAR_FETCHED_DATE = "PREF_POPULAR_FETCHED_DATE"
    }
}
