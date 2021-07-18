package com.balv.imdb.domain.repositories;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingData;

import com.balv.imdb.domain.models.ApiResult;
import com.balv.imdb.domain.models.Movie;

import java.util.List;

import io.reactivex.Observable;

public interface IMovieRepository {
    Observable<List<Movie>> getLocalMovieData();
    Observable<Movie> getMovieDetail(String id);
    Observable<ApiResult> getDetailFromNetwork(String id);
    Observable<ApiResult> getNextRemoteDataPage(int page);
    int getCurrentDataSize();
    long getDataRefreshDate();
    void setDataRefreshDate(long time);
    Observable<PagingData<Movie>> getPagingData();
}
