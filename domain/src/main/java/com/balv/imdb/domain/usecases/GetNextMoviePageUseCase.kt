package com.balv.imdb.domain.usecases

import com.balv.imdb.domain.models.ApiResult
import com.balv.imdb.domain.repositories.IMovieRepository
import io.reactivex.Observable
import javax.inject.Inject


class GetNextMoviePageUseCase @Inject constructor(
    mMovieRepo: IMovieRepository
) : BaseUseCase<Int, ApiResult<*>?>(mMovieRepo) {
    override suspend fun execute(input: Int): ApiResult<*> {
        return mMovieRepo.getNextRemoteDataPage(input)
    }
}
