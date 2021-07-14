package com.balv.imdb.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.balv.imdb.data.local.dao.MovieDao;
import com.balv.imdb.data.model.MovieEntity;

@Database(entities = {MovieEntity.class}, version = 1)
public abstract class AppDb extends RoomDatabase {
    public abstract MovieDao movieDao();
}
