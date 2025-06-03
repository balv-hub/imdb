package com.balv.imdb.domain.usecases

import android.util.Log
import com.balv.imdb.domain.models.MovieDetail
import com.balv.imdb.domain.repositories.IMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    mMovieRepo: IMovieRepository
) : BaseUseCase<Int, Flow<MovieDetail?>>(mMovieRepo) {

    override suspend fun execute(input: Int): Flow<MovieDetail?> {
        val resultFlow = movieRepository.getMovieDetailLocalFlow(input).onEach { local ->
            Log.i("TAG", "execute: oneachhhhh $local")
            if (local == null || System.currentTimeMillis() - local.refreshedDate > DETAIL_POLL_THRESHOLD) {
                val network = movieRepository.getDetailFromNetwork(input)
                Log.i("TAG", "execute: exec result $network")
                if (network.isSuccess) {
                    movieRepository.updateMovieDetail(network.data!!)
                }
            }
        }
        return resultFlow
    }

    companion object {
         val DETAIL_POLL_THRESHOLD = TimeUnit.MINUTES.toMillis(1)
    }
}
