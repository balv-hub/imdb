package com.balv.imdb.domain.usecases;

import android.annotation.SuppressLint;


import com.balv.imdb.domain.models.Movie;

import javax.inject.Inject;

import io.reactivex.Observable;

@SuppressLint("CheckResult")
public class GetMovieDetailUseCase extends BaseUseCase<String, Movie> {
    @Inject
    public GetMovieDetailUseCase(){}

    @Override
    public Observable<Movie> execute(String id) {
        return mMovieRepo.getMovieDetail(id);
    }
}
