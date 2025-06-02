package com.balv.imdb.domain.usecases

import com.balv.imdb.domain.repositories.IMovieRepository

abstract class BaseUseCase<TIn, TOut>(
    protected val movieRepository: IMovieRepository
) {
    abstract suspend fun execute(input: TIn): TOut
}
