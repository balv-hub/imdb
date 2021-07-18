package com.balv.imdb.data.mediator;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.LoadType;
import androidx.paging.PagingState;
import androidx.paging.rxjava2.RxRemoteMediator;

import com.balv.imdb.data.Constant;
import com.balv.imdb.data.local.AppDb;
import com.balv.imdb.data.mapper.Mapper;
import com.balv.imdb.data.model.MovieEntity;
import com.balv.imdb.data.model.SearchData;
import com.balv.imdb.data.network.ApiService;
import com.balv.imdb.domain.repositories.IMovieRepository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class PageKeyedRemoteMediator extends RxRemoteMediator {
    private static final String TAG = "PageKeyedRemoteMediator";
    AppDb appDb;
    ApiService apiService;
    Mapper mapper;
    IMovieRepository movieRepository;

    @Inject
    public PageKeyedRemoteMediator(IMovieRepository movieRepository, AppDb db, ApiService apiService, Mapper mapper) {
        this.movieRepository = movieRepository;
        appDb = db;
        this.apiService = apiService;
        this.mapper = mapper;
    }

    @SuppressLint("CheckResult")
    @NonNull
    @Override
    public Single<MediatorResult> loadSingle(@NonNull LoadType loadType, @NonNull PagingState pagingState) {
        Log.i(TAG, "loadSingle: " + pagingState);
        return Single.fromCallable(() -> {
            int pageNumber = 1;
            switch (loadType){
                case REFRESH:
                    Log.i(TAG, "loadSingle: REFRESH");
                    break;
                case APPEND:
                    Log.i(TAG, "loadSingle: APPEND " + appDb.movieDao().getCount());
                    pageNumber = (int) (Math.ceil(appDb.movieDao().getCount() / 10.0) + 1);
                    break;
                case PREPEND:
                    Log.i(TAG, "loadSingle: PREPEND");
                    return new MediatorResult.Success(true);
            }
            int finalPageNumber = pageNumber;
            MediatorResult mediatorResult = null;
            try {
                Response<SearchData>  response = apiService.getMoviesListSync(Constant.API_KEY, Constant.MOVIE_KEYWORD, finalPageNumber).execute();
                if (response.isSuccessful()) {
                    Log.i(TAG, "loadSingle getRemoteData: listSize=" + response.body().list.size());
                    List<MovieEntity> entityList = response.body().list.stream()
                            .map(networkObject -> mapper.networkToEntity(networkObject))
                            .collect(Collectors.toList());
                    appDb.movieDao().insertAll(entityList);
                    boolean endReached =  appDb.movieDao().getCount() == Integer.parseInt(response.body().totalResults);
                    mediatorResult = new MediatorResult.Success(endReached);
                }
            } catch (Exception e) {
//                e.printStackTrace();
                Log.e(TAG, "loadSingle: load error " + e.getMessage());
                mediatorResult = new MediatorResult.Error(e);
            }
            return mediatorResult;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    @Override
    public Single<InitializeAction> initializeSingle() {
        return Single.create( emitter -> {
            long cachedTime = TimeUnit.HOURS.convert(4, TimeUnit.MILLISECONDS);
            if (System.currentTimeMillis() - movieRepository.getDataRefreshDate() < cachedTime) {
                emitter.onSuccess(InitializeAction.SKIP_INITIAL_REFRESH);
            } else {
                emitter.onSuccess(InitializeAction.LAUNCH_INITIAL_REFRESH);
            }
        });
    }
}
