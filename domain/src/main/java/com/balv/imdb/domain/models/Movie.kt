package com.balv.imdb.domain.models

data class Movie(
    val title: String? = null,
    val id: String,
    val released: String? = null,
    val time: String? = null,
    val genre: String? = null,
    val director: String? = null,
    val writer: String? = null,
    val actors: String? = null,
    val imdbRated: String? = null,
    val poster: String? = null,
    val plot: String? = null,
    val polledDate: Long? = null
)
