package com.balv.imdb.data.network

import com.balv.imdb.data.model.dto.GenreList
import com.balv.imdb.data.model.dto.MovieCreditsResponseRemote
import com.balv.imdb.data.model.dto.MovieDetailRemote
import com.balv.imdb.data.model.dto.SearchData
import com.balv.imdb.domain.models.MovieCredits
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET(".")
    suspend fun getMoviesList(
        @Query("apikey") apiKey: String?,
        @Query("page") page: Int
    ): SearchData

    @GET(".")
    suspend fun getMoviesListSync(
        @Query("apikey") apiKey: String?,
        @Query("s") searchText: String?,
        @Query("page") page: Int
    ): SearchData?

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): MovieDetailRemote

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc",
    ): SearchData

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): SearchData

    @GET("genre/movie/list")
    suspend fun getMovieGenreList(
        @Query("language") language: String="en"
    ): GenreList

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId:Int
    ): MovieCreditsResponseRemote
}
