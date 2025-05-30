package com.balv.imdb.data.mapper

import com.balv.imdb.data.model.MovieData
import com.balv.imdb.data.model.MovieEntity
import com.balv.imdb.data.model.MovieNetworkObject
import com.balv.imdb.domain.models.Movie

object Mapper {
    fun networkToEntity(input: MovieNetworkObject): MovieEntity {
        val entity = MovieEntity(
            id = input.imdbID,
            title = input.title,
            released = input.year,
            poster = input.poster
        )
        return entity
    }


    fun entityToDomain(input: MovieEntity): Movie {
        return Movie( 
            id = input.id, 
            title = input.title, 
            released = input.released, 
            poster = input.poster,
            imdbRated = input.imdbRated,
            plot = input.plot,
            writer = input.writer,
            director = input.director,
            actors = input.actors,
            time = input.time,
            genre = input.genre
        )
    }


    fun detailToEntity(input: MovieData): MovieEntity {
        return MovieEntity(
            id = input.imdbID,
            title = input.title,
            released = input.released,
            poster = input.poster,
            imdbRated = input.imdbRating,
            plot = input.plot,
            writer = input.writer,
            director = input.director,
            actors = input.actors,
            time = input.runtime,
            genre = input.genre
        )
    }


    fun networkToDomain(input: MovieData): Movie {
        return Movie( 
            title = input.title,
            id = input.imdbID,
            released = input.released, 
            time = input.runtime, 
            genre = input.genre, 
            director = input.director, 
            writer = input.writer, 
            actors = input.actors, 
            imdbRated = input.imdbRating, 
            poster = input.poster, 
            plot = input.plot, 
            polledDate = System.currentTimeMillis() 
        )
    }
}
