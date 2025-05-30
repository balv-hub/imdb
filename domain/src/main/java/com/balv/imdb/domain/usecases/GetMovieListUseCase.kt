package com.balv.imdb.domain.usecases

import androidx.paging.PagingData
import com.balv.imdb.domain.models.Movie
import com.balv.imdb.domain.repositories.IMovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieListUseCase @Inject constructor(
    mMovieRepo: IMovieRepository
) : BaseUseCase<Unit, Flow<PagingData<Movie>>>(mMovieRepo) {
    override suspend fun execute(input: Unit): Flow<PagingData<Movie>> {
        return mMovieRepo.allMoviesPaging()
    }
}
