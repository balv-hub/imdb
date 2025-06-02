package com.balv.imdb.domain.usecases

import com.balv.imdb.domain.repositories.IMovieRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetGenresResultUseCase @Inject constructor(
    mMovieRepo: IMovieRepository
) : BaseUseCase<List<Int>, List<String>>(mMovieRepo) {
    override suspend fun execute(input: List<Int>): List<String> {
        val refreshedDate = movieRepository.getGenreFetchedDate()
        if (System.currentTimeMillis() - refreshedDate > GENRE_POLL_THRESHOLD) {
            movieRepository.refreshGenreList()
        }
        return movieRepository.getGenresNameById(input)
    }

    companion object {
        val GENRE_POLL_THRESHOLD = TimeUnit.MINUTES.toMillis(1)
    }
}