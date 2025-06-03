package com.balv.imdb.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.balv.imdb.data.model.entity.GenreEntity

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<GenreEntity>)

    @Query("DELETE FROM genres")
    suspend fun clearAllGenres()

    @Transaction
    suspend fun replaceWithNewGenres(list: List<GenreEntity>) {
        clearAllGenres()
        insertAll(list)
    }

    @Query("SELECT name FROM genres WHERE id IN (:ids)")
    suspend fun getNamesById(ids: List<Int>): List<String>
}