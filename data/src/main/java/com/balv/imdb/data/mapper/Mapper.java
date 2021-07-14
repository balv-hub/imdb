package com.balv.imdb.data.mapper;

import com.balv.imdb.data.model.MovieData;
import com.balv.imdb.data.model.MovieEntity;
import com.balv.imdb.data.model.MovieNetworkObject;
import com.balv.imdb.domain.models.Movie;

import javax.inject.Inject;

public class Mapper {
    @Inject
    public Mapper(){}

    public MovieEntity networkToEntity(MovieNetworkObject input) {
        MovieEntity entity = new MovieEntity();
        entity.id = input.imdbID;
        entity.title = input.title;
        entity.released = input.year;
        entity.poster = input.poster;
        return entity;
    }

    public Movie entityToDomain(MovieEntity input) {
        Movie output = new Movie();
        output.setId(input.id);
        output.setTitle(input.title);
        output.setReleased(input.released);
        output.setPoster(input.poster);
        output.setImdbRated(input.imdbRated);
        output.setPlot(input.plot);
        output.setWriter(input.writer);
        output.setDirector(input.director);
        output.setActors(input.actors);
        output.setTime(input.time);
        output.setGenre(input.genre);
        return output;
    }

    public MovieEntity detailToEntity(MovieData input) {
        MovieEntity output = new MovieEntity();
        output.id = input.imdbID;
        output.title = input.title;
        output.poster = input.poster;
        output.released = input.released;
        output.imdbRated = input.imdbRating;
        output.plot = input.plot;
        output.writer = input.writer;
        output.director = input.director;
        output.actors = input.actors;
        output.time = input.runtime;
        output.genre = input.genre;
        return output;
    }
}
