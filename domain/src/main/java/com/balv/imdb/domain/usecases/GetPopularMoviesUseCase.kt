package com.balv.imdb.domain.usecases

import com.balv.imdb.domain.models.Movie
import com.balv.imdb.domain.repositories.IMovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds

class GetPopularMoviesUseCase @Inject constructor(
    movieRepository: IMovieRepository
) : BaseUseCase<Unit, Flow<List<Movie>>>(movieRepository){
    override suspend fun execute(input: Unit): Flow<List<Movie>> {
        if (isSevenDaysPassed(movieRepository.getPopularMoviesFetchedDate())) {
            movieRepository.refreshPopularMovies()
        }
        return movieRepository.getPopularMovies()
    }
}

private fun isSevenDaysPassed(lastFetchMillis: Long): Boolean {
    return (System.currentTimeMillis() - lastFetchMillis).milliseconds >= 7.days
}