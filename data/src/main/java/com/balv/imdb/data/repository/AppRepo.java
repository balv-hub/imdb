package com.balv.imdb.data.repository;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingDataTransforms;
import androidx.paging.rxjava2.PagingRx;

import com.balv.imdb.data.Constant;
import com.balv.imdb.data.executor.PagingExecutor;
import com.balv.imdb.data.local.AppDb;
import com.balv.imdb.data.local.UserPreference;
import com.balv.imdb.data.mapper.Mapper;
import com.balv.imdb.data.mediator.PageKeyedRemoteMediator;
import com.balv.imdb.data.model.MovieEntity;
import com.balv.imdb.data.network.ApiService;
import com.balv.imdb.domain.models.ApiResult;
import com.balv.imdb.domain.models.ErrorResult;
import com.balv.imdb.domain.models.Movie;
import com.balv.imdb.domain.repositories.IMovieRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

@SuppressLint("CheckResult")
public class AppRepo implements IMovieRepository {
    private static final String TAG = "AppRepo";

    public AppDb mAppDb;

    public ApiService mApiService;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2;

    @Inject
    Mapper mapper;

    @Inject
    UserPreference mUserPref;

    @Inject
    public AppRepo(AppDb mAppDb, ApiService mApiService, Mapper nwToDbMapper, UserPreference preference) {
        this.mapper = nwToDbMapper;
        this.mAppDb = mAppDb;
        this.mApiService = mApiService;
        this.mUserPref = preference;
    }

    @Override
    public Observable<List<Movie>> getLocalMovieData() {
        return mAppDb.movieDao().getMainMovieList().map(list ->
                list.stream()
                        .map(movieEntity -> mapper.entityToDomain(movieEntity))
                        .collect(Collectors.toList()));

    }

    @Override
    public Observable<Movie> getMovieDetail(String id) {
        return mAppDb.movieDao().setMovieDetailSource(id)
                .map(mapper::entityToDomain);
    }

    public static final Executor EXECUTOR = new PagingExecutor (CORE_POOL_SIZE, MAXIMUM_POOL_SIZE);
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
                    mAppDb.movieDao().insertAll(entityList);

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
    public int getCurrentDataSize() {
        return mAppDb.movieDao().getCount();
    }

    @Override
    public long getDataRefreshDate() {
        return mUserPref.getLongValue(Constant.DATA_REFRESH);
    }

    @Override
    public void setDataRefreshDate(long time) {
        mUserPref.saveLongValue(Constant.DATA_REFRESH, time);
    }

    @Override
    public Observable<PagingData<Movie>> getPagingData(  ) {
        PagingConfig config = new PagingConfig(10, 10,
                true, 10, 200);
        Pager<Integer, MovieEntity> pager = new Pager(config, 1,
                new PageKeyedRemoteMediator(this, mAppDb, mApiService, mapper),
                () -> mAppDb.movieDao().pagingSource());

        Observable<PagingData<Movie>> pagingDataObservable = PagingRx.getObservable(pager)
                .map(paging -> PagingDataTransforms.map(paging, EXECUTOR, mapper::entityToDomain));
        return pagingDataObservable;

    }


}
