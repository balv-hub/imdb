package com.balv.imdb.domain.usecases;


import com.balv.imdb.domain.models.ApiResult;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetNextMoviePageUseCase extends BaseUseCase {

    @Inject
    public GetNextMoviePageUseCase(){};

    public Observable<ApiResult> execute(int page){
        return mMovieRepo.getNextRemoteDataPage(page);
    }
}
