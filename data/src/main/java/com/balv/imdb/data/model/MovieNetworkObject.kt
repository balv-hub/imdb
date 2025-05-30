package com.balv.imdb.data.model

import com.google.gson.annotations.SerializedName

data class MovieNetworkObject(
    @SerializedName("Title")
    val title: String? = null,

    @SerializedName("Year")
    val year: String? = null,

    @SerializedName("imdbID")
    val imdbID: String,

    @SerializedName("Type")
    val type: String? = null,

    @SerializedName("Poster")
    val poster: String? = null
)
