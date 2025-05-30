package com.balv.imdb.data.network

import com.balv.imdb.data.model.MovieData
import com.balv.imdb.data.model.SearchData
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET(".")
    suspend fun getMoviesList(
        @Query("apikey") apiKey: String?,
        @Query("s") searchText: String?,
        @Query("page") page: Int
    ): SearchData

    @GET(".")
    suspend fun getMoviesListSync(
        @Query("apikey") apiKey: String?,
        @Query("s") searchText: String?,
        @Query("page") page: Int
    ): SearchData?

    @GET(".")
    suspend fun getMovieDetail(
        @Query("apikey") apiKey: String?,
        @Query("i") imdbId: String?
    ): MovieData?
}
