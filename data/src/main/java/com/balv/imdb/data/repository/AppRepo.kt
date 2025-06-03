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
import com.balv.imdb.data.mapper.entityToDomain
import com.balv.imdb.data.mapper.networkToDomain
import com.balv.imdb.data.mapper.remoteGenreToEntity
import com.balv.imdb.data.mapper.remoteMovieToEntity
import com.balv.imdb.data.mapper.toDomain
import com.balv.imdb.data.mapper.toEntity
import com.balv.imdb.data.mediator.PageKeyedRemoteMediator
import com.balv.imdb.data.model.dto.SearchData
import com.balv.imdb.data.network.ApiService
import com.balv.imdb.domain.models.ApiResult
import com.balv.imdb.domain.models.ErrorResult
import com.balv.imdb.domain.models.Movie
import com.balv.imdb.domain.models.MovieCredits
import com.balv.imdb.domain.models.MovieDetail
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

    override suspend fun getMovieDetailLocalFlow(id: Int): Flow<MovieDetail?> {
        return appDb.movideDetailDao().getMovieDetailLocalFlow(id).map { input -> input?.toDomain() }
    }

    override suspend fun getMovieDetailById(id: Int): MovieDetail? {
        return appDb.movideDetailDao().getMovieDetailLocal(id)?.toDomain()
    }

    override suspend fun getDetailFromNetwork(id: Int): ApiResult<MovieDetail?> {
        return getRemoteResponse {
            apiService.getMovieDetail(movieId = id).let { result ->
                val localEntity = result.toEntity().copy(
                    lastRefreshed = System.currentTimeMillis()
                )
                appDb.movideDetailDao().insertAll(listOf(localEntity))
                result.networkToDomain()
            }
        }
    }

    override suspend fun getNextRemoteDataPage(page: Int): ApiResult<SearchData> {
        return getRemoteResponse {
            apiService.discoverMovies(page = page).let { result: SearchData ->
                Log.i(TAG, "getRemoteData: listSize=" + result.results.size)
                result.results.map { remote ->
                    remote.remoteMovieToEntity()
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
            e.printStackTrace()
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
            20, 5, false, 24, PagingConfig.MAX_SIZE_UNBOUNDED
        )
        val pager = Pager(
            config, 1, PageKeyedRemoteMediator(this, appDb, apiService)
        ) { appDb.movieDao().pagingSource() }

        return pager.flow.map { pagingData ->
            pagingData.map { input -> input.entityToDomain() }
        }
    }

    override fun getPopularMoviesFetchedDate(): Long =
        userPref.getLongValue(PREF_POPULAR_FETCHED_DATE)

    override fun getPopularMovies(): Flow<List<Movie>> {
        return appDb.movieDao().getTop20PopularMovies().map {
            Log.i(TAG, "getPopularMovies: $it")
            it.map { movieEntity -> movieEntity.entityToDomain() }
        }
    }

    override suspend fun refreshPopularMovies() {
        getRemoteResponse {
            apiService.getPopularMovies()
        }.data?.results?.let { listMv ->
            userPref.saveLongValue(PREF_POPULAR_FETCHED_DATE, System.currentTimeMillis())
            appDb.movieDao().insertAll(
                listMv.map {
                    it.remoteMovieToEntity()
                })
        }
    }

    override suspend fun refreshGenreList() {
        getRemoteResponse {
            apiService.getMovieGenreList()
        }.data?.genres?.let { genres ->
            userPref.saveLongValue(PREF_GENRE_FETCHED_DATE, System.currentTimeMillis())
            appDb.genreDao().replaceWithNewGenres(
                genres.map { it.remoteGenreToEntity() })
        }
    }

    override fun getGenreFetchedDate(): Long = userPref.getLongValue(PREF_GENRE_FETCHED_DATE)

    override suspend fun getGenresNameById(ids: List<Int>): List<String> {
        return appDb.genreDao().getNamesById(ids)
    }

    override suspend fun getMovieCredits(id: Int): MovieCredits {
        return apiService.getMovieCredits(id).toDomain()
    }

    override suspend fun updateMovieDetail(detail: MovieDetail) {
        appDb.movideDetailDao().updateMovies(detail.toEntity())
    }

    companion object {
        private const val TAG = "AppRepo"
        private const val PREF_POPULAR_FETCHED_DATE = "PREF_POPULAR_FETCHED_DATE"
        private const val PREF_GENRE_FETCHED_DATE = "PREF_GENRE_FETCHED_DATE"
    }
}
