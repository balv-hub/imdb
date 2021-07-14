package com.balv.imdb.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class MovieEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public String released;
    public String time;
    public String genre;
    public String director;
    public String writer;
    public String actors;
    public String poster;
    public String imdbRated;
    public String plot;
}
