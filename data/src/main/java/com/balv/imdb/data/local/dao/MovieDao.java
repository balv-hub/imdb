package com.balv.imdb.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.PagingLiveData;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.balv.imdb.data.model.MovieEntity;

import java.util.List;

import io.reactivex.Observable;

@Dao
public abstract class MovieDao {
    @Query("SELECT * FROM movies")
    public abstract Observable<List<MovieEntity>> getMainMovieList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<MovieEntity> movieEntities);

    @Update
    public abstract void updateMovies(MovieEntity... movieEntities);

    @Query("SELECT * FROM movies WHERE id = :id")
    public abstract Observable<MovieEntity> setMovieDetailSource(String id);

    @Delete
    public abstract void deleteItem(MovieEntity... ids);

    @Query("SELECT COUNT(*) FROM movies")
    public abstract int getCount();

    @Query("DELETE FROM movies")
    public abstract void clearAll();

    @Query("SELECT * FROM movies")
    public abstract PagingSource<Integer, MovieEntity> pagingSource();

}
