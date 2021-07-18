package com.balv.imdb.domain.repositories;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingData;

import com.balv.imdb.domain.models.ApiResult;
import com.balv.imdb.domain.models.Movie;

import java.util.List;

import io.reactivex.Observable;

public interface IMovieRepository {
    LiveData<List<Movie>> getLocalMovieData();
    LiveData<Movie> getMovieDetail(String id);
    Observable<ApiResult> getDetailFromNetwork(String id);
    Observable<ApiResult> getNextRemoteDataPage(int page);
    boolean initialized();
    int getCurrentDataSize();
    long getDataRefreshDate();
    void setDataRefreshDate(long time);
    Observable<PagingData<Movie>> getPagingData();
}
