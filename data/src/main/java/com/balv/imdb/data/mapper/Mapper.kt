package com.balv.imdb.data.mapper

import com.balv.imdb.data.model.MovieEntity
import com.balv.imdb.data.model.RemoteMovie
import com.balv.imdb.domain.models.Movie

object Mapper {
    fun networkToEntity(input: RemoteMovie) = MovieEntity(
            id = input.id,
            adult = input.adult,
            backdropPath = input.backdropPath,
            genreIds = input.genreIds,
            originalLanguage = input.originalLanguage,
            originalTitle = input.originalTitle,
            overview = input.overview,
            popularity = input.popularity,
            posterPath = input.posterPath,
            releaseDate = input.releaseDate,
            title = input.title,
            video = input.video,
            voteAverage = input.voteAverage,
            voteCount = input.voteCount,
        )

    fun entityToDomain(input: MovieEntity) =  Movie(
        id = input.id,
        adult = input.adult,
        backdropPath = input.backdropPath,
        genreIds = input.genreIds,
        originalLanguage = input.originalLanguage,
        originalTitle = input.originalTitle,
        overview = input.overview,
        popularity = input.popularity,
        posterPath = input.posterPath,
        releaseDate = input.releaseDate,
        title = input.title,
        video = input.video,
        voteAverage = input.voteAverage,
        voteCount = input.voteCount,
    )


    fun remoteMovieToEntity(input: RemoteMovie): MovieEntity {
        return MovieEntity(
            id = input.id,
            adult = input.adult,
            backdropPath = input.backdropPath,
            genreIds = input.genreIds,
            originalLanguage = input.originalLanguage,
            originalTitle = input.originalTitle,
            overview = input.overview,
            popularity = input.popularity,
            posterPath = input.posterPath,
            releaseDate = input.releaseDate,
            title = input.title,
            video = input.video,
            voteAverage = input.voteAverage,
            voteCount = input.voteCount,
        )
    }


    fun networkToDomain(input: RemoteMovie): Movie {
        return Movie(
            id = input.id,
            adult = input.adult,
            backdropPath = input.backdropPath,
            genreIds = input.genreIds,
            originalLanguage = input.originalLanguage,
            originalTitle = input.originalTitle,
            overview = input.overview,
            popularity = input.popularity,
            posterPath = input.posterPath,
            releaseDate = input.releaseDate,
            title = input.title,
            video = input.video,
            voteAverage = input.voteAverage,
            voteCount = input.voteCount,
            polledDate = System.currentTimeMillis() 
        )
    }
}
