package com.balv.imdb.domain.usecases

import com.balv.imdb.domain.models.MovieDetail
import com.balv.imdb.domain.repositories.IMovieRepository
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetMovieCreditUseCase @Inject constructor(
    mMovieRepo: IMovieRepository
) : BaseUseCase<Int, MovieDetail?>(mMovieRepo) {

    override suspend fun execute(input: Int): MovieDetail? {
        val mvCredit = movieRepository.getMovieCredits(input)
        val detail = movieRepository.getMovieDetailById(input)
        val newDetail = detail?.copy(
            crewMembers = mvCredit.crew,
            castMembers = mvCredit.cast
        )?.also {
            movieRepository.updateMovieDetail(it)
        }
        return newDetail
    }

    companion object {
         val DETAIL_POLL_THRESHOLD = TimeUnit.MINUTES.toMillis(1)
    }
}
