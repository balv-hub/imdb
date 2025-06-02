package com.balv.imdb.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.balv.imdb.data.Constant
import com.balv.imdb.data.local.AppDb
import com.balv.imdb.data.mapper.Mapper
import com.balv.imdb.data.model.MovieEntity
import com.balv.imdb.data.network.ApiService
import com.balv.imdb.domain.repositories.IMovieRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.ceil

@OptIn(ExperimentalPagingApi::class)
@Singleton
class PageKeyedRemoteMediator @Inject constructor(
    private val movieRepository: IMovieRepository,
    private val appDb: AppDb,
    private val apiService: ApiService,
) : RemoteMediator<Int, MovieEntity>() {

    companion object {
        private const val TAG = "PageKeyedRemoteMediator"
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val pageNumber = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val count = appDb.movieDao().count()
                    ceil(count / 10.0).toInt() + 1
                }
            }

            val data = apiService.discoverMovies(page = pageNumber)

            val movieList = data.results.map { Mapper.remoteMovieToEntity(it) }

            appDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDb.movieDao().clearAll()
                }
                appDb.movieDao().insertAll(movieList)
            }

            val totalResults = data.totalResults
            val endReached = appDb.movieDao().count() >= totalResults

            MediatorResult.Success(endOfPaginationReached = endReached)

        } catch (e: IOException) {
            Log.e(TAG, "Network error: ${e.localizedMessage}", e)
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error: ${e.localizedMessage}", e)
            MediatorResult.Error(e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error: ${e.localizedMessage}", e)
            MediatorResult.Error(e)
        }
    }


    override suspend fun initialize(): InitializeAction {
        val fourHoursMs = 4 * 60 * 60 * 1000
        return if (System.currentTimeMillis() - movieRepository.getDataRefreshDate() < fourHoursMs) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }
}
