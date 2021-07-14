package com.balv.imdb.data.mediator;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.LoadType;
import androidx.paging.PagingState;
import androidx.paging.RemoteMediator;
import androidx.paging.rxjava2.RxRemoteMediator;

import com.balv.imdb.data.Constant;
import com.balv.imdb.data.local.AppDb;
import com.balv.imdb.data.mapper.Mapper;
import com.balv.imdb.data.network.ApiService;

import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import kotlin.coroutines.Continuation;

public class PageKeyedRemoteMediator extends RxRemoteMediator {
    AppDb appDb;
    ApiService apiService;
    Mapper mapper;

    @Inject
    public PageKeyedRemoteMediator(AppDb db, ApiService apiService, Mapper mapper) {
        appDb = db;
        this.apiService = apiService;
        this.mapper = mapper;
    }

    @SuppressLint("CheckResult")
    @NonNull
    @Override
    public Single<MediatorResult> loadSingle(@NonNull LoadType loadType, @NonNull PagingState pagingState) {
        int pageNumber = 1;
        switch (loadType){
            case REFRESH:
                pageNumber = 1;
            case APPEND:
                pageNumber = appDb.movieDao().getCount()/10 + 1;

            case PREPEND:
                return Single.just(new MediatorResult.Success(true));
        }
        apiService.getMoviesList(Constant.API_KEY, Constant.MOVIE_KEYWORD, pageNumber).subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    appDb.runInTransaction(() -> {
                        if (loadType == LoadType.REFRESH) {
                            appDb.movieDao().deleteAll();
                        }
                        appDb.movieDao().insertAllMovies(result.list.stream().map(nw -> mapper.networkToEntity(nw)).collect(Collectors.toList()));
                    });

                }, Throwable::printStackTrace);
        return null;
    }
}
