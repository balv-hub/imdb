package com.balv.imdb.domain.usecases;

import android.annotation.SuppressLint;

import com.balv.imdb.domain.models.ApiResult;
import com.balv.imdb.domain.models.Movie;

import javax.inject.Inject;

import io.reactivex.Observable;

@SuppressLint("CheckResult")
public class GetDetailFromNetwork extends BaseUseCase<String, ApiResult> {
    @Inject
    public GetDetailFromNetwork(){}

    @Override
    public Observable<ApiResult> execute(String id) {
        return  mMovieRepo.getDetailFromNetwork(id);
    }
}
