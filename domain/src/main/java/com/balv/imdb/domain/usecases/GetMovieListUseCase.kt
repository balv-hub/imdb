package com.balv.imdb.domain.usecases

import androidx.paging.PagingData
import com.balv.imdb.domain.models.Movie
import com.balv.imdb.domain.repositories.IMovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieListUseCase @Inject constructor(
    mMovieRepo: IMovieRepository
) : BaseUseCase<GetMovieListInput, Flow<PagingData<Movie>>>(mMovieRepo) {
    override suspend fun execute(input: GetMovieListInput): Flow<PagingData<Movie>> {
        if (input.forceRefresh) {
            movieRepository.getNextRemoteDataPage(1)
        }
        return movieRepository.allMoviesPaging()
    }
}

data class GetMovieListInput(
    val forceRefresh: Boolean
)
