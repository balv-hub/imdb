package com.balv.imdb.domain.usecases

import com.balv.imdb.domain.models.Movie
import com.balv.imdb.domain.repositories.IMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    mMovieRepo: IMovieRepository
) : BaseUseCase<Int, Flow<Movie?>>(mMovieRepo) {

    override suspend fun execute(input: Int): Flow<Movie?> {
        val resultFlow = movieRepository.getMovieDetailLocal(input).onEach { local ->
            if (local == null || System.currentTimeMillis() - local.polledDate > DETAIL_POLL_THRESHOLD) {
                movieRepository.getDetailFromNetwork(input)
            }
        }
        return resultFlow
    }

    companion object {
         val DETAIL_POLL_THRESHOLD = TimeUnit.MINUTES.toMillis(1)
    }
}
