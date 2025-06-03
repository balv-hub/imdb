package com.balv.imdb.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.balv.imdb.data.local.AppDb
import com.balv.imdb.data.mapper.remoteMovieToEntity
import com.balv.imdb.data.model.entity.MovieEntity
import com.balv.imdb.data.network.ApiService
import com.balv.imdb.domain.repositories.IMovieRepository // Assuming this provides getDataRefreshDate()
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class PageKeyedRemoteMediator @Inject constructor(
    private val movieRepository: IMovieRepository, // Used for cache expiry check
    private val appDb: AppDb,
    private val apiService: ApiService,
) : RemoteMediator<Int, MovieEntity>() {

    companion object {
        private const val TAG = "PageKeyedRemoteMediator"
        private const val NETWORK_PAGE_SIZE = 20
        private const val ALL_MOVIES_REFRESH_RATE_THRESHOLD = 2 * 60 * 60 * 1000 // 2 hours in milliseconds
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        Log.d(TAG, "load() called. LoadType: $loadType, ConfigPageSize: ${state.config.pageSize}, ConfigInitialLoad: ${state.config.initialLoadSize}")

        return try {
            when (loadType) {
                LoadType.REFRESH -> {
                    var currentPageToFetch = 1 // Always start from page 1 for a full refresh
                    var itemsFetchedThisRefresh = 0
                    var moreDataFromApiForRefreshLoop = true
                    // How many items Paging wants initially from the PagingSource (DB)
                    val targetInitialLoadSize = state.config.initialLoadSize
                    var lastApiResponseTotalResults: Int = 0
                    var lastApiResponseTotalPages: Int = 0

                    Log.d(TAG, "REFRESH: Clearing database prior to fetching.")
                    // Clear old data in a single transaction
                    appDb.withTransaction {
                        // appDb.remoteKeysDao().clearRemoteKeys() // If you were using remote keys
                        appDb.movieDao().clearAll()
                    }

                    // Loop to fetch enough data for initialLoadSize or until API ends
                    while (itemsFetchedThisRefresh < targetInitialLoadSize && moreDataFromApiForRefreshLoop) {
                        Log.d(TAG, "REFRESH: Fetching network page $currentPageToFetch")
                        val response = apiService.discoverMovies(page = currentPageToFetch) // Assuming pageSize is handled by API or default
                        val moviesFromNetwork = response.results

                        if (moviesFromNetwork.isEmpty()) {
                            Log.d(TAG, "REFRESH: Network page $currentPageToFetch was empty. Assuming end of data from API.")
                            moreDataFromApiForRefreshLoop = false
                            lastApiResponseTotalResults = response.totalResults // Capture total results from last valid response
                            lastApiResponseTotalPages = response.totalPages
                            break // Exit loop if API returns an empty list for a page
                        }

                        val movieEntities = moviesFromNetwork.map { it.remoteMovieToEntity() }
                        appDb.movieDao().insertAll(movieEntities) // Insert within the loop or batch after

                        itemsFetchedThisRefresh += moviesFromNetwork.size
                        lastApiResponseTotalResults = response.totalResults
                        lastApiResponseTotalPages = response.totalPages
                        moreDataFromApiForRefreshLoop = response.page < response.totalPages // API indicates if more pages exist

                        Log.d(TAG, "REFRESH: Page $currentPageToFetch fetched. Items this page: ${moviesFromNetwork.size}. Total fetched this refresh: $itemsFetchedThisRefresh. More API data: $moreDataFromApiForRefreshLoop. API Total Pages: ${response.totalPages}")

                        if (!moreDataFromApiForRefreshLoop) break // API reported no more pages
                        currentPageToFetch++
                    }
                    // After the loop, update the repository's refresh date
                    movieRepository.setDataRefreshDate(System.currentTimeMillis())


                    val finalDbCount = appDb.movieDao().count()
                    val endOfPaginationReached = finalDbCount >= lastApiResponseTotalResults || !moreDataFromApiForRefreshLoop
                    Log.d(TAG, "REFRESH finished. DB count: $finalDbCount, Last API Total Results: $lastApiResponseTotalResults, More API data: $moreDataFromApiForRefreshLoop, EndReached: $endOfPaginationReached")
                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }

                LoadType.PREPEND -> {
                    // Prepending is usually not needed for a discover/feed like list
                    // that only appends. If your list can prepend, you'd implement logic here.
                    Log.d(TAG, "PREPEND: Reached, usually means end of data for prepending.")
                    MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val currentItemCount = appDb.movieDao().count()
                    // If DB is empty, APPEND might be called after a SKIP_INITIAL_REFRESH.
                    // In this case, we should fetch page 1.
                    val pageNumberToFetch = if (currentItemCount == 0) {
                        1
                    } else {
                        // Calculate next page based on items in DB and NETWORK_PAGE_SIZE
                        (currentItemCount / NETWORK_PAGE_SIZE) + 1
                    }

                    Log.d(TAG, "APPEND: Attempting to fetch network page $pageNumberToFetch. Current DB count: $currentItemCount")


                    val response = apiService.discoverMovies(page = pageNumberToFetch)
                    val moviesFromNetwork = response.results

                    if (moviesFromNetwork.isEmpty()) {
                        Log.d(TAG, "APPEND: Network page $pageNumberToFetch returned empty. Assuming end of pagination.")
                        MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        val movieEntities = moviesFromNetwork.map { it.remoteMovieToEntity() }
                        appDb.withTransaction { // Ensure insert is transactional
                            appDb.movieDao().insertAll(movieEntities)
                        }
                        val endReached = response.page >= response.totalPages
                        Log.d(TAG, "APPEND finished. Page: ${response.page}, Fetched: ${moviesFromNetwork.size}, API total pages: ${response.totalPages}, EndReached: $endReached")
                        MediatorResult.Success(endOfPaginationReached = endReached)
                    }
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error: ${e.localizedMessage}", e)
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error: ${e.code()} - ${e.message()}", e)
            MediatorResult.Error(e)
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error: ${e.localizedMessage}", e)
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val cacheTimeoutMs = ALL_MOVIES_REFRESH_RATE_THRESHOLD
        val lastRefreshTime = movieRepository.getDataRefreshDate() // Assuming this method exists
        val isCacheStale = (System.currentTimeMillis() - lastRefreshTime) >= cacheTimeoutMs

        Log.d(TAG, "initialize() called. LastRefresh: $lastRefreshTime, IsCacheStale: $isCacheStale")

        return if (isCacheStale || lastRefreshTime == 0L) { // Also refresh if never refreshed
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }
}