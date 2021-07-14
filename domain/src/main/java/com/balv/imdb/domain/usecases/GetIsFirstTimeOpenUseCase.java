package com.balv.imdb.domain.usecases;

import javax.inject.Inject;

public class GetIsFirstTimeOpenUseCase extends BaseUseCase {
    @Inject
    public GetIsFirstTimeOpenUseCase(){};

    public boolean initialized(){
        return mMovieRepo.initialized();
    }
}
