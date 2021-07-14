package com.balv.imdb.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.balv.imdb.data.model.MovieEntity;

import java.util.List;

@Dao
public abstract class MovieDao {
    @Query("SELECT * FROM movies")
    public abstract LiveData<List<MovieEntity>> getMainMovieList();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertAllMovies(List<MovieEntity> movieEntities);

    @Update
    public abstract void updateMovies(MovieEntity... movieEntities);

    @Query("SELECT * FROM movies WHERE id = :id")
    public abstract LiveData<MovieEntity> getMovieLiveData(String id);

    @Delete
    public abstract void deleteItem(MovieEntity... ids);

    @Query("SELECT COUNT(*) FROM movies")
    public abstract int getCount();

    @Query("DELETE FROM movies")
    public abstract void deleteAll();
}
