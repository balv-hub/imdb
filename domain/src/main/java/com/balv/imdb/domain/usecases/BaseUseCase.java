package com.balv.imdb.domain.usecases;

import com.balv.imdb.domain.repositories.IMovieRepository;


import javax.inject.Inject;

import io.reactivex.Observable;

public abstract class BaseUseCase<TIn, TOut> {
    @Inject
    IMovieRepository mMovieRepo;

    abstract public Observable<TOut> execute(TIn input);
}
