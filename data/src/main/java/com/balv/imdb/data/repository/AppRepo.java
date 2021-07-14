package com.balv.imdb.data.repository;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.balv.imdb.data.Constant;
import com.balv.imdb.data.local.AppDb;
import com.balv.imdb.data.local.UserPreference;
import com.balv.imdb.data.mapper.Mapper;
import com.balv.imdb.data.model.MovieEntity;
import com.balv.imdb.data.network.ApiService;
import com.balv.imdb.domain.models.ApiResult;
import com.balv.imdb.domain.models.ErrorResult;
import com.balv.imdb.domain.models.Movie;
import com.balv.imdb.domain.repositories.IMovieRepository;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

@SuppressLint("CheckResult")
public class AppRepo implements IMovieRepository {
    private static final String TAG = "AppRepo";

    public AppDb mAppDb;

    public ApiService mApiService;

    @Inject
    Mapper mapper;

    @Inject
    UserPreference mUserPref;

    @Inject
    public AppRepo(AppDb mAppDb, ApiService mApiService, Mapper nwToDbMapper) {
        this.mapper = nwToDbMapper;
        this.mAppDb = mAppDb;
        this.mApiService = mApiService;
    }

    @Override
    public LiveData<List<Movie>> getLocalMovieData() {
        return Transformations.map(mAppDb.movieDao().getMainMovieList(),
                list -> list.stream()
                        .map(movieEntity -> mapper.entityToDomain(movieEntity))
                        .collect(Collectors.toList()));
    }

    @Override
    public LiveData<Movie> getMovieDetail(String id) {
        return Transformations.map(mAppDb.movieDao().getMovieLiveData(id),
                movie -> {
                    if (TextUtils.isEmpty(movie.imdbRated)) {
                        getDetailFromNetwork(id);
                    }
                    return mapper.entityToDomain(movie);
                }
        );
    }

    @Override
    public Observable<ApiResult> getDetailFromNetwork(String id) {
        return mApiService.getMovieDetail(Constant.API_KEY, id)
                .subscribeOn(Schedulers.io())
                .map(result -> {
                    mAppDb.movieDao().updateMovies(mapper.detailToEntity(result));
                    return new ApiResult(result);
                });
    }

    @Override
    public Observable<ApiResult> getNextRemoteDataPage(int page) {
        return mApiService.getMoviesList(Constant.API_KEY, Constant.MOVIE_KEYWORD, page)
                .subscribeOn(Schedulers.io())
                .map(result -> {
                    Log.i(TAG, "getRemoteData: listSize=" + result.list.size());
                    List<MovieEntity> entityList = result.list.stream()
                            .map(networkObject -> mapper.networkToEntity(networkObject))
                            .collect(Collectors.toList());
                    mAppDb.movieDao().insertAllMovies(entityList);

                    return new ApiResult(result);
                })
                .onErrorResumeNext(e -> {
                    Log.i(TAG, "getNextRemoteDataPage: error" + e);
                    if (e instanceof HttpException && ((HttpException) e).code() == Constant.HTTP_ERROR_SESSION_EXPIRED)
                        return Observable.just(new ApiResult(new ErrorResult(401, e.getMessage())));
                    else
                        return Observable.just(new ApiResult(new ErrorResult(Constant.NETWORK_ERROR, e.getMessage())));
                });
    }

    @Override
    public boolean initialized() {
        return mUserPref.getBooleanValue(Constant.INITIALIZED);
    }

    @Override
    public int getCurrentDataSize() {
        return mAppDb.movieDao().getCount();
    }


}
