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
        mMovieRepo.getDetailFromNetwork(id);
        return mMovieRepo.getMovieDetail(id);
/*                .map(movie -> {
                    if (movie.getImdbRated() == null || movie.getImdbRated().isEmpty()) {
                        mMovieRepo.getDetailFromNetwork(id);
                    }
                    return movie;
                })*/
    }
}
