package com.balv.imdb.domain.usecases;

import com.balv.imdb.domain.repositories.IMovieRepository;

import javax.inject.Inject;

public abstract class BaseUseCase {
    @Inject
    IMovieRepository mMovieRepo;

}
