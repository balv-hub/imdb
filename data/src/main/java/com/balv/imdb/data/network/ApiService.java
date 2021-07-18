package com.balv.imdb.data.network;


import com.balv.imdb.data.model.MovieData;
import com.balv.imdb.data.model.SearchData;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET(".")
    Observable<SearchData> getMoviesList(
            @Query("apikey") String apiKey,
            @Query("s") String searchText,
            @Query("page") int page
    );

    @GET(".")
    Call<SearchData> getMoviesListSync(
            @Query("apikey") String apiKey,
            @Query("s") String searchText,
            @Query("page") int page
    );

    @GET(".")
    Observable<MovieData> getMovieDetail(
            @Query("apikey") String apiKey,
            @Query("i") String imdbId
    );
}
