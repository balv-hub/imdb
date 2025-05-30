package com.balv.imdb.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: String,
    val title: String? = null,
    val released: String? = null,
    val time: String? = null,
    val genre: String? = null,
    val director: String? = null,
    val writer: String? = null,
    val actors: String? = null,
    val poster: String? = null,
    val imdbRated: String? = null,
    val plot: String? = null
)
