package com.balv.imdb.domain.usecases;


import com.balv.imdb.domain.models.ApiResult;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetNextMoviePageUseCase extends BaseUseCase<Integer, ApiResult> {

    @Inject
    public GetNextMoviePageUseCase(){};

    @Override
    public Observable<ApiResult> execute(Integer page){
        return mMovieRepo.getNextRemoteDataPage(page);
    }
}
