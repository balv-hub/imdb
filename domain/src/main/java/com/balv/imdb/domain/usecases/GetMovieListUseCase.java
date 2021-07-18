package com.balv.imdb.domain.usecases;

import com.balv.imdb.domain.models.Movie;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import kotlin.Unit;

public class GetMovieListUseCase extends BaseUseCase<Unit, List<Movie>> {
    @Inject
    public GetMovieListUseCase(){}
    @Override
    public Observable<List<Movie>> execute(Unit input) {
        return mMovieRepo.getLocalMovieData();
    }
}
